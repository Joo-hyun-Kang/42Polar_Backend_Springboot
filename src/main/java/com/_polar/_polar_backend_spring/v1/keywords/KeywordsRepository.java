package com._polar._polar_backend_spring.v1.keywords;

import com._polar._polar_backend_spring.v1.categories.dto.response.MentorsListElement;
import com._polar._polar_backend_spring.v1.categories.dto.response.MentorsListInfo;
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
                "SELECT DISTINCT m.id AS id, " +
                        "m.name AS name, " +
                        "m.intraId AS intraId, " +
                        "m.profileImage AS profileImage, " +
                        "m.tags AS tags, " +
                        "m.introduction AS introduction, " +
                        "m.isActive AS isActive, " +
                        "k.name AS keyword " +
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
        TypedQuery<Object[]> query = em.createQuery(jpql.toString(), Object[].class)
                .setMaxResults(1000); //最大 1000件;

        // パラメータ設定
        query.setParameter("keywords", keywords);
        if (requestMentorNameOrIntraId != null && !requestMentorNameOrIntraId.isBlank()) {
            query.setParameter("mentorNameOrIntraId", "%" + requestMentorNameOrIntraId + "%");
        }

        // 結果取得
        List<Object[]> results = query.getResultList();

        // ===MentorsListElementに変換===
        Map<String, MentorsListElement> mentorKeywordsMap = new HashMap<>();

        // 結果リストをループしてマッピング
        for (Object[] row : results) {
            MentorsListInfo mentor = new MentorsListInfo(
                    (String) row[0], // id
                    (String) row[1], // name
                    (String) row[2], // intraId
                    (String) row[4], // tags
                    (String) row[3], // profileImage
                    (String) row[5], // introduction
                    (Boolean) row[6] // isActive
            );

            // キーワードの取得
            String keyword = (String) row[7];

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
}
