package com.yuhan.yuhantrip_springboot.repository;

import PlaceDB.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByCategory(String category);
}
