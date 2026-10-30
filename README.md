# Telco Retention Case Study

A business analysis case study on customer churn at a subscription telecom provider. I treated it like a real BA engagement: write down the problem, map how retention works today, find the gaps, and size the opportunity with SQL before proposing anything.

**Data:** IBM's public *Telco Customer Churn* sample dataset (7,043 customers, 21 columns) —
https://github.com/IBM/telco-customer-churn-on-icp4d/blob/master/data/Telco-Customer-Churn.csv

## What's in here

| File | What it is |
|---|---|
| [docs/BRD.md](docs/BRD.md) | Business requirements document |
| [docs/user-stories.md](docs/user-stories.md) | User stories with acceptance criteria |
| [docs/process-maps.md](docs/process-maps.md) | As-is and to-be retention process (Mermaid) |
| [docs/gap-analysis.md](docs/gap-analysis.md) | Gap analysis between the two |
| [sql/sizing.sql](sql/sizing.sql) | Queries used to find and size the problem |
| [scripts/size_opportunity.py](scripts/size_opportunity.py) | Loads the CSV into SQLite, runs the queries, writes `docs/results.json` |

## Key findings

- Overall churn is **26.5%** (1,869 of 7,043 customers), about **$139K** in monthly recurring revenue walking out.
- Month-to-month customers churn at **42.7%**, versus 11.3% on one-year and 2.8% on two-year contracts.
- Month-to-month customers in their **first 12 months** churn at **51.4%**. That one group is **1,024 churners, 54.8% of all churn**, and **$68.3K** of lost MRR.
- Internet customers with neither tech support nor online security churn at **49.0%**; with both, **9.0%**.
- Customers paying by electronic check churn at **45.3%**, roughly triple the automatic payment methods (15–17%).

**Opportunity:** if an early-life retention play saves 15% of that first-year month-to-month group (an assumption, not a measured result), that is about **$10.2K MRR / $123K ARR** kept.

## Run it

```bash
python scripts/size_opportunity.py   # prints every query result
python -m pytest -q
```

No dependencies beyond the Python standard library (plus pytest for tests).
