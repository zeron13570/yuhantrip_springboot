package com.yuhan.yuhantrip_springboot.controller;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DirectionsController {

    private final String KAKAO_API_KEY = "6f28c4040e6851e1e1f3524c3ee25832";  // 실제 Kakao API 키로 변경

    // HTML 페이지 반환
    @GetMapping("/directions")
    public String showDirectionsPage(Model model) {
        return "b";
    }

    // Kakao API 호출 처리
    @PostMapping("/api/directions")
    @ResponseBody
    public ResponseEntity<?> getDirections(@RequestBody Map<String, Object> requestData) {
        String apiKey = "6f28c4040e6851e1e1f3524c3ee25832";  // 카카오 API 키 설정
        String kakaoApiUrl = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";

        try {
            // 클라이언트에서 받은 데이터 확인
            System.out.println("Received data: " + requestData);

            // 카카오 API에 전달할 데이터 구성
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("origin", requestData.get("origin"));
            requestBody.put("destination", requestData.get("destination"));
            requestBody.put("waypoints", requestData.get("waypoints"));
            requestBody.put("priority", "RECOMMEND");
            requestBody.put("car_fuel", "GASOLINE");
            requestBody.put("car_hipass", false);
            requestBody.put("alternatives", false);
            requestBody.put("road_details", true);

            // 카카오 API 요청을 위한 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "KakaoAK " + apiKey);

            // 카카오 API 요청
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(kakaoApiUrl, HttpMethod.POST, entity, String.class);

            // 성공 시 클라이언트에 응답 반환
            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred while processing directions.");
        }
    }
}