package com.yuhan.yuhantrip_springboot.service;

import PlaceDB.Cafe;
import PlaceDB.Food;
import PlaceDB.Lodgment;
import PlaceDB.Place;
import com.yuhan.yuhantrip_springboot.repository.CafeRepository;
import com.yuhan.yuhantrip_springboot.repository.FoodRepository;
import com.yuhan.yuhantrip_springboot.repository.LodgmentRepository;
import com.yuhan.yuhantrip_springboot.repository.PlaceRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.CommandLineRunner;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final String API_KEY = "6f28c4040e6851e1e1f3524c3ee25832";

    // 지역별 좌표를 저장하는 Map
    private final Map<String, double[]> regionCoordinates = new HashMap<>();

    public PlaceService() {
        // 좌표 데이터 (도시명, [위도, 경도])
        regionCoordinates.put("서울", new double[]{37.5665, 126.9780});
        regionCoordinates.put("부산", new double[]{35.1796, 129.0756});
        regionCoordinates.put("제주", new double[]{33.4996, 126.5312});
        regionCoordinates.put("강릉", new double[]{37.7519, 128.8760});
        regionCoordinates.put("군산", new double[]{35.9672, 126.7360});
        regionCoordinates.put("경주", new double[]{35.8562, 129.2247});
        regionCoordinates.put("인천", new double[]{37.4563, 126.7052});
        regionCoordinates.put("수원", new double[]{37.2636, 127.0286});
        regionCoordinates.put("포항", new double[]{36.0190, 129.3435});
        regionCoordinates.put("울산", new double[]{35.5384, 129.3114});
        regionCoordinates.put("대구", new double[]{35.8722, 128.6025});
        regionCoordinates.put("전주", new double[]{35.8242, 127.1480});
        // 추가로 다른 도시들도 여기에 넣을 수 있습니다.
    }

    //애플리케이션이 시작될 때 자동으로 지역별 데이터 저장
    @Override
    public void run(String... args) throws Exception {
//        fetchAndSavePlacesForAllRegions("숙박", 5000);
    }

    public void fetchAndSavePlacesForAllRegions(String query, int radius) {
        // 지역별로 데이터를 가져오는 루프
        for (Map.Entry<String, double[]> entry : regionCoordinates.entrySet()) {
            String region = entry.getKey();
            double[] coordinates = entry.getValue();
            double x = coordinates[1]; // 경도
            double y = coordinates[0]; // 위도

            System.out.println("Fetching places for region: " + region);
            fetchAndSavePlaces(query, x, y, radius);
        }
    }

    // 카카오 API로 데이터 가져와서 저장하는 메서드
    public void fetchAndSavePlaces(String query, double x, double y, int radius) {
        String apiKey = "KakaoAK " + API_KEY;
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + query + "&x=" + x + "&y=" + y + "&radius=" + radius;

        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", apiKey);
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

        org.springframework.http.ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);
        String responseBody = response.getBody();

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray placesArray = jsonObject.getJSONArray("documents");

        for (int i = 0; i < placesArray.length(); i++) {
            JSONObject placeObject = placesArray.getJSONObject(i);
            Place place = new Place();
            place.setName(placeObject.getString("place_name"));
            place.setAddress(placeObject.getString("address_name"));
            place.setLatitude(placeObject.getDouble("y"));
            place.setLongitude(placeObject.getDouble("x"));
            place.setPhone(placeObject.optString("phone", null));

            // 데이터베이스에 저장
            placeRepository.save(place);
        }
    }
    // 조회
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }
    public List<Cafe> getAllCafes() {
        return cafeRepository.findAll();
    }
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }
    public List<Lodgment> getAllLodgments() {
        return lodgmentRepository.findAll();
    }
}