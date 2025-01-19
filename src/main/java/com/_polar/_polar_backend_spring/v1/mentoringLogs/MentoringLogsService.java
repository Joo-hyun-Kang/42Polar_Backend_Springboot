package com._polar._polar_backend_spring.v1.mentoringLogs;

import com._polar._polar_backend_spring.domain.entity.MentoringLogs;
import com._polar._polar_backend_spring.v1.dto.request.PaginationDto;
import com._polar._polar_backend_spring.v1.mentors.response.MentoringLogsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentoringLogsService {
    final private MentoringLogsRepository mentoringLogsRepository;

    public  List<MentoringLogs> getSimpleLogsPagination(String mentorIntraId, PaginationDto paginationDto) {
        return mentoringLogsRepository.getSimpleLogs(mentorIntraId, paginationDto.getTake(), paginationDto.getPage());
    }

    public List<MentoringLogsDto> getMentoringsLists(String mentorIntraId, PaginationDto paginationDto) {
        List<MentoringLogs> mentoringsLists = mentoringLogsRepository.getMentoringsLists(mentorIntraId, paginationDto.getTake(), paginationDto.getPage());

        return mentoringsLists.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private MentoringLogsDto convertToDto(MentoringLogs log) {
        MentoringLogsDto dto = new MentoringLogsDto();
        dto.setId(log.getId().toString());
        dto.setCreatedAt(log.getCreatedAt());
        dto.setMeetingAt(log.getMeetingAt());

        MentoringLogsDto.CadetDto cadetDto = new MentoringLogsDto.CadetDto();
        if (log.getCadets() != null) {
            cadetDto.setName(log.getCadets().getName());
            cadetDto.setIntraId(log.getCadets().getIntraId());
            cadetDto.setResumeUrl(log.getCadets().getResumeUrl());
            cadetDto.setCommon(log.getCadets().isCommon());
            dto.setCadet(cadetDto);
        }

        MentoringLogsDto.ReportDto reportDto = new MentoringLogsDto.ReportDto();
        if (log.getReports() != null) {
            reportDto.setId(log.getReports().getId().toString());
            reportDto.setStatus(log.getReports().getStatus().getDescription());
        }
        dto.setReport(reportDto);

        MentoringLogsDto.MetaDto metaDto = new MentoringLogsDto.MetaDto();
        metaDto.setRequestTime(Arrays.asList(log.getRequestTime1(), log.getRequestTime2(), log.getRequestTime3()));
        metaDto.setRejectMessage(log.getRejectMessage());
        metaDto.setContent(log.getContent());
        dto.setMeta(metaDto);

        return dto;
    }
}