package com._polar._polar_backend_spring.v1.keywords;

import com._polar._polar_backend_spring.domain.entity.Keywords;
import com._polar._polar_backend_spring.v1.categories.dto.response.MentorsListElement;
import com._polar._polar_backend_spring.v1.categories.dto.response.MentorsListInfo;
import com._polar._polar_backend_spring.v1.keywords.dto.db.MentorsListRow;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class KeywordsRepository {
    @PersistenceContext
    private EntityManager em;

    public List<MentorsListElement> getMentorsByKeywords(List<String> keywords, String requestMentorNameOrIntraId) {
        StringBuilder jpql = new StringBuilder(
                "SELECT DISTINCT new com._polar._polar_backend_spring.v1.keywords.dto.db.MentorsListRow(" +
                        "str(m.id), " + // m.id を文字列に変換
                        "m.name, " +
                        "m.intraId, " +
                        "m.profileImage, " +
                        "m.tags, " +
                        "m.introduction, " +
                        "m.isActive, " +
                        "k.name" +
                        ") " +
                        "FROM Keywords k " +
                        "JOIN k.mentorKeywords mk " +
                        "JOIN mk.mentors m " +
                        "WHERE k.name IN :keywords "
        );



        // オプション条件の追加
        if (requestMentorNameOrIntraId != null && !requestMentorNameOrIntraId.isBlank()) {
            jpql.append("AND (m.name LIKE :mentorNameOrIntraId OR m.intraId LIKE :mentorNameOrIntraId) ");
        }

        jpql.append("ORDER BY m.isActive DESC");

        // TypedQuery の作成
        TypedQuery<MentorsListRow> query = em.createQuery(jpql.toString(), MentorsListRow.class);

        // パラメータ設定
        query.setParameter("keywords", keywords);
        if (requestMentorNameOrIntraId != null && !requestMentorNameOrIntraId.isBlank()) {
            query.setParameter("mentorNameOrIntraId", "%" + requestMentorNameOrIntraId + "%");
        }

        // 結果取得
        List<MentorsListRow> rows = query.getResultList();

        // ===MentorsListElementに変換===
        Map<String, MentorsListElement> mentorKeywordsMap = new HashMap<>();

        // 結果リストをループしてマッピング
        for (MentorsListRow row : rows) {
            // MentorsListInfo は既存のコンストラクタを利用
            MentorsListInfo mentor = new MentorsListInfo(
                    row.getId(),
                    row.getName(),
                    row.getIntraId(),
                    row.getTags(),
                    row.getProfileImage(),
                    row.getIntroduction(),
                    row.getIsActive()
            );

            // キーワードの取得
            String keyword = row.getKeyword();

            //既に、メンター情報が生成しているか確認する
            String mentorId = mentor.getId();
            if (mentorKeywordsMap.containsKey(mentorId)) {
                // 既存の場合、キーワードを追加
                mentorKeywordsMap.get(mentorId).getKeywords().add(keyword);
            } else {
                // 新規の場合、新しいエントリを作成
                List<String> MentorsListElementKeywords = new ArrayList<>();
                MentorsListElementKeywords.add(keyword);

                mentorKeywordsMap.put(mentorId, new MentorsListElement(mentor, MentorsListElementKeywords));
            }
        }

        List<MentorsListElement> result = new ArrayList<>();
        for (Map.Entry<String, MentorsListElement> entry : mentorKeywordsMap.entrySet()) {
            result.add(entry.getValue());
        }

        return result;
    }

    public List<Keywords> getKeywords(List<String> keywords) {
        String jpql = "SELECT k FROM Keywords k WHERE k.name IN :keywords";

        return em.createQuery(jpql, Keywords.class)
                .setParameter("keywords", keywords)
                .getResultList();
    }
}
