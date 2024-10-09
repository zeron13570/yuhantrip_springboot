package com.yuhan.yuhantrip_springboot.controller;

import java.util.List;

public class PathRequest {
    private Coordinate origin;          // 출발지 좌표
    private Coordinate destination;     // 도착지 좌표
    private List<Coordinate> waypoints; // 경유지 리스트

    public Coordinate getOrigin() {
        return origin;
    }

    public void setOrigin(Coordinate origin) {
        this.origin = origin;
    }

    public Coordinate getDestination() {
        return destination;
    }

    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }

    public List<Coordinate> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Coordinate> waypoints) {
        this.waypoints = waypoints;
    }

    // Coordinate 클래스 내부 정의
    public static class Coordinate {
        private double x;  // 경도 (longitude)
        private double y;  // 위도 (latitude)
        private String name;  // 장소 이름

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
