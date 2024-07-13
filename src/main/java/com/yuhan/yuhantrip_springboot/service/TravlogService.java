package com.yuhan.yuhantrip_springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.yuhan.yuhantrip_springboot.domain.Travlog;
import com.yuhan.yuhantrip_springboot.repository.TravlogRepository;

@Service
public class TravlogService {

    private final TravlogRepository travlogRepository;

    @Autowired
    public TravlogService(TravlogRepository travlogRepository) {
        this.travlogRepository = travlogRepository;
    }

    // Travlog 저장
    public Travlog saveTravlog(Travlog travlog) {
        return travlogRepository.save(travlog);
    }

    // ID로 Travlog 조회
    public Optional<Travlog> getTravlogById(Long id) {
        return travlogRepository.findById(id);
    }

    // 모든 Travlog 조회
    public List<Travlog> getAllTravlogs() {
        return travlogRepository.findAll();
    }

    // 작성자로 Travlog 조회
    public List<Travlog> getTravlogsByAuthor(String author) {
        return travlogRepository.findByAuthor(author);
    }

    // Travlog 업데이트
    public Travlog updateTravlog(Travlog travlog) {
        return travlogRepository.save(travlog);
    }

    // Travlog 삭제
    public void deleteTravlog(Long id) {
        travlogRepository.deleteById(id);
    }
}
