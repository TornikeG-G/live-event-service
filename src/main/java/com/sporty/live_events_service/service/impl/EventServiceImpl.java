package com.sporty.live_events_service.service.impl;

import com.sporty.live_events_service.model.EventStatus;
import com.sporty.live_events_service.service.EventService;
import com.sporty.live_events_service.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);
    private final SchedulerService schedulerService;
    private final Map<String, EventStatus> events = new ConcurrentHashMap<>();

    public EventServiceImpl(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Override
    public void updateStatus(String eventId, EventStatus status) {
        events.put(eventId, status);
        log.info("Event {} set to {}", eventId, status);

        if (status == EventStatus.LIVE) {
            schedulerService.start(eventId);
        } else {
            schedulerService.stop(eventId);
        }
    }
}
