package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.EventCreateRequest;
import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.service.EventService;
import com.kenzie.appserver.service.model.Event;
import com.kenzie.capstone.service.model.Guest;
import com.kenzie.capstone.service.model.GuestData;
import com.kenzie.capstone.service.model.GuestRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getEvents() {
        List<Event> events = eventService.getAllEvents();

        if(events == null || events.isEmpty()) return ResponseEntity.noContent().build();

        List<EventResponse> responses = events.stream().map(this::convertEvent).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable("id") String eventId,
                                                     @RequestBody EventCreateRequest updateRequest) {
        Event event = new Event(eventId,
                updateRequest.getEventName(),
                updateRequest.getUsername(),
                updateRequest.getDescription(),
                updateRequest.getVenueId(),
                updateRequest.getStartDate(),
                updateRequest.getEndDate(),
                updateRequest.getCategory());

        EventResponse response = convertEvent(eventService.updateEvent(event));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable("id") String eventId) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertEvent(event));
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventCreateRequest eventCreateRequest) {
        Event event = new Event(UUID.randomUUID().toString(),
                eventCreateRequest.getEventName(),
                eventCreateRequest.getUsername(),
                eventCreateRequest.getDescription(),
                eventCreateRequest.getVenueId(),
                eventCreateRequest.getStartDate(),
                eventCreateRequest.getEndDate(),
                eventCreateRequest.getCategory());

        EventResponse eventResponse = convertEvent(eventService.addEvent(event));

        return ResponseEntity.created(URI.create("/events/"+eventResponse.getId())).body(eventResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EventResponse> deleteEvent(@PathVariable("id") String eventId) {
        eventService.deleteEventById(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/guests/{id}")
    public ResponseEntity<List<Guest>> getGuests(@PathVariable("id") String eventId) {
        return ResponseEntity.ok(eventService.getEventGuests(eventId));
    }

    @PostMapping("/guests")
    public ResponseEntity<GuestData> rsvp(@RequestBody GuestRequest request) {
        return ResponseEntity.ok(eventService.addEventGuest(request));
    }

    @GetMapping("/today")
    public ResponseEntity<List<EventResponse>> getEventsToday() {
        return ResponseEntity.ok(eventService.getEventsToday()
                .stream().map(this::convertEvent).collect(Collectors.toList()));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<EventResponse>> getEventsByDate(@PathVariable("date") String date) {
        return ResponseEntity.ok(eventService.getEventsByDate(date)
                .stream().map(this::convertEvent).collect(Collectors.toList()));
    }

    @GetMapping("/venueId/{venueId}")
    public ResponseEntity<List<EventResponse>> getEventsByVenueId(@PathVariable("venueId") String venueId) {
        return ResponseEntity.ok(eventService.getEventsByVenueId(venueId)
                .stream().map(this::convertEvent).collect(Collectors.toList()));
    }

    @GetMapping("/eventName/{eventName}")
    public ResponseEntity<List<EventResponse>> getEventsByName(@PathVariable("eventName") String eventName) {
        return ResponseEntity.ok(eventService.getEventsByName(eventName)
                .stream().map(this::convertEvent).collect(Collectors.toList()));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<List<EventResponse>> getEventsByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(eventService.getEventsByUserame(username)
                .stream().map(this::convertEvent).collect(Collectors.toList()));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<EventResponse>> getEventsByCategory(@PathVariable("category") String category) {
        return ResponseEntity.ok(eventService.getEventsByCategory(category)
                .stream().map(this::convertEvent).collect(Collectors.toList()));
    }

    @GetMapping("/description/{description}")
    public ResponseEntity<List<EventResponse>> getEventsByDescription(@PathVariable("description") String description) {
        return ResponseEntity.ok(eventService.getEventsByDescription(description)
                .stream().map(this::convertEvent).collect(Collectors.toList()));
    }

    @DeleteMapping
    public ResponseEntity<EventResponse> deleteAllEvents() {
        eventService.deleteAllEvents();
        return ResponseEntity.noContent().build();
    }

    private EventResponse convertEvent(Event event){
        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(event.getId());
        eventResponse.setUsername(event.getUsername());
        eventResponse.setEventName(event.getEventName());
        eventResponse.setDescription(event.getDescription());
        eventResponse.setVenueId(event.getVenueId());
        eventResponse.setStartDate(event.getStartDate());
        eventResponse.setEndDate(event.getEndDate());
        eventResponse.setCategory(event.getCategory());
        return eventResponse;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({EmptyResultDataAccessException.class, NoSuchElementException.class, NotFoundException.class})
    public void notFound(){}
}