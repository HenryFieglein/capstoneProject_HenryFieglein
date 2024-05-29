package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.GuestDao;
import com.kenzie.capstone.service.model.Guest;
import com.kenzie.capstone.service.model.GuestData;
import com.kenzie.capstone.service.model.GuestRecord;
import com.kenzie.capstone.service.model.GuestRequest;

import javax.inject.Inject;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LambdaService {

    private GuestDao guestDao;

    @Inject
    public LambdaService(GuestDao guestDao) {
        this.guestDao = guestDao;
    }

    public Guest getGuestData(String id) {
        return recordToGuest(guestDao.getGuestData(id));
    }

    public GuestRecord setGuestData(GuestRequest request) {
        GuestRecord record = requestToRecord(request);
        record.setId(UUID.randomUUID().toString());
        return guestDao.storeGuestData(record);
    }

    public boolean deleteGuestData(String id) {
        GuestRecord record = new GuestRecord();
        record.setId(id);
        return guestDao.deleteGuestData(record);
    }

    public List<Guest> getGuestsByEventId(String id) {
        return guestDao.getGuestsByEventId(id)
                .stream().map(this::recordToGuest)
                .collect(Collectors.toList());
    }

    private GuestRecord requestToRecord(GuestRequest request) {
        GuestRecord record = new GuestRecord();
        record.setName(request.getName());
        record.setEventId(request.getEventId());
        return record;
    }

    private Guest recordToGuest(GuestRecord record) {
        Guest guest = new Guest();
        guest.setId(record.getId());
        guest.setName(record.getName());
        guest.setEventId(record.getEventId());
        return guest;
    }
}
