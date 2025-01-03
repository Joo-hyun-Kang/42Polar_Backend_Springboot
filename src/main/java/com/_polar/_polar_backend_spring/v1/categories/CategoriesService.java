package com._polar._polar_backend_spring.v1.categories;

import com._polar._polar_backend_spring.domain.entity.Categories;
import com._polar._polar_backend_spring.domain.entity.KeywordCategories;
import com._polar._polar_backend_spring.v1.categories.dto.response.CategoryKeywordsDto;
import com._polar._polar_backend_spring.v1.categories.dto.response.MentorsListByCategory;
import com._polar._polar_backend_spring.v1.categories.dto.response.MentorsListElement;
import com._polar._polar_backend_spring.v1.keywords.KeywordsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final KeywordsRepository keywordsRepository;

    public CategoryKeywordsDto getKeywords(String categoryName) {
        Categories relatedCategoryKeyword = categoriesRepository.getRelatedCategoryKeyword(categoryName);

        List<KeywordCategories> keywordCategories = relatedCategoryKeyword.getKeywordCategories();
        List<String> keywords = relatedCategoryKeyword.getKeywordCategories().stream()
                .map(keywordCategory -> keywordCategory.getKeywords().getName())
                .collect(Collectors.toList());

        return new CategoryKeywordsDto(relatedCategoryKeyword.getName(), keywords);
    }

    public MentorsListByCategory getMentorListFromCategory(CategoryKeywordsDto categoryKeywordsDto,
                                                           List<String> requestKeywords,
                                                           String mentorNameOrIntraId) {
        List<String> searchKeywords = (requestKeywords != null && !requestKeywords.isEmpty())
                ? requestKeywords
                : categoryKeywordsDto.getKeywords();

        List<MentorsListElement> mentorList = keywordsRepository.getMentorsByKeywords(searchKeywords, mentorNameOrIntraId);

        return new MentorsListByCategory(
                categoryKeywordsDto.getCategory(),
                mentorList.size(),
                mentorList
        );
    }
}
