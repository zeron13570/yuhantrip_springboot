package com.yuhan.yuhantrip_springboot.repository;

import PlaceDB.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByNameAndAddress(String name, String address);
    Place findByName(String name);
}
