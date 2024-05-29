package com.kenzie.appserver.controller;

import com.jayway.jsonpath.JsonPath;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.VenueCreateRequest;
import com.kenzie.appserver.controller.model.VenueResponse;
import com.kenzie.appserver.service.VenueService;
import com.kenzie.appserver.service.model.Venue;
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
public class VenueControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    VenueService venueService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getVenues_returnsListOfVenues() throws Exception {
        String id = UUID.randomUUID().toString();

        Venue venue = new Venue(id, mockNeat.strings().valStr(), mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.strings().valStr());

        venueService.createVenue(venue);

        mvc.perform(get("/venues")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        venueService.deleteVenue(id);
    }

    @Test
    public void getVenueById_withValidId_returnsVenue() throws Exception {
        String id = UUID.randomUUID().toString();

        Venue venue = new Venue(id, mockNeat.strings().valStr(), mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.strings().valStr());

        venueService.createVenue(venue);

        mvc.perform(get("/venues/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("event_capacity").exists())
                .andExpect(jsonPath("address").exists())
                .andExpect(jsonPath("description").exists())
                .andExpect(status().isOk());
        venueService.deleteVenue(id);
    }

    @Test
    public void getVenueById_withInvalidId_returnsNotFoundStatus() throws Exception {
        String id = UUID.randomUUID().toString();

        mvc.perform(get("/venues/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addNewVenue_withValidContents_returnsGoodStatus() throws Exception {
        VenueCreateRequest venueCreateRequest = new VenueCreateRequest();
        venueCreateRequest.setName(mockNeat.names().valStr());
        venueCreateRequest.setAddress(mockNeat.addresses().valStr());
        venueCreateRequest.setDescription(mockNeat.dicts().toString());
        venueCreateRequest.setEventCapacity(mockNeat.ints().valStr());
        venueCreateRequest.setStatus("AVAILABLE");

        MvcResult result = mvc.perform(post("/venues")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(venueCreateRequest)))

                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("event_capacity").exists())
                .andExpect(jsonPath("address").exists())
                .andExpect(jsonPath("description").exists())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        venueService.deleteVenue(JsonPath.read(result.getResponse().getContentAsString(), "$.id"));
    }

    @Test
    public void deleteVenue_withValidId_deletesVenue() throws Exception {
        String id = UUID.randomUUID().toString();

        Venue venue = new Venue(id, mockNeat.strings().valStr(), mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.strings().valStr());

        venueService.createVenue(venue);

        mvc.perform(delete("/venues/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        mvc.perform(get("/venues/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteVenue_withInvalidId_returnsNotFoundStatus() throws Exception {
        String id = UUID.randomUUID().toString();

        mvc.perform(delete("/venues/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateVenue_withValidId_updatesVenue() throws Exception {
        String id = UUID.randomUUID().toString();

        Venue venue = new Venue(id, mockNeat.strings().valStr(), mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.strings().valStr());

        venueService.createVenue(venue);

        Venue updatedVenue = new Venue(id, mockNeat.strings().valStr(), mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.strings().valStr(), "AVAILABLE",
                mockNeat.ints().valStr(), mockNeat.domains().valStr(), mockNeat.emails().valStr());

        ResultActions actions = mvc.perform(post("/venues/{id}", id)
                .content(mapper.writeValueAsString(updatedVenue))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        VenueResponse response = mapper.readValue(actions.andReturn().getResponse().getContentAsString(), VenueResponse.class);
        Assertions.assertEquals(response.getId(), venue.getId());
        Assertions.assertNotEquals(response.getDescription(), venue.getDescription());
        Assertions.assertEquals(response.getDescription(), updatedVenue.getDescription());

    }

    @Test
    public void updateVenue_withInvalidId_returnsNotFoundStatus() throws Exception {
        Venue updatedVenue = new Venue(mockNeat.uuids().valStr(), mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.strings().valStr(), mockNeat.strings().valStr(),
                "AVAILABLE", mockNeat.ints().valStr(), mockNeat.domains().valStr(), mockNeat.emails().valStr());

        ResultActions actions = mvc.perform(post("/venues/{id}", updatedVenue.getId())
                        .content(mapper.writeValueAsString(updatedVenue))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAvailableVenues_returnsListOfAvailableVenues() throws Exception {
        String id = UUID.randomUUID().toString();

        Venue venue = new Venue(id, mockNeat.strings().valStr(),
                mockNeat.strings().valStr(), mockNeat.strings().valStr(), mockNeat.strings().valStr(),
                "AVAILABLE", mockNeat.ints().valStr(), mockNeat.domains().valStr(), mockNeat.emails().valStr());

        venueService.createVenue(venue);

        mvc.perform(get("/venues/open")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        venueService.deleteVenue(id);
    }
}
