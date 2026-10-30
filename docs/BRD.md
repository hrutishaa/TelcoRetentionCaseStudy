# Business Requirements Document — Early-Life Retention

## 1. Background
Churn is 26.5% across 7,043 customers, costing about $139K in monthly recurring revenue. Retention today is reactive: the team usually hears about churn when the cancellation call comes in.

## 2. Problem statement
More than half of all churn (54.8%) comes from month-to-month customers in their first year, and nobody owns that window. There is no early warning and no structured outreach before a customer decides to leave.

## 3. Objectives
1. Flag at-risk customers before they cancel, not during the cancellation call.
2. Cut churn in the first-year month-to-month segment (51.4% today).
3. Move more customers onto contracts, autopay and add-on support where it fits.

## 4. Scope
**In scope:** month-to-month customers with 0–12 months tenure, risk flagging, outreach workflow, offer catalogue, reporting.
**Out of scope:** pricing changes, network quality, billing system replacement.

## 5. Stakeholders
| Role | Interest |
|---|---|
| Head of Customer Care | Owns retention outcomes |
| Retention agents | Run outreach, log outcomes |
| Marketing | Owns offers and messaging |
| Billing | Autopay enrollment, contract changes |
| BI / Data | Risk flag, dashboards |

## 6. Business requirements
| ID | Requirement | Priority |
|---|---|---|
| BR-1 | Score every in-scope customer for churn risk weekly using contract, tenure, payment method and add-on services | Must |
| BR-2 | Route high-risk customers to a retention queue with a reason code | Must |
| BR-3 | Give agents a defined offer set (contract upgrade, autopay credit, trial of tech support / online security) | Must |
| BR-4 | Log every outreach attempt and outcome | Must |
| BR-5 | Report saved customers and saved MRR monthly against a control group | Should |
| BR-6 | Nudge electronic-check payers toward autopay at onboarding | Could |

## 7. Success metrics
- First-year month-to-month churn, measured against a held-out control group.
- Saved MRR per month.
- Share of in-scope customers contacted within 7 days of being flagged.

## 8. Assumptions and constraints
- The 15% save rate used for sizing is a planning assumption and must be validated in a pilot.
- Data is a point-in-time snapshot; the pilot needs monthly refreshes.
