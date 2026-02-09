package com.sporty.live_events_service.controller;

import com.sporty.live_events_service.model.EventApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/mock-api")
public class MockExternalApiController {

    private final Random random = new Random();

    @GetMapping("/event/{eventId}")
    public EventApiResponse getEvent(@PathVariable String eventId) {
        int home = random.nextInt(6); // 0–5
        int away = random.nextInt(6);

        return new EventApiResponse(
                eventId,
                home + ":" + away
        );
    }
}
