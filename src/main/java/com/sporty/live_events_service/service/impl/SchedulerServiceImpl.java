package com.sporty.live_events_service.service.impl;

import com.sporty.live_events_service.client.ExternalApiClient;
import com.sporty.live_events_service.messaging.KafkaPublisher;
import com.sporty.live_events_service.model.EventApiResponse;
import com.sporty.live_events_service.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class SchedulerServiceImpl implements SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerServiceImpl.class);
    private final TaskScheduler scheduler;
    private final ExternalApiClient apiClient;
    private final KafkaPublisher publisher;
    private final Map<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    public SchedulerServiceImpl(final TaskScheduler scheduler,
                                final ExternalApiClient apiClient,
                                final KafkaPublisher publisher) {
        this.scheduler = scheduler;
        this.apiClient = apiClient;
        this.publisher = publisher;
    }

    @Override
    public void start(String eventId) {
        if (tasks.containsKey(eventId)) return;

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(
                () -> execute(eventId),
                Duration.ofSeconds(10)
        );

        tasks.put(eventId, future);
        log.info("Scheduler started for event {}", eventId);
    }

    @Override
    public void stop(String eventId) {
        ScheduledFuture<?> future = tasks.remove(eventId);
        if (future != null) {
            future.cancel(true);
            log.info("Scheduler stopped for event {}", eventId);
        }
    }

    private void execute(String eventId) {
        try {
            EventApiResponse response = apiClient.fetch(eventId);
            publisher.publish(response);
            log.info("Published event data for event {}: {}", eventId, response);

        } catch (Exception e) {
            log.error("Processing failed for event {}", eventId, e);
        }
    }
}
