package com.yuhan.yuhantrip_springboot.controller;

import PlaceDB.RouteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @PostMapping("/save")
    public ResponseEntity<String> saveRoute(@RequestBody RouteDto routeDto) {
        // 실제로 DB에 저장하는 로직은 추가해야 함.
        System.out.println("출발지: " + routeDto.getOrigin());
        System.out.println("목적지: " + routeDto.getDestination());
        System.out.println("경유지: " + routeDto.getWaypoints());
        System.out.println("경로 데이터: " + routeDto.getRouteData());

        // 예시로 데이터를 출력하고 "저장 완료" 응답을 보냄
        return ResponseEntity.ok("경로가 성공적으로 저장되었습니다");
    }
}
