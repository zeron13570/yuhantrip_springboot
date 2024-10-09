package com.yuhan.yuhantrip_springboot.controller;

import java.util.List;

public class RouteRequest {
    private Location origin;
    private Location destination;
    private List<Location> waypoints;

    public Location getOrigin() { return origin; }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public List<Location> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Location> waypoints) {
        this.waypoints = waypoints;
    }

    // Location 클래스 내부 정의
    public static class Location {
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