package com.yuhan.yuhantrip_springboot.repository;

import PlaceDB.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findByNameAndAddress(String name, String address);  // 모든 음식점 목록을 조회하는 메서드
}
