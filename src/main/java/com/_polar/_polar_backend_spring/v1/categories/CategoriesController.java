package com._polar._polar_backend_spring.v1.categories;

import com._polar._polar_backend_spring.domain.entity.Categories;
import com._polar._polar_backend_spring.v1.auth.decorators.AuthGuard;
import com._polar._polar_backend_spring.v1.auth.enums.ROLES;
import com._polar._polar_backend_spring.v1.categories.dto.request.MentorKeywordsDto;
import com._polar._polar_backend_spring.v1.categories.dto.response.CategoriesDto;
import com._polar._polar_backend_spring.v1.categories.dto.response.CategoryKeywordsDto;
import com._polar._polar_backend_spring.v1.categories.dto.response.MentorsListByCategory;
import com._polar._polar_backend_spring.v1.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoriesController {
    private final CategoriesService categoriesService;
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
     * メンター詳細ページでメンタが自分のキーワードを追加、削除する時に使う
     */
    @AuthGuard({ROLES.MENTOR})
    @GetMapping("/category/keywords")
    public List<CategoryKeywordsDto> getCategoriesWithKeywords() throws SQLException {
        List<CategoryKeywordsDto> categoryKeywords = categoriesService.getAllCategoryKeyword();
        if (categoryKeywords == null) {
            throw new SQLException(GlobalExceptionHandler.CONFLICTEXCEPTION_SEARCH);
        }

        return categoryKeywords;
    }

    /*
     * フロントのメンター陣リストページでキーワード見える時に使う
     */
    @GetMapping("/{category}/keywords")
    public CategoryKeywordsDto getKeywords(@PathVariable("category") String categoryName) {
        return categoriesService.getKeywords(categoryName);
    }

    /*
     * フロントのメンターリストページでメンターを表示する
     */
    @GetMapping("/{category}")
    public MentorsListByCategory getMentors(@PathVariable("category") String categoryName,
                                            @ModelAttribute MentorKeywordsDto mentorKeywordsDto) {
        CategoryKeywordsDto categoryKeywords = categoriesService.getKeywords(categoryName);

        //キーワードがない場合、EarlyReturn : mentorを取るためにキーワードが必要
        if (categoryKeywords.getKeywords().isEmpty()) {
            return new MentorsListByCategory(categoryKeywords.getCategory(), 0, List.of());
        }

        if (mentorKeywordsDto.getKeywords() != null && mentorKeywordsDto.getKeywords().size() > 0) {
            Set<String> keywordsFromDb = new HashSet<>(categoryKeywords.getKeywords());

            // クエリパラメータのキーワードがすべて含まれているかを検証
            if (!keywordsFromDb.containsAll(mentorKeywordsDto.getKeywords())) {
                throw new IllegalArgumentException("間違いキーワードが含められています");
            }
        }

        return categoriesService.getMentorListFromCategory(
                categoryKeywords,
                mentorKeywordsDto.getKeywords(),
                mentorKeywordsDto.getMentorName()
        );
    }
}
