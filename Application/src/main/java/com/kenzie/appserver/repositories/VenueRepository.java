package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.VenueRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface VenueRepository extends CrudRepository<VenueRecord, String> {
}
