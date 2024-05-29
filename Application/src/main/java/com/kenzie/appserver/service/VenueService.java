package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.VenueRepository;
import com.kenzie.appserver.repositories.model.VenueRecord;
import com.kenzie.appserver.service.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    @Autowired
    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public Venue createVenue(Venue venue) {
        venueRepository.save(venueToRecord(venue));
        return venue;
    }

    public Venue updateVenue(Venue venue) {
        if (venue == null) {
            throw new NullPointerException("Venue cannot be null!");
        }
        if (venueRepository.existsById(venue.getId())) {
            venueRepository.save(venueToRecord(venue));
            return venue;
        } else {
            throw new NotFoundException("Venue not found!");
        }
    }

    public void deleteVenue(String id) {
        venueRepository.deleteById(id);
    }

    public Venue getVenueById(String id) {
        if(id == null)
            throw new NullPointerException("id cannot be null");
        Optional<VenueRecord> recordOptional = venueRepository.findById(id);
        if (recordOptional.isEmpty()) {
            throw new NotFoundException("Venue not found!");
        }
        return recordToVenue(recordOptional.get());
    }

    public List<Venue> getAllVenues() {
        Iterable<VenueRecord> records = venueRepository.findAll();

        List<Venue> venues = new ArrayList<>();
        for (VenueRecord record : records) {
            venues.add(recordToVenue(record));
        }

        if (venues.isEmpty())
            throw new NotFoundException("No venues found!");
        return venues;
    }

    public List<Venue> getAvailableVenues() {
        Iterable<VenueRecord> records = venueRepository.findAll();

        List<Venue> venues = new ArrayList<>();
        for (VenueRecord record : records) {
            if (record.getStatus().equals("AVAILABLE"))
                venues.add(recordToVenue(record));
        }

        if (venues.isEmpty())
            throw new NotFoundException("No available venues found!");
        return venues;
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