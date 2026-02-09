package com.sporty.live_events_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventStatusRequest(
        @NotBlank String eventId,
        @NotNull EventStatus status
) {
}
