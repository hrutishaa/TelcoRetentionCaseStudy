"""Load the Telco churn CSV into SQLite, run sql/sizing.sql and size the opportunity."""
import csv, sqlite3, json, pathlib
ROOT = pathlib.Path(__file__).resolve().parents[1]

def load(db=":memory:"):
    con = sqlite3.connect(db)
    with open(ROOT / "data/Telco-Customer-Churn.csv") as f:
        rows = list(csv.DictReader(f))
    cols = list(rows[0].keys())
    con.execute(f"CREATE TABLE customers ({', '.join(cols)})")
    for r in rows:
        r["tenure"] = int(r["tenure"]); r["MonthlyCharges"] = float(r["MonthlyCharges"])
        r["TotalCharges"] = float(r["TotalCharges"]) if r["TotalCharges"].strip() else None
    con.executemany(f"INSERT INTO customers VALUES ({','.join('?'*len(cols))})",
                    [tuple(r[c] for c in cols) for r in rows])
    return con

def size(con, save_rate=0.15):
    q = lambda s: con.execute(s).fetchone()
    total, churned, lost = q("SELECT COUNT(*), SUM(Churn='Yes'), SUM(CASE WHEN Churn='Yes' THEN MonthlyCharges END) FROM customers")
    tgt_n, tgt_mrr = q("""SELECT COUNT(*), SUM(MonthlyCharges) FROM customers
        WHERE Churn='Yes' AND Contract='Month-to-month' AND tenure<=12""")
    return {
        "customers": total, "churned": churned,
        "churn_pct": round(100 * churned / total, 1),
        "lost_mrr": round(lost),
        "target_segment_churners": tgt_n,
        "target_share_of_churners_pct": round(100 * tgt_n / churned, 1),
        "target_lost_mrr": round(tgt_mrr),
        "save_rate_assumption": save_rate,
        "saved_mrr": round(tgt_mrr * save_rate),
        "saved_arr": round(tgt_mrr * save_rate * 12),
    }

if __name__ == "__main__":
    con = load()
    for stmt in (ROOT / "sql/sizing.sql").read_text().split(";"):
        if "SELECT" in stmt:
            cur = con.execute(stmt)
            print(stmt.strip().splitlines()[0])
            print([d[0] for d in cur.description])
            for r in cur.fetchall(): print(r)
            print()
    res = size(con)
    print(json.dumps(res, indent=2))
    (ROOT / "docs/results.json").write_text(json.dumps(res, indent=2))
