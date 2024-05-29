package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.VenueRepository;
import com.kenzie.appserver.repositories.model.VenueRecord;
import com.kenzie.appserver.service.model.Venue;
import com.kenzie.capstone.service.client.LambdaServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VenueServiceTest {
    private VenueService venueService;

    @Mock
    private VenueRepository venueRepository;

    private Venue venue;

    private VenueRecord record;

    private List<VenueRecord> records;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        venueRepository = mock(VenueRepository.class);
        venueService = new VenueService(venueRepository);
        venue = new Venue("id", "name", "description", "address", "capacity",
                "AVAILABLE", null, null, null);
        record = venueToRecord(venue);
        records = new ArrayList<>();
        records.add(record);
        records.add(record);
    }

    // Test 1
    @Test
    public void getAvailableVenues() {
        // GIVEN
        when(venueRepository.findAll()).thenReturn(records);

        // WHEN
        List<Venue> result = venueService.getAvailableVenues();

        // THEN
        assertEquals(recordToVenue(record), result.get(0));
    }

    // Test 2
    @Test
    public void getAllVenues() {
        // GIVEN
        when(venueRepository.findAll()).thenReturn(records);

        // WHEN
        List<Venue> result = venueService.getAllVenues();

        // THEN
        assertEquals(recordToVenue(record), result.get(0));
    }

    // Test 3
    @Test
    public void CreateVenue() {
        // GIVEN
        when(venueRepository.save(record)).thenReturn(record);

        // WHEN
        Venue result = venueService.createVenue(venue);

        // THEN
        assertEquals(venue, result);
    }

    // Test 4
    @Test
    public void UpdateVenueStatus() {
        // GIVEN
        when(venueRepository.existsById(venue.getId())).thenReturn(true);
        when(venueRepository.save(record)).thenReturn(record);

        // WHEN
        Venue result = venueService.updateVenue(venue);

        // THEN
        assertEquals(venue, result);
    }

    // Test 5
    @Test
    public void ReturnNotFoundWhenUpdatingNonexistentVenue() {
        // GIVEN
        when(venueRepository.existsById(venue.getId())).thenReturn(false);

        // WHEN & THEN
        assertThrows(NotFoundException.class, () -> venueService.updateVenue(venue));
    }

    // Test 6
    @Test
    public void DeleteVenue() {
        // GIVEN
        when(venueRepository.existsById(venue.getId())).thenReturn(true);

        // WHEN
        venueService.deleteVenue(venue.getId());

        // THEN
        verify(venueRepository, times(1)).deleteById(venue.getId());
    }

    // Test 7
    @Test
    public void getVenueNotFound() {
        // GIVEN
        when(venueRepository.findById(anyString())).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(NotFoundException.class, () -> venueService.getVenueById("1"));
    }

    // Test 8
    @Test
    public void GetVenue() {
        // GIVEN
        when(venueRepository.findById(venue.getId())).thenReturn(Optional.of(record));

        // WHEN
        Venue result = venueService.getVenueById(venue.getId());

        // THEN
        assertEquals(venue, result);
    }

    private VenueRecord venueToRecord(Venue venue){
        VenueRecord record = new VenueRecord();
        record.setId(venue.getId());
        record.setName(venue.getName());
        record.setDescription(venue.getDescription());
        record.setAddress(venue.getAddress());
        record.setEventCapacity(venue.getEventCapacity());
        record.setStatus(venue.getStatus());
        record.setPhone(venue.getPhone());
        record.setWebsite(venue.getWebsite());
        record.setEmail(venue.getEmail());
        return record;
    }

    private Venue recordToVenue(VenueRecord record){
        return new Venue(record.getId(),
                record.getName(),
                record.getDescription(),
                record.getAddress(),
                record.getEventCapacity(),
                record.getStatus(),
                record.getPhone(),
                record.getWebsite(),
                record.getEmail());
    }
}