package com._polar._polar_backend_spring.v1.keywords;


import com._polar._polar_backend_spring.domain.entity.Keywords;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class KeywordsRepositoryTest {
    @Autowired
    private KeywordsRepository keywordsRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void testGetKeywords_returnsMatchingKeywords() {
        // 1) 事前にDBへエンティティを投入
        Keywords k1 = new Keywords();
        k1.setName("Java");
        em.persist(k1);

        Keywords k2 = new Keywords();
        k2.setName("Spring");
        em.persist(k2);

        Keywords k3 = new Keywords();
        k3.setName("Other");
        em.persist(k3);

        // EntityManagerのキャッシュをクリアしてDBと同期
        em.flush();
        em.clear();

        // 2) テスト対象メソッド呼び出し
        List<String> searchNames = Arrays.asList("Java", "Spring");
        List<Keywords> result = keywordsRepository.getKeywords(searchNames);

        // 3) 結果検証
        assertThat(result).hasSize(2);  // Java, Spring が見つかるはず
        assertThat(result)
                .extracting("name")
                .containsExactlyInAnyOrder("Java", "Spring");
    }

    @Test
    void testGetKeywords_whenNoMatchFound_returnsEmptyList() {
        // 1) テストデータを仕込む（空でも良いが明示的に1件だけ入れてみる）
        Keywords k = new Keywords();
        k.setName("Dummy");
        em.persist(k);

        em.flush();
        em.clear();

        // 2) 検索に引っかからないキーワードリスト
        List<Keywords> result = keywordsRepository.getKeywords(List.of("NonExistent"));

        // 3) 空リストになることを検証
        assertThat(result).isEmpty();
    }
}