package casestudy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

/** Loads the Telco churn CSV into SQLite, runs sql/sizing.sql and sizes the opportunity. */
public class SizeOpportunity {

    public static Connection load(Path csv) throws Exception {
        Connection con = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Reader in = Files.newBufferedReader(csv)) {
            var records = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(in);
            List<String> cols = records.getHeaderNames();
            try (Statement st = con.createStatement()) {
                st.execute("CREATE TABLE customers (" + String.join(", ", cols) + ")");
            }
            String ph = String.join(",", Collections.nCopies(cols.size(), "?"));
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO customers VALUES (" + ph + ")")) {
                for (CSVRecord r : records) {
                    for (int i = 0; i < cols.size(); i++) {
                        String c = cols.get(i), v = r.get(i);
                        switch (c) {
                            case "tenure" -> ps.setInt(i + 1, Integer.parseInt(v));
                            case "MonthlyCharges" -> ps.setDouble(i + 1, Double.parseDouble(v));
                            case "TotalCharges" -> { if (v.isBlank()) ps.setNull(i + 1, Types.REAL); else ps.setDouble(i + 1, Double.parseDouble(v)); }
                            default -> ps.setString(i + 1, v);
                        }
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            con.commit();
        }
        return con;
    }

    public static Map<String, Object> size(Connection con, double saveRate) throws SQLException {
        Map<String, Object> out = new LinkedHashMap<>();
        try (Statement st = con.createStatement()) {
            ResultSet a = st.executeQuery("SELECT COUNT(*), SUM(Churn='Yes'), SUM(CASE WHEN Churn='Yes' THEN MonthlyCharges END) FROM customers");
            a.next();
            int total = a.getInt(1), churned = a.getInt(2); double lost = a.getDouble(3);
            ResultSet b = st.executeQuery("SELECT COUNT(*), SUM(MonthlyCharges) FROM customers WHERE Churn='Yes' AND Contract='Month-to-month' AND tenure<=12");
            b.next();
            int tgtN = b.getInt(1); double tgtMrr = b.getDouble(2);
            out.put("customers", total);
            out.put("churned", churned);
            out.put("churn_pct", round1(100.0 * churned / total));
            out.put("lost_mrr", Math.round(lost));
            out.put("target_segment_churners", tgtN);
            out.put("target_share_of_churners_pct", round1(100.0 * tgtN / churned));
            out.put("target_lost_mrr", Math.round(tgtMrr));
            out.put("save_rate_assumption", saveRate);
            out.put("saved_mrr", Math.round(tgtMrr * saveRate));
            out.put("saved_arr", Math.round(tgtMrr * saveRate * 12));
        }
        return out;
    }

    static double round1(double x) { return Math.round(x * 10) / 10.0; }

    static void runQueries(Connection con, Path sqlFile) throws Exception {
        for (String stmt : Files.readString(sqlFile).split(";")) {
            if (!stmt.contains("SELECT")) continue;
            System.out.println(stmt.strip().lines().findFirst().orElse(""));
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(stmt)) {
                ResultSetMetaData md = rs.getMetaData();
                List<String> head = new ArrayList<>();
                for (int i = 1; i <= md.getColumnCount(); i++) head.add(md.getColumnLabel(i));
                System.out.println(head);
                while (rs.next()) {
                    List<Object> row = new ArrayList<>();
                    for (int i = 1; i <= md.getColumnCount(); i++) row.add(rs.getObject(i));
                    System.out.println(row);
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        Connection con = load(Path.of("data/Telco-Customer-Churn.csv"));
        runQueries(con, Path.of("sql/sizing.sql"));
        Map<String, Object> res = size(con, 0.15);
        ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        String json = om.writeValueAsString(res);
        System.out.println(json);
        Files.writeString(Path.of("docs/results.json"), json);
    }
}
