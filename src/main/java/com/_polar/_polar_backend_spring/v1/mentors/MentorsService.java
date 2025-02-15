package com._polar._polar_backend_spring.v1.mentors;

import com._polar._polar_backend_spring.domain.entity.MentorKeywords;
import com._polar._polar_backend_spring.domain.entity.Mentors;
import com._polar._polar_backend_spring.v1.mentors.dto.common.MentorEnrollDto;
import com._polar._polar_backend_spring.v1.mentors.dto.common.MentorUpdateDto;
import com._polar._polar_backend_spring.v1.mentors.dto.response.MentorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MentorsService {
    private final MentorsRepository mentorsRepository;
    private final MentorKeywordsService mentorKeywordsService;

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

    @Transactional
    public boolean enrollMentorInfo(String intraId, MentorEnrollDto mentorEnrollDto) {
        Mentors mentorOrNull = this.mentorsRepository.findByIntraOrNull(intraId);
        if (mentorOrNull == null) {
            return false;
        }

        mentorOrNull.setName(mentorEnrollDto.getName());
        mentorOrNull.setSlackId(mentorEnrollDto.getSlackId());
        mentorOrNull.setAvailableTime(mentorEnrollDto.getAvailableTime());
        mentorOrNull.setActive(mentorEnrollDto.getIsActive());
        mentorOrNull.setCompany(mentorEnrollDto.getCompany());
        mentorOrNull.setDuty(mentorEnrollDto.getDuty());

        return true;
    }

    @Transactional
    public boolean updateMentorDetails(String intraId, MentorUpdateDto mentorUpdateDto) {
        Mentors mentorOrNull = this.mentorsRepository.findByIntraOrNull(intraId);
        if (mentorOrNull == null) {
            return false;
        }

        //クライアントから更新されたデータがあれば、アップデート
        if (mentorUpdateDto.getIsActive() != null) {
            mentorOrNull.setActive(mentorUpdateDto.getIsActive());
        }

        if (mentorUpdateDto.getAvailableTime() != null) {
            mentorOrNull.setAvailableTime(mentorUpdateDto.getAvailableTime());
        }

        if (mentorUpdateDto.getName() != null) {
            mentorOrNull.setName(mentorUpdateDto.getName());
        }

        if (mentorUpdateDto.getSlackId() != null) {
            mentorOrNull.setSlackId(mentorUpdateDto.getSlackId());
        }

        if (mentorUpdateDto.getCompany() != null) {
            mentorOrNull.setCompany(mentorUpdateDto.getCompany());
        }

        if (mentorUpdateDto.getDuty() != null) {
            mentorOrNull.setDuty(mentorUpdateDto.getDuty());
        }

        if (mentorUpdateDto.getIntroduction() != null) {
            mentorOrNull.setIntroduction(mentorUpdateDto.getIntroduction());
        }

        if (mentorUpdateDto.getTags() != null) {
            mentorOrNull.setTags(mentorUpdateDto.getTags());
        }

        if (mentorUpdateDto.getMarkdownContent() != null) {
            mentorOrNull.setMarkdownContent(mentorUpdateDto.getMarkdownContent());
        }

        return true;
    }

    public List<String> getMentorKeywordNamesOfMentor(String mentorIntra) {
        List<MentorKeywords> mentorKeywords = mentorKeywordsService.getMentorKeywords(mentorIntra);

        return mentorKeywords.stream().map(mentorKeyword -> mentorKeyword.getKeywords().getName()).collect(Collectors.toList());
    }
}
