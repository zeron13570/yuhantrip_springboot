package com.yuhan.yuhantrip_springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

    @GetMapping("/test")
    public String showMap() {
        return "b";  // templates 디렉토리의 b.html 파일을 반환
    }
}
