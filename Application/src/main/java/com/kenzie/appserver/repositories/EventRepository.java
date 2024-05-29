package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.EventRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface EventRepository extends CrudRepository<EventRecord, String> {
    /**
     * Find events by date range.
     * @param date the Date within the range
     * @return a list of events within the given date range
     */

    List<EventRecord> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(String date, String date2);

    /**
     * Find events by venue ID.
     * @param venueId the ID of the venue
     * @return a list of events taking place at the given venue
     */
    List<EventRecord> findByVenueId(String venueId);

    /**
     * Find events by category.
     * @param category the category of the event
     * @return a list of events belonging to the given category
     */
    List<EventRecord> findByCategory(String category);

    /**
     * Find events by name.
     * @param name the name of the event
     * @return a list of events with the given name
     */
    List<EventRecord> findByEventNameContaining(String name);

    List<EventRecord> findByDescriptionContaining(String description);

    List<EventRecord> findByUsername(String username);
}
