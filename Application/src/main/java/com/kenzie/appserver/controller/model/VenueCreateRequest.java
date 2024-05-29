package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class VenueCreateRequest {
    @NotEmpty
    @JsonProperty("venue_name")
    private String name;

    @NotEmpty
    @JsonProperty("description")
    private String description;

    @NotEmpty
    @JsonProperty("address")
    private String address;

    @NotEmpty
    @JsonProperty("status")
    private String status;

    @NotEmpty
    @JsonProperty("event_capacity")
    private String eventCapacity;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("website")
    private String website;

    @JsonProperty("email")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventCapacity() {
        return eventCapacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEventCapacity(String eventCapacity) {
        this.eventCapacity = eventCapacity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
