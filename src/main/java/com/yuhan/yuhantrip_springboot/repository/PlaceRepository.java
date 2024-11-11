package com.yuhan.yuhantrip_springboot.repository;

import PlaceDB.Cafe;
import PlaceDB.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByNameAndAddress(String name, String address);
    Place findByName(String name);

    Page<Place> findByAddressContaining(String address, Pageable pageable);
    Page<Place> findByAddressStartingWith(String address, Pageable pageable);
}