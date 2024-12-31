package com._polar._polar_backend_spring.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity @Getter
public class Categories {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 50, unique = true)
    private String name;

    @OneToMany(mappedBy = "categories")
    private final List<KeywordCategories> keywordCategories = new ArrayList<>();
}
