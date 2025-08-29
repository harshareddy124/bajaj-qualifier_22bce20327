import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class WebhookSqlSolution {
    public static void main(String[] args) {
        try {
            System.out.println("=== Bajaj Finserv Health Qualifier 1 - Starting ===");
            
            // 1. Generate webhook
            System.out.println("Step 1: Sending POST request to generate webhook...");
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            String requestBody = "{\"name\":\"Tanguturi Venkata Thanuj\",\"regNo\":\"22bce20003\",\"email\":\"venkata.22bce20003@vitapstudent.ac.in\"}";
            
            System.out.println("Request URL: " + url);
            System.out.println("Request Body: " + requestBody);
            
            String response = sendPostRequest(url, requestBody, null);
            System.out.println("Webhook generation response: " + response);
            
            // Parse response to extract webhook and accessToken
            System.out.println("Step 2: Parsing response to extract webhook and accessToken...");
            String webhook = extractValue(response, "webhook");
            String accessToken = extractValue(response, "accessToken");
            
            System.out.println("Extracted webhook: " + webhook);
            System.out.println("Extracted accessToken: " + (accessToken != null ? "***HIDDEN***" : "null"));
            
            if (webhook == null || accessToken == null) {
                System.err.println("ERROR: Failed to extract webhook or accessToken from response");
                return;
            }
            
            // 2. Prepare SQL query for Question 1
            System.out.println("Step 3: Preparing SQL query for Question 1...");
            String finalQuery = "SELECT t.SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, e.AGE, d.DEPARTMENT_NAME\\n" +
                "FROM TRANSACTIONS t\\n" +
                "JOIN EMPLOYEES e ON t.EMP_ID = e.EMP_ID\\n" +
                "JOIN DEPARTMENTS d ON e.DEPT_ID = d.DEPT_ID\\n" +
                "WHERE DAY(t.TRANSACTION_DATE) <> 1\\n" +
                "ORDER BY t.SALARY DESC\\n" +
                "LIMIT 1;";
            
            System.out.println("SQL Query prepared successfully");
            
            // 3. Submit the solution
            System.out.println("Step 4: Submitting solution to webhook...");
            String answerBody = "{\"finalQuery\":\"" + finalQuery.replace("\"", "\\\"") + "\"}";
            System.out.println("Submitting to URL: " + webhook);
            String answerResponse = sendPostRequest(webhook, answerBody, accessToken);
            
            System.out.println("Solution submission response: " + answerResponse);
            System.out.println("=== SOLUTION SUBMITTED SUCCESSFULLY! ===");
            
        } catch (Exception e) {
            System.err.println("ERROR occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String sendPostRequest(String urlString, String body, String bearerToken) throws IOException {
        System.out.println("Sending POST request to: " + urlString);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        if (bearerToken != null) {
            conn.setRequestProperty("Authorization", bearerToken);
            System.out.println("Using Authorization header: " + bearerToken.substring(0, Math.min(20, bearerToken.length())) + "...");
        }
        conn.setDoOutput(true);
        
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        int responseCode = conn.getResponseCode();
        System.out.println("Response code: " + responseCode);
        
        InputStream inputStream;
        if (responseCode >= 200 && responseCode < 300) {
            inputStream = conn.getInputStream();
        } else {
            inputStream = conn.getErrorStream();
        }
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
    
    private static String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return null;
        
        startIndex += searchKey.length();
        int endIndex = json.indexOf("\"", startIndex);
        if (endIndex == -1) return null;
        
        return json.substring(startIndex, endIndex);
    }
}
