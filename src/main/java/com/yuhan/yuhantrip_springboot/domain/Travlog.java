package com.yuhan.yuhantrip_springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "travlog")
@Setter
@Getter
public class Travlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] photo;


    // 기본 생성자
    public Travlog() {
    }

    // 생성자
    public Travlog(String author, byte[] photo) {
        this.author = author;
        this.photo = photo;
    }

}
