package com.sporty.live_events_service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.live_events_service.model.EventApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api-key}")
    private String apiKey;
    @Value("${openai.model}")
    private String model;

    public EventApiResponse getRandomEventData(String eventId) {
        try {
            String prompt = String.format(
                    "Generate a JSON object with fields `eventId` and `currentScore` " +
                            "for a sports event. eventId should be \"%s\" and currentScore a realistic random score like '2:1'. " +
                            "Return only JSON, nothing else.", eventId
            );

            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", List.of(Map.of("role", "user", "content", prompt))
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            Map<String, Object> response = restTemplate.postForObject(
                    "https://api.openai.com/v1/chat/completions",
                    entity,
                    Map.class
            );

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                String content = message != null ? (String) message.get("content") : "{}";

                return objectMapper.readValue(content, EventApiResponse.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new EventApiResponse(eventId, "0:0");
    }
}
