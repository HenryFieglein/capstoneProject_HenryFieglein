package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Events")
public class EventRecord {

    private String id;

    private String eventName;

    private String venueId;

    private String description;

    private String username;

    private String startDate;

    private String endDate;

    private String category;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "eventName")
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "startDate")
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @DynamoDBAttribute(attributeName = "endDate")
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @DynamoDBAttribute(attributeName = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBAttribute(attributeName = "venueId")
    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    @DynamoDBAttribute(attributeName = "category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventRecord record = (EventRecord) o;
        return Objects.equals(id, record.id) && Objects.equals(eventName, record.eventName) && Objects.equals(venueId, record.venueId) && Objects.equals(description, record.description) && Objects.equals(username, record.username) && Objects.equals(startDate, record.startDate) && Objects.equals(endDate, record.endDate) && Objects.equals(category, record.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventName, venueId, description, username, startDate, endDate, category);
    }
}
