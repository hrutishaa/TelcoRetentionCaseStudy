package casestudy;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class SizeOpportunityTest {
    @Test
    void sizesTheOpportunity() throws Exception {
        Map<String, Object> r = SizeOpportunity.size(SizeOpportunity.load(Path.of("data/Telco-Customer-Churn.csv")), 0.15);
        assertEquals(7043, r.get("customers"));
        assertEquals(1869, r.get("churned"));
        assertEquals(26.5, r.get("churn_pct"));
        assertEquals(1024, r.get("target_segment_churners"));
        assertEquals(54.8, r.get("target_share_of_churners_pct"));
        long tgt = (long) r.get("target_lost_mrr");
        assertTrue(Math.abs((long) r.get("saved_arr") - tgt * 0.15 * 12) < 2);
    }
}
