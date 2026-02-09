package com.sporty.live_events_service.client;

import com.sporty.live_events_service.model.EventApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalApiClient {

    private static final Logger log = LoggerFactory.getLogger(ExternalApiClient.class);
    private static final String API_URL =
            "http://localhost:8080/mock-api/event/{eventId}";
    private final RestTemplate restTemplate;

    public ExternalApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EventApiResponse fetch(String eventId) {
        try {
            EventApiResponse response =
                    restTemplate.getForObject(API_URL, EventApiResponse.class, eventId);

            log.info("Fetched external data for event {}", eventId);
            return response;

        } catch (Exception e) {
            log.error("External API call failed for event {}", eventId, e);
            throw e;
        }
    }
}

