package com.yuhan.yuhantrip_springboot.controller;

import PlaceDB.Place;
import com.yuhan.yuhantrip_springboot.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    // 특정 키워드로 여러 지역의 데이터를 가져오는 API
    @GetMapping("/fetch-places-for-all-regions")
    public String fetchPlacesForAllRegions(@RequestParam String query, @RequestParam int radius) {
        placeService.fetchAndSavePlacesForAllRegions(query, radius);
        return "Places fetched and saved successfully for all regions!";
    }
    @GetMapping("/api/places")
    public List<Place> getAllPlaces() {
        return placeService.getAllPlaces();  // H2 데이터베이스에서 모든 장소 데이터 조회
    }
}