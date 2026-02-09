package com.sporty.live_events_service.service;

import com.sporty.live_events_service.model.EventStatus;

public interface EventService {
    void updateStatus(String eventId, EventStatus eventStatus);
}
