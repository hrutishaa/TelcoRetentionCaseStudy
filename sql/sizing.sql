-- Churn by contract type
SELECT Contract, COUNT(*) AS customers,
       SUM(Churn='Yes') AS churned,
       ROUND(100.0*SUM(Churn='Yes')/COUNT(*),1) AS churn_pct,
       ROUND(SUM(CASE WHEN Churn='Yes' THEN MonthlyCharges END),0) AS lost_mrr
FROM customers GROUP BY Contract ORDER BY churn_pct DESC;

-- Early-life churn: month-to-month customers in their first 12 months
SELECT CASE WHEN tenure<=12 THEN '0-12 mo' ELSE '13+ mo' END AS tenure_band,
       COUNT(*) AS customers, SUM(Churn='Yes') AS churned,
       ROUND(100.0*SUM(Churn='Yes')/COUNT(*),1) AS churn_pct
FROM customers WHERE Contract='Month-to-month' GROUP BY 1;

-- Churn with vs without tech support / online security (internet customers)
SELECT TechSupport, OnlineSecurity, COUNT(*) AS customers,
       ROUND(100.0*SUM(Churn='Yes')/COUNT(*),1) AS churn_pct
FROM customers WHERE InternetService<>'No' GROUP BY 1,2 ORDER BY churn_pct DESC;

-- Payment method
SELECT PaymentMethod, COUNT(*) AS customers,
       ROUND(100.0*SUM(Churn='Yes')/COUNT(*),1) AS churn_pct
FROM customers GROUP BY 1 ORDER BY churn_pct DESC;
