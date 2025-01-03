package com._polar._polar_backend_spring.v1.categories;

import com._polar._polar_backend_spring.domain.entity.Categories;
import com._polar._polar_backend_spring.domain.entity.KeywordCategories;
import com._polar._polar_backend_spring.v1.categories.dto.response.CategoriesDto;
import com._polar._polar_backend_spring.v1.categories.dto.response.CategoryKeywordsDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.NonUniqueResultException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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

    /*
     * フロントのメンター陣リストページでキーワード見える時に使う
     */
    @GetMapping("/{category}/keywords")
    public CategoryKeywordsDto getKeywords(@PathVariable("category") String categoryName) {
        Categories relatedCategoryKeyword = categoriesRepository.getRelatedCategoryKeyword(categoryName);

        List<KeywordCategories> keywordCategories = relatedCategoryKeyword.getKeywordCategories();
        List<String> keywords = relatedCategoryKeyword.getKeywordCategories().stream()
                .map(keywordCategory -> keywordCategory.getKeywords().getName())
                .collect(Collectors.toList());

        return new CategoryKeywordsDto(relatedCategoryKeyword.getName(), keywords);
    }
}
