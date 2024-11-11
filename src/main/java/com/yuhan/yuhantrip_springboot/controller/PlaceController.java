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

    private String addProvincePrefix(String cityName) {
        // 지역명 앞에 도/광역시 정보를 추가
        switch (cityName) {
            case "수원":
                return "경기 " + cityName;
            default:
                return cityName;
        }
    }

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
        String fullFilter = (filter != null) ? addProvincePrefix(filter.trim()) : null;

        return placeService.getPlaces(fullFilter, page, limit);
    }
    @GetMapping(value = "/place", params = "name") // 'name' 파라미터가 있는 경우
    public Map<String, Object> getPlacesWithName(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        String fullName = addProvincePrefix(name);

        return placeService.getPlacesByCity(fullName, page, limit);
    }
    @GetMapping("/cafe")
    public Map<String, Object> getCafes(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        String fullFilter = (filter != null) ? addProvincePrefix(filter.trim()) : null;

        return placeService.getCafes(fullFilter, page, limit);
    }
    @GetMapping(value = "/cafe", params = "name") // 'name' 파라미터가 있는 경우
    public Map<String, Object> getCafesWithName(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        String fullName = addProvincePrefix(name);

        return placeService.getCafesByCity(fullName, page, limit);
    }
    @GetMapping("/food")
    public Map<String, Object> getFoods(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        String fullFilter = (filter != null) ? addProvincePrefix(filter.trim()) : null;

        return placeService.getFoods(fullFilter, page, limit);
    }
    @GetMapping(value = "/food", params = "name") // 'name' 파라미터가 있는 경우
    public Map<String, Object> getFoodsWithName(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        String fullName = addProvincePrefix(name);

        return placeService.getFoodsByCity(fullName, page, limit);
    }
    @GetMapping("/lodgment")
    public Map<String, Object> getLodgments(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        String fullFilter = (filter != null) ? addProvincePrefix(filter) : null;

        return placeService.getLodgments(fullFilter, page, limit);
    }

    @GetMapping("/coordinates")
    public Place getCoordinates(@RequestParam String placeName) {
        return placeService.getPlaceByName(placeName);
    }
}