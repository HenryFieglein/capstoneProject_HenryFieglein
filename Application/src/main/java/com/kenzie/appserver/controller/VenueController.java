package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.VenueCreateRequest;
import com.kenzie.appserver.controller.model.VenueResponse;
import com.kenzie.appserver.service.VenueService;
import com.kenzie.appserver.service.model.Venue;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private final VenueService venueService;

    VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping
    public ResponseEntity<List<VenueResponse>> getVenues(){
        List<Venue> venues = venueService.getAllVenues();

        if(venues == null || venues.isEmpty()) return ResponseEntity.noContent().build();

        List<VenueResponse> responses = venues.stream().map(this::convertVenue).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/open")
    public ResponseEntity<List<VenueResponse>> getAvailableVenues(){
        List<Venue> venues = venueService.getAvailableVenues();

        if(venues == null || venues.isEmpty()) return ResponseEntity.noContent().build();

        List<VenueResponse> responses = venues.stream().map(this::convertVenue).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}")
    public ResponseEntity<VenueResponse> updateVenue(@PathVariable("id") String venueId,
                                                     @RequestBody VenueCreateRequest updateRequest) {
        Venue venue = new Venue(venueId,
                updateRequest.getName(),
                updateRequest.getDescription(),
                updateRequest.getAddress(),
                updateRequest.getEventCapacity(),
                updateRequest.getStatus(),
                updateRequest.getPhone(),
                updateRequest.getWebsite(),
                updateRequest.getEmail());

        VenueResponse response = convertVenue(venueService.updateVenue(venue));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable("id") String id) {

        Venue venue = venueService.getVenueById(id);
        if (venue == null) {
            return ResponseEntity.notFound().build();
        }

        VenueResponse venueResponse = convertVenue(venue);

        return ResponseEntity.ok(venueResponse);
    }

    @PostMapping
    public ResponseEntity<VenueResponse> addNewVenue(@RequestBody VenueCreateRequest venueCreateRequest) {
        Venue venue = new Venue(UUID.randomUUID().toString(),
                venueCreateRequest.getName(),
                venueCreateRequest.getDescription(),
                venueCreateRequest.getAddress(),
                venueCreateRequest.getEventCapacity());
        if (venueCreateRequest.getStatus() != null)
            venue.setStatus(venueCreateRequest.getStatus());
        if (venueCreateRequest.getPhone() != null)
            venue.setPhone(venueCreateRequest.getPhone());
        if (venueCreateRequest.getEmail() != null)
            venue.setEmail(venueCreateRequest.getEmail());
        if (venueCreateRequest.getWebsite() != null)
            venue.setWebsite(venueCreateRequest.getWebsite());

        VenueResponse venueResponse = convertVenue(venueService.createVenue(venue));

        return ResponseEntity.ok(venueResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VenueResponse> deleteVenue(@PathVariable("id") String venueId){
        venueService.deleteVenue(venueId);
        return ResponseEntity.noContent().build();
    }

    private VenueResponse convertVenue(Venue venue) {
        VenueResponse venueResponse = new VenueResponse();
        venueResponse.setId(venue.getId());
        venueResponse.setName(venue.getName());
        venueResponse.setDescription(venue.getDescription());
        venueResponse.setAddress(venue.getAddress());
        venueResponse.setEventCapacity(venue.getEventCapacity());
        venueResponse.setStatus(venue.getStatus());
        venueResponse.setPhone(venue.getPhone());
        venueResponse.setWebsite(venue.getWebsite());
        venueResponse.setEmail(venue.getEmail());
        return venueResponse;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({EmptyResultDataAccessException.class, NoSuchElementException.class, NotFoundException.class})
    public void notFound(){}
}

