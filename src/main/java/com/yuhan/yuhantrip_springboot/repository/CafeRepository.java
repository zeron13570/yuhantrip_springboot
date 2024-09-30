package com.yuhan.yuhantrip_springboot.repository;

import PlaceDB.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {
    List<Cafe> findAll();  // 모든 카페 목록을 조회하는 메서드
}
