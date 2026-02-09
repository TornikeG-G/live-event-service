package com.sporty.live_events_service.service;

import com.sporty.live_events_service.client.ExternalApiClient;
import com.sporty.live_events_service.messaging.KafkaPublisher;
import com.sporty.live_events_service.model.EventApiResponse;
import com.sporty.live_events_service.service.impl.SchedulerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import static java.lang.Thread.sleep;

class SchedulerServiceImplTest {

    private SchedulerServiceImpl schedulerService;
    private ExternalApiClient apiClient;
    private KafkaPublisher publisher;

    @BeforeEach
    void setup() {
        apiClient = Mockito.mock(ExternalApiClient.class);
        publisher = Mockito.mock(KafkaPublisher.class);
        schedulerService = new SchedulerServiceImpl(
                new ConcurrentTaskScheduler(),
                apiClient,
                publisher
        );
    }

    @Test
    void testStartAndExecute() throws Exception {
        EventApiResponse response = new EventApiResponse("1234", "1:0");
        Mockito.when(apiClient.fetch("1234")).thenReturn(response);

        schedulerService.start("1234");
        sleep(11);
        Mockito.verify(publisher, Mockito.atLeastOnce()).publish(response);

        schedulerService.stop("1234");
    }
}
