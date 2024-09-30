package com.yuhan.yuhantrip_springboot.controller;

import PlaceDB.Cafe;
import PlaceDB.Food;
import PlaceDB.Lodgment;
import PlaceDB.Place;
import com.yuhan.yuhantrip_springboot.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    // 특정 키워드로 여러 지역의 데이터를 가져오는 API
    @GetMapping("/fetch-places-for-all-regions")
    public String fetchPlacesForAllRegions(@RequestParam String query, @RequestParam int radius) {
        placeService.fetchAndSavePlacesForAllRegions(query, radius);
        return "Places fetched and saved successfully for all regions!";
    }
    @GetMapping("/place")
    public List<Place> getAllPlaces() {
        return placeService.getAllPlaces();
    }
    @GetMapping("/cafe")
    public List<Cafe> getAllCafes() {
        return placeService.getAllCafes();
    }
    @GetMapping("/food")
    public List<Food> getAllFoods() {
        return placeService.getAllFoods();
    }

    @GetMapping("/lodgment")
    public List<Lodgment> getAllLodgments() {
        return placeService.getAllLodgments();
    }
}