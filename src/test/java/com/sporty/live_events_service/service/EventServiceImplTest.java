package com.sporty.live_events_service.service;

import com.sporty.live_events_service.model.EventStatus;
import com.sporty.live_events_service.model.EventStatusRequest;
import com.sporty.live_events_service.service.impl.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EventServiceImplTest {

    private SchedulerService schedulerService;
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        schedulerService = Mockito.mock(SchedulerService.class);
        eventService = new EventServiceImpl(schedulerService);
    }

    @Test
    void updateStatus_shouldStartScheduler_whenEventIsLive() {
        EventStatusRequest request = new EventStatusRequest("1234", EventStatus.LIVE);

        eventService.updateStatus(request.eventId(), request.status());

        Mockito.verify(schedulerService).start("1234");
        Mockito.verify(schedulerService, Mockito.never()).stop(Mockito.any());
    }

    @Test
    void updateStatus_shouldStopScheduler_whenEventIsNotLive() {
        EventStatusRequest request = new EventStatusRequest("1234", EventStatus.NOT_LIVE);

        eventService.updateStatus(request.eventId(), request.status());

        Mockito.verify(schedulerService).stop("1234");
        Mockito.verify(schedulerService, Mockito.never()).start(Mockito.any());
    }
}