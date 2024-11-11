package com.yuhan.yuhantrip_springboot.repository;

import PlaceDB.Lodgment;
import PlaceDB.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LodgmentRepository extends JpaRepository<Lodgment, Long> {
    Optional<Lodgment> findByNameAndAddress(String name, String address);
    Page<Lodgment> findByAddressContaining(String address, Pageable pageable);
    Page<Lodgment> findByAddressStartingWith(String address, Pageable pageable);
}
