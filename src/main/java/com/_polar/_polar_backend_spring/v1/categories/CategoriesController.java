package com._polar._polar_backend_spring.v1.categories;

import com._polar._polar_backend_spring.domain.entity.Categories;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class CategoriesController {
    private final CategoriesRepository categoriesRepository;

    @GetMapping("categories")
    public List<Categories> getCategories() {
        return categoriesRepository.findAll();
    }
}
