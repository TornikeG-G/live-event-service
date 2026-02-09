package com.sporty.live_events_service.messaging;

import com.sporty.live_events_service.model.EventApiResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

class KafkaPublisherTest {

    @Test
    void testPublishSuccess() {
        KafkaTemplate<String, EventApiResponse> kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        KafkaPublisher publisher = new KafkaPublisher(kafkaTemplate);

        EventApiResponse event = new EventApiResponse("1234", "1:0");
        publisher.publish(event);

        Mockito.verify(kafkaTemplate).send("live-events", "1234", event);
    }

    @Test
    void testPublishRetry() {
        KafkaTemplate<String, EventApiResponse> kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        Mockito.doThrow(new RuntimeException("fail"))
                .when(kafkaTemplate).send(Mockito.anyString(), Mockito.anyString(), Mockito.any());

        KafkaPublisher publisher = new KafkaPublisher(kafkaTemplate);
        EventApiResponse event = new EventApiResponse("1234", "1:0");

        publisher.publish(event);

        Mockito.verify(kafkaTemplate, Mockito.times(3))
                .send(Mockito.anyString(), Mockito.anyString(), Mockito.any());
    }
}
