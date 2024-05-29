package com.kenzie.appserver.controller;

import com.jayway.jsonpath.JsonPath;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.EventCreateRequest;
import com.kenzie.appserver.controller.model.EventResponse;
import com.kenzie.appserver.service.EventService;
import com.kenzie.appserver.service.model.Event;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
public class EventControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    EventService eventService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getEvents_returnsListOfEvents() throws Exception {
        String id = UUID.randomUUID().toString();

        Event event = new Event(id, mockNeat.strings().valStr(), mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.uuids().valStr(), mockNeat.localDates().valStr(),
                mockNeat.localDates().valStr(), mockNeat.strings().valStr());

        eventService.addEvent(event);

        mvc.perform(get("/events")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        eventService.deleteEventById(id);
    }

    @Test
    public void getEventById_withValidId_returnsEvent() throws Exception {
        String id = UUID.randomUUID().toString();

        Event event = new Event(id, mockNeat.strings().valStr(), mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.uuids().valStr(), mockNeat.localDates().valStr(),
                mockNeat.localDates().valStr(), mockNeat.strings().valStr());

        eventService.addEvent(event);

        mvc.perform(get("/events/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("event_name").exists())
                .andExpect(jsonPath("username").exists())
                .andExpect(jsonPath("venue_id").exists())
                .andExpect(jsonPath("description").exists())
                .andExpect(status().isOk());
        eventService.deleteEventById(id);
    }

    @Test
    public void getEventById_withInvalidId_returnsNotFoundStatus() throws Exception {
        String id = UUID.randomUUID().toString();

        mvc.perform(get("/events/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createEvent_withValidContents_returnsGoodStatus() throws Exception {
        EventCreateRequest eventCreateRequest = new EventCreateRequest();
        eventCreateRequest.setEventName(mockNeat.words().valStr());
        eventCreateRequest.setUsername(mockNeat.addresses().valStr());
        eventCreateRequest.setDescription(mockNeat.dicts().toString());
        eventCreateRequest.setVenueId(mockNeat.uuids().valStr());
        eventCreateRequest.setCategory(mockNeat.departments().valStr());
        eventCreateRequest.setStartDate(mockNeat.localDates().valStr());
        eventCreateRequest.setEndDate(mockNeat.localDates().valStr());

        MvcResult result = mvc.perform(post("/events")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(eventCreateRequest)))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("event_name").exists())
                .andExpect(jsonPath("username").exists())
                .andExpect(jsonPath("venue_id").exists())
                .andExpect(jsonPath("description").exists())
                .andExpect(status().isCreated())
                .andReturn();
        eventService.deleteEventById(JsonPath.read(result.getResponse().getContentAsString(), "$.id"));
    }

    @Test
    public void deleteEventById_withValidId_deletesEvent() throws Exception {
        String id = UUID.randomUUID().toString();

        Event event = new Event(id, mockNeat.strings().valStr(), mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.uuids().valStr(), mockNeat.localDates().valStr(),
                mockNeat.localDates().valStr(), mockNeat.strings().valStr());

        eventService.addEvent(event);

        mvc.perform(delete("/events/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        mvc.perform(get("/events/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteEventById_withInvalidId_returnsNotFoundStatus() throws Exception {
        String id = UUID.randomUUID().toString();

        mvc.perform(delete("/events/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateEvent_withValidId_updatesEvent() throws Exception {
        String id = UUID.randomUUID().toString();

        Event event = new Event(id, mockNeat.strings().valStr(), mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.uuids().valStr(), mockNeat.localDates().valStr(),
                mockNeat.localDates().valStr(), mockNeat.strings().valStr());

        eventService.addEvent(event);

        EventResponse updatedEvent = convertEvent(new Event(id, mockNeat.strings().valStr(), mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.uuids().valStr(), mockNeat.localDates().valStr(),
                mockNeat.localDates().valStr(), mockNeat.strings().valStr()));

        ResultActions actions = mvc.perform(post("/events/{id}", id)
                        .content(mapper.writeValueAsString(updatedEvent))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        EventResponse response = mapper.readValue(actions.andReturn().getResponse().getContentAsString(), EventResponse.class);
        Assertions.assertEquals(response.getId(), event.getId());
        Assertions.assertNotEquals(response.getDescription(), event.getDescription());
        Assertions.assertEquals(response.getDescription(), updatedEvent.getDescription());

    }

    @Test
    public void updateEvent_withInvalidId_returnsNotFoundStatus() throws Exception {
        EventResponse updatedEvent = convertEvent(new Event(mockNeat.uuids().valStr(), mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.strings().valStr(), mockNeat.uuids().valStr(),
                mockNeat.localDates().valStr(), mockNeat.localDates().valStr(), mockNeat.strings().valStr()));

        mvc.perform(post("/events/{id}", updatedEvent.getId())
                        .content(mapper.writeValueAsString(updatedEvent))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAvailableEvents_returnsListOfAvailableEvents() throws Exception {

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
}