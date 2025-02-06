package com._polar._polar_backend_spring.v1.keywords;

import com._polar._polar_backend_spring.domain.entity.Keywords;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordsService {
    private final KeywordsRepository keywordsRepository;

    public List<Keywords> getKeywords(List<String> keywords) {
        return keywordsRepository.getKeywords(keywords);
    }
}
