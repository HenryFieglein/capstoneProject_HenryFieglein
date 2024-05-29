package com.kenzie.appserver.service;
import com.kenzie.appserver.repositories.EventRepository;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.service.model.Event;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import com.kenzie.capstone.service.model.Guest;
import com.kenzie.capstone.service.model.GuestData;
import com.kenzie.capstone.service.model.GuestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LambdaServiceClient lambdaServiceClient;

    @Autowired
    public EventService(LambdaServiceClient lambdaServiceClient, EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.lambdaServiceClient = lambdaServiceClient;
    }

    public Event getEventById(String id) {
        if (id == null) {
            throw new NullPointerException("id cannot be null!");
        }
        return recordToEvent(eventRepository.findById(id).get());
    }

    public Event addEvent(Event event) {
        eventRepository.save(eventToRecord(event));
        return event;
    }

    public Event updateEvent(Event event) {
        if (getEventById(event.getId()) == null)
            throw new NotFoundException("No event with id: " + event.getId() + "! ");
        eventRepository.save(eventToRecord(event));
        return event;
    }

    public void deleteEventById(String id) {
        eventRepository.deleteById(id);
    }

    public List<Event> getAllEvents() {
        Iterable<EventRecord> records = eventRepository.findAll();

        List<Event> events = new ArrayList<>();
        for (EventRecord record : records) {
            events.add(recordToEvent(record));
        }

        if (events.isEmpty())
            throw new NotFoundException("No events found!");
        return events;
    }

    public List<Guest> getEventGuests(String eventId){
        return lambdaServiceClient.getGuestDataByEventId(eventId);
    }

    public GuestData addEventGuest(GuestRequest request){
        return lambdaServiceClient.setGuestData(request);
    }

    public List<Event> getEventsToday() {
        LocalDate today = LocalDate.now();
        return eventRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today.toString(), today.toString())
                .stream().map(this::recordToEvent).collect(Collectors.toList());
    }

    public List<Event> getEventsByDate(String date) {
        return eventRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date)
                .stream().map(this::recordToEvent).collect(Collectors.toList());
    }

    public List<Event> getEventsByVenueId(String venueId) {
        return eventRepository.findByVenueId(venueId)
                .stream().map(this::recordToEvent).collect(Collectors.toList());
    }

    public void deleteAllEvents() {
        eventRepository.deleteAll();
    }

    public List<Event> getEventsByName(String name) {
        return eventRepository.findByEventNameContaining(name)
                .stream().map(this::recordToEvent).collect(Collectors.toList());
    }

    public List<Event> getEventsByUserame(String name) {
        return eventRepository.findByUsername(name)
                .stream().map(this::recordToEvent).collect(Collectors.toList());
    }

    public List<Event> getEventsByCategory(String category) {
        return eventRepository.findByCategory(category)
                .stream().map(this::recordToEvent).collect(Collectors.toList());
    }

    public List<Event> getEventsByDescription(String description) {
        return eventRepository.findByDescriptionContaining(description)
                .stream().map(this::recordToEvent).collect(Collectors.toList());
    }

    private EventRecord eventToRecord(Event event){
        EventRecord record = new EventRecord();
        record.setId(event.getId());
        record.setEventName(event.getEventName());
        record.setDescription(event.getDescription());
        record.setVenueId(event.getVenueId());
        record.setUsername(event.getUsername());
        record.setStartDate(event.getStartDate());
        record.setEndDate(event.getEndDate());
        record.setCategory(event.getCategory());
        return record;
    }

    private Event recordToEvent(EventRecord record){
        return new Event(record.getId(),
                record.getEventName(),
                record.getUsername(),
                record.getDescription(),
                record.getVenueId(),
                record.getStartDate(),
                record.getEndDate(),
                record.getCategory());
    }
}