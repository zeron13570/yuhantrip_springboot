package com.yuhan.yuhantrip_springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@Controller
public class DirectionsController {

    @Value("${kakao.api.key}")  // application.properties에 정의한 키를 읽어옵니다.
    private String KAKAO_API_KEY;

    // 경로 찾기 요청을 처리하는 엔드포인트
    @PostMapping("/findRoute")
    public ResponseEntity<Map<String, Object>> findRoute(@RequestBody Map<String, Object> routeRequest) {

        String apiUrl = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(routeRequest, headers);
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
}