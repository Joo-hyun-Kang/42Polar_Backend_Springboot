package com._polar._polar_backend_spring.v1.mentors;

import com._polar._polar_backend_spring.domain.entity.Mentors;
import com._polar._polar_backend_spring.v1.mentors.response.MentorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentorsService {
    private final MentorsRepository mentorsRepository;

    public boolean isMentor(String intraId) {
        return mentorsRepository.findByIntraOrNull(intraId) != null;
    }

    public Mentors findByIntraOrNull(String intraId) {
        return mentorsRepository.findByIntraOrNull(intraId);
    }

    @Transactional
    public Mentors createUser(String intraId) {
        Mentors mentors = new Mentors(intraId);
        mentorsRepository.save(mentors);

        return mentors;
    }

    public MentorDto getMentorDetails(String intraId) {
        Mentors mentorOrNull = mentorsRepository.findByIntraOrNull(intraId);
        if (mentorOrNull == null) {
            return null;
        }

        return new MentorDto(
                mentorOrNull.getId().toString(),
                mentorOrNull.getIntraId(),
                mentorOrNull.getSlackId() != null ? mentorOrNull.getSlackId() : "",
                mentorOrNull.getName() != null ? mentorOrNull.getName() : "",
                mentorOrNull.getEmail() != null ? mentorOrNull.getEmail() : "",
                mentorOrNull.getCompany() != null ? mentorOrNull.getCompany() : "",
                mentorOrNull.getDuty() != null ? mentorOrNull.getDuty() : "",
                mentorOrNull.getProfileImage() != null ? mentorOrNull.getProfileImage() : "",
                mentorOrNull.getAvailableTime() != null ? mentorOrNull.getAvailableTime() : "",
                mentorOrNull.getIntroduction() != null ? mentorOrNull.getIntroduction() : "",
                mentorOrNull.getTags(),
                mentorOrNull.isActive(),
                mentorOrNull.getMarkdownContent(),
                mentorOrNull.getCreateAt(),
                mentorOrNull.getUpdateAt()
        );
    }
}
