package com._polar._polar_backend_spring.v1.mentors;

import com._polar._polar_backend_spring.domain.entity.Mentors;
import com._polar._polar_backend_spring.v1.auth.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentorsService {
    private final MentorsRepository mentorsRepository;

    public boolean isMentor(String intraId) {
        return mentorsRepository.findByIntra(intraId) != null;
    }

    public Mentors findByIntra(String intraId) {
        return mentorsRepository.findByIntra(intraId);
    }

    @Transactional
    public Mentors createUser(String intraId) {
        Mentors mentors = new Mentors(intraId);
        mentorsRepository.save(mentors);

        return mentors;
    }
}
