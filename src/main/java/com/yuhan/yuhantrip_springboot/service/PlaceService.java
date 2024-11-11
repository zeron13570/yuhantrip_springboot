package com.yuhan.yuhantrip_springboot.service;

import PlaceDB.*;
import PlaceDTO.placeDTO;
import com.yuhan.yuhantrip_springboot.repository.CafeRepository;
import com.yuhan.yuhantrip_springboot.repository.FoodRepository;
import com.yuhan.yuhantrip_springboot.repository.LodgmentRepository;
import com.yuhan.yuhantrip_springboot.repository.PlaceRepository;
import org.springframework.data.domain.Page;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.CommandLineRunner;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlaceService implements CommandLineRunner {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private LodgmentRepository lodgmentRepository;

    private final String API_KEY = "98fd008ffc24d8862055a60d6e18f3e4";
    //private final String API_KEY = "d1533ca76e6ff83395ea27bbf16b5b1a";
    //private final String API_KEY = "6a7a67f3d602d22e880cf4fabf15b6dc";
    private final String STATE_FILE = "state.json";

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Loading resume data...");

        // 상태 파일을 읽어서 현재 상태를 로드합니다.
        Map<String, String> queryStates = loadQueryCompletionStatus();
        System.out.println("Loaded query states: " + queryStates);

        // 데이터 수집 키워드 리스트
        String[] detailedQueries = {"관광지", "박물관", "아이와체험", "문화예술공간", "미술관", "명소", "카페", "음식점", "식당", "숙박", "호텔", "모텔", "놀거리", "전시관"};

        // 서울, 부산, 제주도를 순차적으로 수집
        String[] regions = {"서울", "부산", "제주도", "강릉", "군산", "경주", "인천", "수원", "포항", "울산", "대구", "전주"};

        for (String region : regions) {
            System.out.println("Starting data collection for region: " + region);

            double[] coordinates = getRegionCoordinates(region);
            double minLat = coordinates[0];
            double maxLat = coordinates[1];
            double minLon = coordinates[2];
            double maxLon = coordinates[3];

            for (String query : detailedQueries) {
                // queryKey 생성 시 올바르게 설정
                String queryKey = region + "_" + query;
                System.out.println("Using queryKey: " + queryKey);  // 디버그 로그로 올바르게 생성되는지 확인

                if (queryStates.containsKey(queryKey) && queryStates.get(queryKey).equals("completed")) {
                    System.out.println("Query already completed for " + query + " in region: " + region);
                    continue;
                }

                // 실행시 데이터가져옴, 주석하면 데이터안넣음
                //fetchAndSavePlacesForAllRects(query, determineCategory(query), queryKey, queryStates, minLat, maxLat, minLon, maxLon);

                queryStates.put(queryKey, "completed");
                saveQueryStates(queryStates);
                System.out.println("Query completed: " + query + " in region: " + region + " - State updated and saved.");
            }
        }

        System.out.println("All regions and queries completed.");
    }

    private String determineCategory(String query) {
        if (query.contains("관광지") || query.contains("명소") || query.contains("유적지") || query.contains("박물관") || query.contains("미술관")) {
            return "place";
        } else if (query.contains("카페")) {
            return "cafe";
        } else if (query.contains("음식점") || query.contains("식당")) {
            return "food";
        } else if (query.contains("숙박") || query.contains("호텔") || query.contains("모텔")) {
            return "lodgment";
        } else {
            return "place";  // 기본 카테고리 설정
        }
    }

    private void saveQueryStates(Map<String, String> queryStates) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATE_FILE))) {
            JSONObject json = new JSONObject(queryStates);
            writer.write(json.toString());
            System.out.println("Query states saved: " + json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> loadQueryCompletionStatus() {
        Map<String, String> queryStates = new HashMap<>();
        File file = new File(STATE_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String content = reader.readLine();
                if (content != null) {
                    JSONObject json = new JSONObject(content);
                    for (String key : json.keySet()) {
                        queryStates.put(key, json.getString(key));  // 모든 상태를 String으로 읽어옴
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return queryStates;
    }

    private double[] getRegionCoordinates(String region) {
        switch (region) {
            case "서울":
                return new double[]{37.413294, 37.715133, 126.734086, 127.269311};
            case "부산":
                return new double[]{35.0400, 35.2400, 128.9000, 129.3000};
            case "제주도":
                return new double[]{33.1000, 33.5800, 126.1000, 126.9800};
            case "강릉":
                return new double[]{37.6000, 37.9000, 128.7000, 129.0000};
            case "군산":
                return new double[]{35.8000, 36.1000, 126.6000, 126.9000};
            case "경주":
                return new double[]{35.7000, 35.9000, 129.1000, 129.3000};
            case "인천":
                return new double[]{37.4000, 37.6000, 126.6000, 126.8000};
            case "수원":
                return new double[]{37.2000, 37.4000, 126.9000, 127.1000};
            case "포항":
                return new double[]{35.9000, 36.1000, 129.2000, 129.5000};
            case "울산":
                return new double[]{35.4000, 35.6000, 129.2000, 129.4000};
            case "대구":
                return new double[]{35.7000, 36.0000, 128.5000, 128.7000};
            case "전주":
                return new double[]{35.7000, 36.0000, 127.0000, 127.2000};
            default:
                throw new IllegalArgumentException("Unknown region: " + region);
        }
    }

    public void fetchAndSavePlacesForAllRects(String query, String category, String queryKey, Map<String, String> queryStates, double minLat, double maxLat, double minLon, double maxLon) {
        double latStep = 0.003;
        double lonStep = 0.003;

        boolean resume = queryStates.containsKey(queryKey) && queryStates.get(queryKey).startsWith("resume:");

        for (double lat = minLat; lat < maxLat; lat += latStep) {
            for (double lon = minLon; lon < maxLon; lon += lonStep) {
                String rect = String.format("%f,%f,%f,%f", lon, lat, lon + lonStep, lat + latStep);

                // queryKey와 resume 키를 비교할 때 정확하게 일치하는지 확인
                if (resume && !rect.equals(queryStates.get(queryKey).substring(7))) {
                    continue;
                }

                // 로그에 queryKey와 query를 정확하게 표시하도록 수정
                System.out.println("Fetching rect: " + rect + " for query: " + query + " queryKey: " + queryKey);
                boolean hasData = fetchPlacesByRect(query, rect, category, queryStates, queryKey);

                if (hasData) {
                    queryStates.put(queryKey, "resume:" + rect);
                    saveQueryStates(queryStates);
                    System.out.println("Query states saved for queryKey: " + queryKey);
                }

                try {
                    Thread.sleep(300);  // 스로틀링 방지
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                resume = false;  // resume 상태 초기화
            }
        }

        queryStates.put(queryKey, "completed");
        saveQueryStates(queryStates);
        System.out.println("Query completed: " + queryKey + " - State updated and saved.");
    }

    public boolean fetchPlacesByRect(String query, String rect, String category, Map<String, String> queryStates, String queryKey) {
        String apiKey = "KakaoAK " + API_KEY;
        String urlTemplate = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + query + "&rect=" + rect + "&page=%d&size=15";

        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", apiKey);
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

        int page = 1;
        boolean hasNextPage = true;
        int maxPages = 45;
        boolean hasData = false;

        while (hasNextPage && page <= maxPages) {
            String url = String.format(urlTemplate, page);
            System.out.println("Requesting URL: " + url + " for queryKey: " + queryKey);  // queryKey 로그 추가

            try {
                org.springframework.http.ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);
                String responseBody = response.getBody();

                if (responseBody == null || responseBody.isEmpty()) {
                    System.out.println("No data found for rect: " + rect + " at page: " + page + " for queryKey: " + queryKey);
                    break;
                }

                JSONObject jsonObject = new JSONObject(responseBody);
                JSONArray placesArray = jsonObject.getJSONArray("documents");

                if (placesArray.length() == 0) {
                    System.out.println("Empty data for rect: " + rect + " at page: " + page + " for queryKey: " + queryKey);
                    hasNextPage = false;
                    break;
                }

                hasData = true;

                for (int i = 0; i < placesArray.length(); i++) {
                    JSONObject placeObject = placesArray.getJSONObject(i);
                    String name = placeObject.getString("place_name");
                    String address = placeObject.getString("address_name");
                    double latitude = placeObject.getDouble("y");
                    double longitude = placeObject.getDouble("x");
                    String phone = placeObject.optString("phone", null);

                    savePlaceByCategory(name, address, latitude, longitude, phone, query, category);
                }

                JSONObject meta = jsonObject.getJSONObject("meta");
                boolean isEnd = meta.getBoolean("is_end");
                if (isEnd) {
                    System.out.println("Reached end of pages for rect: " + rect + " at page: " + page + " for queryKey: " + queryKey);
                    hasNextPage = false;
                } else {
                    page++;
                }

            } catch (Exception e) {
                System.out.println("Error occurred while requesting URL: " + url + " for queryKey: " + queryKey);
                e.printStackTrace();
                hasNextPage = false;
            }
        }

        return hasData;
    }


    // 중복 검사 및 데이터 저장
    private void savePlaceByCategory(String name, String address, double latitude, double longitude, String phone, String query, String category) {
        switch (category) {
            case "place":
                if (!placeRepository.findByNameAndAddress(name, address).isPresent()) saveNewPlace(name, address, latitude, longitude, phone, query);
                break;
            case "cafe":
                if (!cafeRepository.findByNameAndAddress(name, address).isPresent()) saveNewCafe(name, address, latitude, longitude, phone, query);
                break;
            case "food":
                if (!foodRepository.findByNameAndAddress(name, address).isPresent()) saveNewFood(name, address, latitude, longitude, phone, query);
                break;
            case "lodgment":
                if (!lodgmentRepository.findByNameAndAddress(name, address).isPresent()) saveNewLodgment(name, address, latitude, longitude, phone, query);
                break;
        }
    }

    private void saveNewPlace(String name, String address, double latitude, double longitude, String phone, String query) {
        Place place = new Place();
        place.setName(name);
        place.setAddress(address);
        place.setLatitude(latitude);
        place.setLongitude(longitude);
        place.setPhone(phone);
        place.setCategory(query);
        placeRepository.save(place);
        System.out.println("Saved place: " + place.getName());
    }

    private void saveNewCafe(String name, String address, double latitude, double longitude, String phone, String query) {
        Cafe cafe = new Cafe();
        cafe.setName(name);
        cafe.setAddress(address);
        cafe.setLatitude(latitude);
        cafe.setLongitude(longitude);
        cafe.setPhone(phone);
        cafe.setCategory(query);
        cafeRepository.save(cafe);
        System.out.println("Saved cafe: " + cafe.getName());
    }

    private void saveNewFood(String name, String address, double latitude, double longitude, String phone, String query) {
        Food food = new Food();
        food.setName(name);
        food.setAddress(address);
        food.setLatitude(latitude);
        food.setLongitude(longitude);
        food.setPhone(phone);
        food.setCategory(query);
        foodRepository.save(food);
        System.out.println("Saved food: " + food.getName());
    }

    private void saveNewLodgment(String name, String address, double latitude, double longitude, String phone, String query) {
        Lodgment lodgment = new Lodgment();
        lodgment.setName(name);
        lodgment.setAddress(address);
        lodgment.setLatitude(latitude);
        lodgment.setLongitude(longitude);
        lodgment.setPhone(phone);
        lodgment.setCategory(query);
        lodgmentRepository.save(lodgment);
        System.out.println("Saved lodgment: " + lodgment.getName());
    }

    // 조회 메서드들
    public Map<String, Object> getPlaces(String filter, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Place> placePage;

        if (filter != null && !filter.isEmpty()) {
            if (filter.equals("대구")) {
                // 대구의 경우 주소가 '대구'로 시작하는 데이터 조회
                placePage = placeRepository.findByAddressStartingWith(filter, pageable);
            } else {
                // 다른 지역의 경우 주소에 필터 문자열이 포함된 데이터 조회
                placePage = placeRepository.findByAddressContaining(filter, pageable);
            }
        } else {
            placePage = placeRepository.findAll(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("places", placePage.getContent());
        response.put("totalCount", placePage.getTotalElements());
        return response;
    }

    public Map<String, Object> getCafes(String filter, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Cafe> cafePage;

        if (filter != null && !filter.isEmpty()) {
            if (filter.equals("대구")) {
                // 대구의 경우 주소가 '대구'로 시작하는 데이터 조회
                cafePage = cafeRepository.findByAddressStartingWith(filter, pageable);
            } else {
                cafePage = cafeRepository.findByAddressContaining(filter, pageable);
            }
        } else {
            cafePage = cafeRepository.findAll(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("cafes", cafePage.getContent());
        response.put("totalCount", cafePage.getTotalElements());
        return response;
    }

    public Map<String, Object> getFoods(String filter, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Food> foodPage;

        if (filter != null && !filter.isEmpty()) {
            if (filter.equals("대구")) {
                // 대구의 경우 주소가 '대구'로 시작하는 데이터 조회
                foodPage = foodRepository.findByAddressStartingWith(filter, pageable);
            } else {
                // 다른 지역의 경우 주소에 필터 문자열이 포함된 데이터 조회
                foodPage = foodRepository.findByAddressContaining(filter, pageable);
            }
        } else {
            foodPage = foodRepository.findAll(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("foods", foodPage.getContent());
        response.put("totalCount", foodPage.getTotalElements());
        return response;
    }

    public Map<String, Object> getLodgments(String filter, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Lodgment> lodgmentPage;

        if (filter != null && !filter.isEmpty()) {
            if (filter.equals("대구")) {
                // 대구의 경우 주소가 '대구'로 시작하는 데이터 조회
                lodgmentPage = lodgmentRepository.findByAddressStartingWith(filter, pageable);
            } else {
                // 다른 지역의 경우 주소에 필터 문자열이 포함된 데이터 조회
                lodgmentPage = lodgmentRepository.findByAddressContaining(filter, pageable);
            }
        } else {
            lodgmentPage = lodgmentRepository.findAll(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("lodgments", lodgmentPage.getContent());
        response.put("totalCount", lodgmentPage.getTotalElements());
        return response;
    }
    // DB + 지도api 경로찾기
    public Place getPlaceByName(String name) {
        return placeRepository.findByName(name);
    }
    public Place findPlaceByName(String name) {
        return placeRepository.findByName(name);
    }

    // 관광지 목록 조회
    public Map<String, Object> getPlacesByCity(String cityName, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Place> placePage;

        if (cityName != null && !cityName.isEmpty()) {
            if (cityName.equals("대구")) {
                // 대구의 경우 주소가 '대구'로 시작하는 데이터 조회
                placePage = placeRepository.findByAddressStartingWith(cityName, pageable);
            } else {
                // 다른 지역의 경우 주소에 필터 문자열이 포함된 데이터 조회
                placePage = placeRepository.findByAddressContaining(cityName, pageable);
            }
        } else {
            placePage = placeRepository.findAll(pageable);
        }

        List<placeDTO> placeDTO = placePage.getContent().stream()
                .map(place -> new placeDTO(place.getName(), place.getAddress()))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("places", placeDTO);
        response.put("totalCount", placePage.getTotalElements());
        return response;
    }
    // 카페 목록 조회 (필터링 및 페이지네이션 적용)
    public Map<String, Object> getCafesByCity(String cityName, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Cafe> cafePage;

        if (cityName != null && !cityName.isEmpty()) {
            if (cityName.equals("대구")) {
                // 대구의 경우 주소가 '대구'로 시작하는 데이터 조회
                cafePage = cafeRepository.findByAddressStartingWith(cityName, pageable);
            } else {
                // 다른 지역의 경우 주소에 필터 문자열이 포함된 데이터 조회
                cafePage = cafeRepository.findByAddressContaining(cityName, pageable);
            }
        } else {
            cafePage = cafeRepository.findAll(pageable);
        }

        List<placeDTO> placeDTO = cafePage.getContent().stream()
                .map(cafe -> new placeDTO(cafe.getName(), cafe.getAddress()))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("places", placeDTO);
        response.put("totalCount", cafePage.getTotalElements());
        return response;
    }
    // 음식점 목록
    public Map<String, Object> getFoodsByCity(String cityName, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Food> foodPage;

        if (cityName != null && !cityName.isEmpty()) {
            if (cityName.equals("대구")) {
                // 대구의 경우 주소가 '대구'로 시작하는 데이터 조회
                foodPage = foodRepository.findByAddressStartingWith(cityName, pageable);
            } else {
                // 다른 지역의 경우 주소에 필터 문자열이 포함된 데이터 조회
                foodPage = foodRepository.findByAddressContaining(cityName, pageable);
            }
        } else {
            foodPage = foodRepository.findAll(pageable);
        }

        List<placeDTO> placeDTO = foodPage.getContent().stream()
                .map(food -> new placeDTO(food.getName(), food.getAddress()))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("places", placeDTO);
        response.put("totalCount", foodPage.getTotalElements());
        return response;
    }
}
