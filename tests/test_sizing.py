from scripts.size_opportunity import load, size
def test_counts():
    r = size(load())
    assert r["customers"] == 7043
    assert 0 < r["target_segment_churners"] < r["churned"]
    assert r["saved_arr"] == round(r["target_lost_mrr"] * 0.15 * 12)
