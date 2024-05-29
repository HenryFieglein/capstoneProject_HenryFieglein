package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.GuestDao;
import com.kenzie.capstone.service.model.Guest;
import com.kenzie.capstone.service.model.GuestData;
import com.kenzie.capstone.service.model.GuestRecord;
import com.kenzie.capstone.service.model.GuestRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaServiceTest {

    private GuestDao guestDao;
    private LambdaService lambdaService;

    @BeforeAll
    void setup() {
        this.guestDao = mock(GuestDao.class);
        this.lambdaService = new LambdaService(guestDao);
    }

    @Test
    void setDataTest() {
        ArgumentCaptor<GuestRecord> recordCaptor = ArgumentCaptor.forClass(GuestRecord.class);

        // GIVEN
        GuestRequest guestRequest = new GuestRequest();
        guestRequest.setName("John John");
        guestRequest.setEventId("123aabb");

        // WHEN
        GuestRecord response = this.lambdaService.setGuestData(guestRequest);

        // THEN
        verify(guestDao, times(1)).storeGuestData(recordCaptor.capture());

        GuestRecord capturedData = recordCaptor.getValue();

        assertNotNull(capturedData.getId(), "An ID is generated");
        assertEquals(guestRequest.getName(), capturedData.getName(), "The data is saved");
    }

    @Test
    void getDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "fakeid";
        String name = "somedata";
        String eventId = "fakeevent";
        GuestRecord record = new GuestRecord();
        record.setId(id);
        record.setName(name);
        record.setEventId(eventId);


        when(guestDao.getGuestData(id)).thenReturn(record);

        // WHEN
        Guest response = this.lambdaService.getGuestData(id);

        // THEN
        verify(guestDao, times(1)).getGuestData(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getId(), "The response id should match");
        assertEquals(name, response.getName(), "The response name should match");
        assertEquals(eventId, response.getEventId(), "The response eventId should match");
    }

    @Test
    void getByEventTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "fakeid";
        String name = "somedata";
        String eventId = "fakeevent";
        GuestRecord record = new GuestRecord();
        record.setId(id);
        record.setName(name);
        record.setEventId(eventId);


        when(guestDao.getGuestsByEventId(eventId)).thenReturn(List.of(record));

        // WHEN
        List<Guest> response = this.lambdaService.getGuestsByEventId(eventId);

        // THEN
        verify(guestDao, times(1)).getGuestsByEventId(idCaptor.capture());

        assertEquals(eventId, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.get(0).getId(), "The response id should match");
        assertEquals(name, response.get(0).getName(), "The response name should match");
        assertEquals(eventId, response.get(0).getEventId(), "The response eventId should match");
    }

    @Test
    void deleteDataTest() {
        ArgumentCaptor<GuestRecord> recordCaptor = ArgumentCaptor.forClass(GuestRecord.class);

        // GIVEN
        String id = "fakeid";
        GuestRecord record = new GuestRecord();
        record.setId(id);


        when(guestDao.deleteGuestData(record)).thenReturn(true);

        // WHEN
        boolean response = this.lambdaService.deleteGuestData(id);

        // THEN
        verify(guestDao, times(1)).deleteGuestData(recordCaptor.capture());

        assertEquals(id, recordCaptor.getValue().getId(), "The correct record is used");

        assertTrue(response, "A response is returned");
    }

}