package com.yuhan.yuhantrip_springboot.repository;

import PlaceDB.Lodgment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LodgmentRepository extends JpaRepository<Lodgment, Long> {
    List<Lodgment> findAll();  // 모든 숙박 목록을 조회하는 메서드
}
