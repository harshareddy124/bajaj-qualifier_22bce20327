package com.bajajfinservhealth.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class StartupTask {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() throws Exception {
        // 1. Generate webhook
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        Map<String, String> requestBody = new HashMap<>();
    requestBody.put("name", "Harsha vardhan reddy");
    requestBody.put("regNo", "22bce20327");
    requestBody.put("email", "harshavardhan.22bce20327@vitapstudent.ac.in");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            System.err.println("Failed to generate webhook: " + response.getStatusCode());
            return;
        }
        JsonNode json = objectMapper.readTree(response.getBody());
        String webhookUrl = json.get("webhook").asText();
        String accessToken = json.get("accessToken").asText();

        // 2. Prepare SQL query for Question 1 (replace below with your actual solution)
        String finalQuery = "SELECT t.SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, e.AGE, d.DEPARTMENT_NAME\n"
            + "FROM TRANSACTIONS t\n"
            + "JOIN EMPLOYEES e ON t.EMP_ID = e.EMP_ID\n"
            + "JOIN DEPARTMENTS d ON e.DEPT_ID = d.DEPT_ID\n"
            + "WHERE DAY(t.TRANSACTION_DATE) <> 1\n"
            + "ORDER BY t.SALARY DESC\n"
            + "LIMIT 1;";

        // 3. Submit the solution
        Map<String, String> answerBody = new HashMap<>();
        answerBody.put("finalQuery", finalQuery);
        HttpHeaders answerHeaders = new HttpHeaders();
        answerHeaders.setContentType(MediaType.APPLICATION_JSON);
        answerHeaders.setBearerAuth(accessToken);
        HttpEntity<Map<String, String>> answerEntity = new HttpEntity<>(answerBody, answerHeaders);

        ResponseEntity<String> answerResponse = restTemplate.postForEntity(webhookUrl, answerEntity, String.class);
        if (answerResponse.getStatusCode() == HttpStatus.OK) {
            System.out.println("Solution submitted successfully!");
        } else {
            System.err.println("Failed to submit solution: " + answerResponse.getStatusCode());
        }
    }
}
