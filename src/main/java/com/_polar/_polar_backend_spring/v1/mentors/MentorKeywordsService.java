package com._polar._polar_backend_spring.v1.mentors;

import com._polar._polar_backend_spring.domain.entity.MentorKeywords;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentorKeywordsService {
    private final MentorKeywordsRepository mentorKeywordsRepository;

    public List<MentorKeywords> getMentorKeywords(String mentorIntra) {
        return this.mentorKeywordsRepository.findByIntraOrNull(mentorIntra);
    }
}
