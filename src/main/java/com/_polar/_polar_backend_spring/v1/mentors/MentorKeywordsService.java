package com._polar._polar_backend_spring.v1.mentors;

import com._polar._polar_backend_spring.domain.entity.Keywords;
import com._polar._polar_backend_spring.domain.entity.MentorKeywords;
import com._polar._polar_backend_spring.domain.entity.Mentors;
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

    @Transactional
    public boolean updateMentorToKeywords(Mentors mentor, List<Keywords> keywords) {
        mentorKeywordsRepository.deleteAllForMentor(mentor.getId().toString());

        if (!keywords.isEmpty()) {
            mentorKeywordsRepository.insertMentorToKeywords(mentor, keywords);
        }

        return true;
    }
}
