# Bajaj Finserv Health Qualifier 1 - Java Solution

## Project Overview

This Spring Boot application automatically executes the Bajaj Finserv Health hiring challenge workflow:

1. On startup, sends a POST request to generate a webhook
2. Solves SQL Problem 1 (for odd regNo ending)
3. Submits the SQL solution to the webhook URL using JWT authentication

## Student Details

- **Name**: K Harsha Vardhan Reddy
- **Registration Number**: 22bce20327
- **Email**: harshavardhan.22bce20327@vitapstudent.ac.in

## SQL Solution (Question 1)

The application solves the following problem:
*Find the highest salary that was credited to an employee, but only for transactions that were not made on the 1st day of any month. Along with the salary, extract employee data like name, age and department.*

```sql
SELECT t.SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, e.AGE, d.DEPARTMENT_NAME
FROM TRANSACTIONS t
JOIN EMPLOYEES e ON t.EMP_ID = e.EMP_ID
JOIN DEPARTMENTS d ON e.DEPT_ID = d.DEPT_ID
WHERE DAY(t.TRANSACTION_DATE) <> 1
ORDER BY t.SALARY DESC
LIMIT 1;
```

## How to Run

1. Compile: `javac WebhookSqlSolution.java`
2. Run: `java WebhookSqlSolution`
3. Or run the JAR: `java -jar webhook-sql-solution-1.0.0.jar`

## Files

- `WebhookSqlSolution.java` - Main application file
- `webhook-sql-solution-1.0.0.jar` - Executable JAR file
- Spring Boot project files (pom.xml, src/main/java/...)

## Requirements Met

- ✅ Uses REST API calls (without external dependencies)
- ✅ No controller/endpoint triggers the flow
- ✅ Uses JWT in Authorization header
- ✅ Automatic execution on startup
- ✅ Submits correct SQL solution for Question 1

## JAR Download

The executable JAR file is available in this repository: `webhook-sql-solution-1.0.0.jar`
