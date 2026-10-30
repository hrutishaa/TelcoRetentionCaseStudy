# User Stories

**US-1 — Weekly risk list**
As a retention lead, I want a weekly list of at-risk first-year customers so my team can call them before they cancel.
- Given a customer is month-to-month with tenure ≤ 12 months, when the weekly job runs, then they get a risk score and a reason code.
- Given a score is above the agreed threshold, then the customer lands in the retention queue.
- The list refreshes every Monday by 8 AM.

**US-2 — Reason-based offers**
As a retention agent, I want to see why a customer was flagged so I can pick the right offer.
- Given a flagged customer, when I open their record, then I see the top reason (e.g. no tech support, electronic check, month-to-month).
- The offer list is filtered to offers that match the reason.

**US-3 — Outcome logging**
As a retention agent, I want to log the result of each call in under 30 seconds.
- Outcomes are picked from a fixed list (saved – contract, saved – autopay, saved – add-on, declined, unreachable).
- A call cannot be closed without an outcome.

**US-4 — Autopay nudge**
As a billing manager, I want new electronic-check customers offered autopay at signup.
- Given a new customer selects electronic check, then the autopay offer is shown once with the credit amount.
- The acceptance is written to the customer record.

**US-5 — Results reporting**
As the Head of Customer Care, I want monthly saved-customer and saved-MRR numbers versus a control group so I know if the program pays off.
- A random 10% of flagged customers are held out and never contacted.
- The report shows churn for treated vs. control and the difference in MRR.
