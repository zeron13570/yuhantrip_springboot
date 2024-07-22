package com.yuhan.yuhantrip_springboot.controller;

import com.yuhan.yuhantrip_springboot.domain.Travlog;
import com.yuhan.yuhantrip_springboot.service.TravlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

    @RestController
    @RequestMapping("/api/travlogs")
    public class TravlogController {

        private final TravlogService travlogService;

        @Autowired
        public TravlogController(TravlogService travlogService) {
            this.travlogService = travlogService;
        }

        @PostMapping
        public ResponseEntity<Travlog> createTravlog(@RequestParam("author") String author,
                                                     @RequestParam("photo") MultipartFile photo) throws IOException {
            Travlog travlog = new Travlog(author, photo.getBytes());
            Travlog savedTravlog = travlogService.saveTravlog(travlog);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTravlog);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Travlog> getTravlog(@PathVariable Long id) {
            return travlogService.getTravlogById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping
        public ResponseEntity<List<Travlog>> getAllTravlogs() {
            List<Travlog> travlogs = travlogService.getAllTravlogs();
            return ResponseEntity.ok(travlogs);
        }

        @GetMapping("/author/{author}")
        public ResponseEntity<List<Travlog>> getTravlogsByAuthor(@PathVariable String author) {
            List<Travlog> travlogs = travlogService.getTravlogsByAuthor(author);
            return ResponseEntity.ok(travlogs);
        }

        @GetMapping("/{id}/photo")
        public ResponseEntity<byte[]> getTravlogPhoto(@PathVariable Long id) {
            return travlogService.getTravlogById(id)
                    .map(travlog -> ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG) // 또는 적절한 이미지 타입
                            .body(travlog.getPhoto()))
                    .orElse(ResponseEntity.notFound().build());
        }

        @PutMapping("/{id}")
        public ResponseEntity<Travlog> updateTravlog(@PathVariable Long id,
                                                     @RequestParam(required = false) String author,
                                                     @RequestParam(required = false) MultipartFile photo) throws IOException {
            return travlogService.getTravlogById(id)
                    .map(travlog -> {
                        if (author != null) {
                            travlog.setAuthor(author);
                        }
                        if (photo != null) {
                            try {
                                travlog.setPhoto(photo.getBytes());
                            } catch (IOException e) {
                                throw new RuntimeException("Failed to process photo", e);
                            }
                        }
                        return ResponseEntity.ok(travlogService.updateTravlog(travlog));
                    })
                    .orElse(ResponseEntity.notFound().build());
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteTravlog(@PathVariable Long id) {
            if (travlogService.getTravlogById(id).isPresent()) {
                travlogService.deleteTravlog(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }
