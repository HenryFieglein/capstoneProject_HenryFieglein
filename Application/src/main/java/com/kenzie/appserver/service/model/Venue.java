package com.kenzie.appserver.service.model;

import java.util.Objects;

public class Venue {
    private String name;
    private String address;
    private String phone;
    private String website;
    private String email;
    private String description;
    private String id;
    private String eventCapacity;
    private String status;

    public Venue(String id, String name, String description, String address, String eventCapacity, String status,
                 String phone, String website, String email) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.eventCapacity = eventCapacity;
        this.status = status;
        this.phone = phone;
        this.website = website;
        this.email = email;
    }

    public Venue(String id, String name, String description, String address, String eventCapacity) {
        this(id, name, description, address, eventCapacity, "CLOSED", null, null, null);
    }

    public Venue() {
        this(null, null, null, null, null);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventCapacity() {
        return eventCapacity;
    }

    public void setEventCapacity(String eventCapacity) {
        this.eventCapacity = eventCapacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", eventCapacity='" + eventCapacity + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return Objects.equals(name, venue.name) && Objects.equals(address, venue.address) && Objects.equals(phone, venue.phone) && Objects.equals(website, venue.website) && Objects.equals(email, venue.email) && Objects.equals(description, venue.description) && Objects.equals(id, venue.id) && Objects.equals(eventCapacity, venue.eventCapacity) && Objects.equals(status, venue.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, phone, website, email, description, id, eventCapacity, status);
    }
}