package com._polar._polar_backend_spring.v1.categories;

import com._polar._polar_backend_spring.domain.entity.Categories;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Slf4j
public class CategoriesController {
    @Autowired
    CategoriesRepository categoriesRepository;
    Logger logger;

    @GetMapping("categories")
    public List<Categories> getCategories() {
        List<Categories> test = categoriesRepository.findAll();
        for (Categories categories : test) {
            System.out.println(categories);
        }


        return test;
    }

    @GetMapping("categories2")
    public String getCategories2() {

        List<Categories> test = categoriesRepository.findAll();
        for (Categories categories : test) {
            System.out.println(categories);
        }

        return "Hello";

    }
}
