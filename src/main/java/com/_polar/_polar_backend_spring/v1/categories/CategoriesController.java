package com._polar._polar_backend_spring.v1.categories;

import com._polar._polar_backend_spring.domain.entity.Categories;
import com._polar._polar_backend_spring.v1.categories.dto.response.CategoriesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoriesController {
    private final CategoriesRepository categoriesRepository;

    /*
     * フロントのメインページでカテゴリー見せる時に使っている
     */
    @GetMapping()
    public List<CategoriesDto> getCategories() {
        List<Categories> categories = categoriesRepository.findAll();
        List<CategoriesDto> result = new ArrayList<>();
        for (Categories category : categories) {
            CategoriesDto categoriesDto = new CategoriesDto();
            categoriesDto.setName(category.getName());
            result.add(categoriesDto);
        }

        return result;
    }
}
