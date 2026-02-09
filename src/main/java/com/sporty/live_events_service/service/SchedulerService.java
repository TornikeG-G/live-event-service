package com.sporty.live_events_service.service;

public interface SchedulerService {
    void start(String eventId);

    void stop(String eventId);
}
