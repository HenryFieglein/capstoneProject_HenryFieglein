package com.kenzie.capstone.service.client;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.capstone.service.model.Guest;
import com.kenzie.capstone.service.model.GuestData;
import com.kenzie.capstone.service.model.GuestRequest;

import java.util.List;

public class LambdaServiceClient {

    private static final String GUEST_BY_EVENT_ENDPOINT = "guests/event/{id}";
    private static final String GUEST_BY_ID_ENDPOINT = "guests/{id}";
    private static final String GUEST_ENDPOINT = "guests";

    private ObjectMapper mapper;

    public LambdaServiceClient() {
        this.mapper = new ObjectMapper();
    }

    public GuestData getGuestData(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GUEST_BY_ID_ENDPOINT.replace("{id}", id));
        GuestData guestData;
        try {
            guestData = mapper.readValue(response, GuestData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return guestData;
    }

    public GuestData setGuestData(GuestRequest guestRequest) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String request;
        try {
            request = mapper.writeValueAsString(guestRequest);
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to serialize request: " + e);
        }
        String response = endpointUtility.postEndpoint(GUEST_ENDPOINT, request);
        GuestData guestData;
        try {
            guestData = mapper.readValue(response, GuestData.class);
        } catch (Exception e) {
            throw new ApiGatewayException("Unable to map deserialize JSON: " + e);
        }
        return guestData;
    }

    public boolean deleteGuestData(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.deleteEndpoint(GUEST_BY_ID_ENDPOINT.replace("{id}", id));
        try {
            return mapper.readValue(response, Boolean.class);
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to deserialize response: " + e);
        }
    }

    public List<Guest> getGuestDataByEventId(String id) {
        EndpointUtility endpointUtility = new EndpointUtility();
        String response = endpointUtility.getEndpoint(GUEST_BY_EVENT_ENDPOINT.replace("{id}", id));
        try {
            return mapper.readValue(response, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new ApiGatewayException("Unable to deserialize response: " + e);
        }
    }
}