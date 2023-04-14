package com.demo.repository;

import com.demo.entity.User;
import com.demo.entity.Venue;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface VenueRepository extends CrudRepository <Venue,Integer>{
    Venue findVenueByVunueID(int venueID);
    @Modifying
    @Transactional
    Iterable<Venue> deleteVenueByVenueID(int venueID);
}
