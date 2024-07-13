package com.yuhan.yuhantrip_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.yuhan.yuhantrip_springboot.domain.Travlog;

import java.util.List;

@Repository
public interface TravlogRepository extends JpaRepository<Travlog, Long> {

    List<Travlog> findByAuthor(String author);
}