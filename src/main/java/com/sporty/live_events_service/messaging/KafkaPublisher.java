package com.sporty.live_events_service.messaging;

import com.sporty.live_events_service.model.EventApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaPublisher.class);
    private final KafkaTemplate<String, EventApiResponse> kafkaTemplate;

    public KafkaPublisher(final KafkaTemplate<String, EventApiResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(EventApiResponse message) {
        int attempts = 0;
        int maxAttempts = 3;
        long backoffMillis = 2000;

        while (attempts < maxAttempts) {
            try {
                kafkaTemplate.send("live-events", message.eventId(), message);
                log.info("Kafka message sent for event {}", message.eventId());
                return; // success
            } catch (Exception e) {
                attempts++;
                log.warn("Attempt {} failed for event {}. Retrying in {}ms", attempts,
                        message.eventId(), backoffMillis, e);
                sleep(backoffMillis);
            }
        }

        log.error("Kafka publish permanently failed for event {}", message.eventId());
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}


