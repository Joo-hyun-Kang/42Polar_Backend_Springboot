package com._polar._polar_backend_spring.v1.mentors;

import com._polar._polar_backend_spring.domain.entity.Keywords;
import com._polar._polar_backend_spring.domain.entity.Mentors;
import com._polar._polar_backend_spring.v1.keywords.KeywordsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorsServiceTest {
    @Mock
    private MentorsRepository mentorsRepository;

    @Mock
    private MentorKeywordsService mentorKeywordsService;

    @Mock
    private KeywordsService keywordsService;

    @InjectMocks
    private MentorsService mentorsService;

    @BeforeEach
    void setUp() {
        // 必要があればここで初期設定などを行う
    }

    @Test
    void updateMentorKeywords_success() {
        // --- 準備 ---
        String intraId = "testIntra";
        List<String> keywordNames = Arrays.asList("Java", "Spring");

        // メンターエンティティを想定
        Mentors mentor = new Mentors(intraId);

        // リポジトリが返す期待値を設定
        when(mentorsRepository.findByIntraOrNull(intraId)).thenReturn(mentor);

        // キーワード取得サービスが返す想定
        Keywords k1 = new Keywords();
        k1.setName("Java");
        Keywords k2 = new Keywords();
        k2.setName("Spring");
        List<Keywords> keywords = Arrays.asList(k1, k2);
        when(keywordsService.getKeywords(keywordNames)).thenReturn(keywords);

        // MentorKeywordsServiceの更新結果
        when(mentorKeywordsService.updateMentorToKeywords(mentor, keywords)).thenReturn(true);

        // --- 実行 ---
        boolean result = mentorsService.updateMentorKeywords(intraId, keywordNames);

        // --- 検証 ---
        assertTrue(result, "更新が成功した場合はtrueが返される");

        // 呼び出し回数などを検証
        verify(mentorsRepository, times(1)).findByIntraOrNull(intraId);
        verify(keywordsService, times(1)).getKeywords(keywordNames);
        verify(mentorKeywordsService, times(1)).updateMentorToKeywords(mentor, keywords);
    }

    @Test
    void updateMentorKeywords_mentorNotFound() {
        // --- 準備 ---
        String intraId = "notFoundIntra";
        List<String> keywordNames = Arrays.asList("Java", "Spring");

        // リポジトリ側がnull(見つからない)を返す想定
        when(mentorsRepository.findByIntraOrNull(intraId)).thenReturn(null);

        // --- 実行 ---
        boolean result = mentorsService.updateMentorKeywords(intraId, keywordNames);

        // --- 検証 ---
        assertFalse(result, "メンターが見つからない場合はfalseが返される");

        // mentorKeywordsServiceやkeywordsServiceは呼ばれないことを確認
        verify(keywordsService, never()).getKeywords(anyList());
        verify(mentorKeywordsService, never()).updateMentorToKeywords(any(Mentors.class), anyList());
    }

    @Test
    void updateMentorKeywords_nullKeywords() {
        // --- 準備 ---
        String intraId = "testIntra";
        Mentors mentor = new Mentors(intraId);

        // findByIntraOrNullが返す想定
        when(mentorsRepository.findByIntraOrNull(intraId)).thenReturn(mentor);

        // この場合 selectedKeywordNames が null
        // すると service内で keywords = null のまま
        // → mentorKeywordsService.updateMentorToKeywords(mentorOrNull, null) が呼ばれる

        // mentorKeywordsServiceの動作
        when(mentorKeywordsService.updateMentorToKeywords(mentor, null)).thenReturn(true);

        // --- 実行 ---
        boolean result = mentorsService.updateMentorKeywords(intraId, null);

        // --- 検証 ---
        assertTrue(result, "キーワードリストがnullでもエラーが発生せずtrueが返る想定");

        // keywordsServiceは呼ばれない
        verify(keywordsService, never()).getKeywords(anyList());

        // mentorKeywordsServiceは呼ばれる
        verify(mentorKeywordsService, times(1)).updateMentorToKeywords(mentor, null);
    }
}
