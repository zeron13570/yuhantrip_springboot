package com.yuhan.yuhantrip_springboot.controller;

import PlaceDB.*;
import com.yuhan.yuhantrip_springboot.service.PlaceService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    // 특정 키워드로 여러 지역의 데이터를 가져오는 API
    @GetMapping("/fetch-places-for-all-regions")
    public String fetchPlacesForAllRegions(@RequestParam String query,
                                           @RequestParam String category,
                                           @RequestParam String querykey,
                                           @RequestParam Map<String, String> queryStates,
                                           @RequestParam double minLat, double maxLat, double minLon, double maxLon) {
        placeService.fetchAndSavePlacesForAllRects(query, category, querykey, queryStates, minLat, maxLat, minLon, maxLon);
        return "Places fetched and saved successfully for all regions!";
    }
    @GetMapping("/place")
    public Map<String, Object> getPlaces(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return placeService.getPlaces(filter, page, limit);
    }
    @GetMapping(value = "/place", params = "name") // 'name' 파라미터가 있는 경우
    public Map<String, Object> getPlacesWithName(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        return placeService.getPlacesByCity(name, page, limit);
    }
    @GetMapping("/cafe")
    public Map<String, Object> getCafes(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return placeService.getCafes(filter, page, limit);
    }
    @GetMapping(value = "/cafe", params = "name") // 'name' 파라미터가 있는 경우
    public Map<String, Object> getCafesWithName(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        return placeService.getCafesByCity(name, page, limit);
    }
    @GetMapping("/food")
    public Map<String, Object> getFoods(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return placeService.getFoods(filter, page, limit);
    }
    @GetMapping(value = "/food", params = "name") // 'name' 파라미터가 있는 경우
    public Map<String, Object> getFoodsWithName(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        return placeService.getFoodsByCity(name, page, limit);
    }
    @GetMapping("/lodgment")
    public Map<String, Object> getLodgments(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        return placeService.getLodgments(filter, page, limit);
    }

    @GetMapping("/coordinates")
    public Place getCoordinates(@RequestParam String placeName) {
        return placeService.getPlaceByName(placeName);
    }
}