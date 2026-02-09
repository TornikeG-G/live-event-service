package com.sporty.live_events_service.controller;

import com.sporty.live_events_service.model.EventStatusRequest;
import com.sporty.live_events_service.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/events/status")
    public void getEvent(@RequestBody @Valid EventStatusRequest request) {
        eventService.updateStatus(request.eventId(), request.status());
    }
}
