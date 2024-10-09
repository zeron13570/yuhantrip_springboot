package com.yuhan.yuhantrip_springboot.controller;

import PlaceDB.Place;
import com.yuhan.yuhantrip_springboot.service.PlaceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PathController {

    private final PlaceService placeService;
    @Value("${kakao.api.key}")  // application.properties에서 Kakao API 키를 가져옴
    private String kakaoApiKey;

    public PathController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @PostMapping("/path/find")
    public ResponseEntity<String> findPath(@RequestBody PathRequest pathRequest) {
        // 출발지와 도착지 좌표 가져오기
        PathRequest.Coordinate origin = pathRequest.getOrigin();
        PathRequest.Coordinate destination = pathRequest.getDestination();

        // 좌표 형식: "경도,위도"
        String originCoords = origin.getX() + "," + origin.getY();       // 경도,위도
        String destinationCoords = destination.getX() + "," + destination.getY(); // 경도,위도

        // Kakao Directions API URL 설정
        String url = "https://apis-navi.kakaomobility.com/v1/directions?origin=" + originCoords
                + "&destination=" + destinationCoords;

        // RestTemplate을 사용하여 API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Kakao Directions API 호출 및 결과 반환
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("경로를 찾을 수 없습니다.");
        }
    }

    // DB + 지도api로 경로찾기
    // 좌표를 조회하는 엔드포인트
    @GetMapping("/places/coordinates")
    public ResponseEntity<?> getCoordinates(@RequestParam("placeName") String placeName) {
        // 서비스에서 placeName을 기준으로 좌표 정보를 가져옵니다.
        Place place = placeService.findPlaceByName(placeName);

        if (place != null) {
            Map<String, Double> coordinates = new HashMap<>();
            coordinates.put("latitude", place.getLatitude());
            coordinates.put("longitude", place.getLongitude());
            return ResponseEntity.ok(coordinates);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Place not found");
        }
    }
}
