package com.yuhan.yuhantrip_springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "http://localhost:5173")
public class DirectionsController {

    @Value("${kakao.api.key}")  // application.properties에 정의한 키를 읽어옵니다.
    private String KAKAO_API_KEY;

    // 경로 찾기 요청을 처리하는 엔드포인트
    @PostMapping("/findRoute")
    public ResponseEntity<Map<String, Object>> findRoute(@RequestBody Map<String, Object> routeRequest) {

        List<Map<String, Object>> waypoints = (List<Map<String, Object>>) routeRequest.get("waypoints");

        if (waypoints == null || waypoints.size() < 2) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("error", "최소 2개의 지점이 필요합니다.");
            return ResponseEntity.ok(errorResult);
        }

        // 출발지와 도착지 설정
        Map<String, Object> origin = waypoints.get(0);
        Map<String, Object> destination = waypoints.get(waypoints.size() - 1);
        List<Map<String, Object>> viaPoints = waypoints.subList(1, waypoints.size() - 1);

        // Kakao Directions API에 보낼 요청 데이터 구성
        Map<String, Object> kakaoRequest = new HashMap<>();
        kakaoRequest.put("origin", origin);
        kakaoRequest.put("destination", destination);
        kakaoRequest.put("waypoints", viaPoints);

        String apiUrl = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(kakaoRequest, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response;

        try {
            // Kakao Directions API에 POST 요청 보내기
            response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Map.class);
        } catch (Exception e) {
            // 예외 처리: 오류가 발생할 경우 빈 결과 반환
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("error", "Kakao API 요청 실패: " + e.getMessage());
            return ResponseEntity.ok(errorResult);
        }

        // 요청이 성공했는지 확인
        Map<String, Object> result = new HashMap<>();
        result.put("success", response.getStatusCode() == HttpStatus.OK);
        result.put("route", response.getBody());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/findRoute2")
    public ResponseEntity<Map<String, Object>> findRoute2(@RequestBody Map<String, Object> routeRequest) {
        Map<String, Object> origin = (Map<String, Object>) routeRequest.get("origin");
        Map<String, Object> destination = (Map<String, Object>) routeRequest.get("destination");
        List<Map<String, Object>> waypoints = (List<Map<String, Object>>) routeRequest.get("waypoints");

        if (origin == null || destination == null) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("error", "출발지와 도착지가 필요합니다.");
            return ResponseEntity.ok(errorResult);
        }

        // Kakao Directions API에 보낼 요청 데이터 구성
        Map<String, Object> kakaoRequest = new HashMap<>();
        kakaoRequest.put("origin", origin);
        kakaoRequest.put("destination", destination);
        if (waypoints != null && !waypoints.isEmpty()) {
            kakaoRequest.put("waypoints", waypoints);
        }

        String apiUrl = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(kakaoRequest, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response;

        try {
            // Kakao Directions API에 POST 요청 보내기
            response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Map.class);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("error", "Kakao API 요청 실패: " + e.getMessage());
            return ResponseEntity.ok(errorResult);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", response.getStatusCode() == HttpStatus.OK);
        result.put("route", response.getBody());

        return ResponseEntity.ok(result);
    }

}