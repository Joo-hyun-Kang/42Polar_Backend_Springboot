package com._polar._polar_backend_spring.v1.mentors;

import com._polar._polar_backend_spring.domain.entity.MentoringLogs;
import com._polar._polar_backend_spring.v1.dto.request.PaginationDto;
import com._polar._polar_backend_spring.v1.mentoringLogs.MentoringLogsService;
import com._polar._polar_backend_spring.v1.mentors.response.SimpleLogDto;
import com._polar._polar_backend_spring.v1.mentors.response.SimpleMentoringInfoDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/mentors")
@Slf4j
public class MentorsController {
    private final MentoringLogsService mentoringLogsService;

    /*
     * フロントのメンター詳細ページでメンターに行われたメンタリングリストに使用
     */
    @GetMapping("/simplelogs/{intraId}")
    public SimpleMentoringInfoDto getSimpleLogs(@PathVariable String intraId, @Valid PaginationDto paginationDto) {
        List<MentoringLogs> simpleLogsPagination = mentoringLogsService.getSimpleLogsPagination(intraId, paginationDto);

        List<SimpleLogDto> simpleLogDtoList = new ArrayList<>();
        for (MentoringLogs mentoringLog : simpleLogsPagination) {
            simpleLogDtoList.add(
                    new SimpleLogDto(
                            mentoringLog.getId().toString(),
                            mentoringLog.getMeetingAt(),
                            mentoringLog.getMeetingStart(),
                            mentoringLog.getTopic(),
                            mentoringLog.getStatus().getDescription(),
                            mentoringLog.getCreatedAt()
                    )
            );
        }

        return new SimpleMentoringInfoDto(simpleLogDtoList, simpleLogDtoList.size());
    }
}
