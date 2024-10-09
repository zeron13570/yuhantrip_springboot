package com.yuhan.yuhantrip_springboot.repository;

import PlaceDB.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {
    Optional<Cafe> findByNameAndAddress(String name, String address);
}
