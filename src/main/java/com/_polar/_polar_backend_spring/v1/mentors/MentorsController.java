package com._polar._polar_backend_spring.v1.mentors;

import com._polar._polar_backend_spring.domain.entity.MentoringLogs;
import com._polar._polar_backend_spring.v1.auth.decorators.AuthGuard;
import com._polar._polar_backend_spring.v1.auth.decorators.AuthInfoResolver;
import com._polar._polar_backend_spring.v1.auth.dto.common.AuthInfo;
import com._polar._polar_backend_spring.v1.auth.enums.ROLES;
import com._polar._polar_backend_spring.v1.dto.request.PaginationDto;
import com._polar._polar_backend_spring.v1.mentoringLogs.MentoringLogsService;
import com._polar._polar_backend_spring.v1.mentors.response.MentoringInfoDto;
import com._polar._polar_backend_spring.v1.mentors.response.MentoringLogsDto;
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
     * 私のメンタリングーMentorページにメンターのメンタリングログを見せるAPI
     */
    @AuthGuard({ROLES.MENTOR})
    @GetMapping("/mentorings")
    public MentoringInfoDto getMentoringsLists(@AuthInfoResolver AuthInfo authInfo, @Valid PaginationDto paginationDto) {
        System.out.println(authInfo);

        List<MentoringLogsDto> MentoringLogsDtos = mentoringLogsService.getMentoringsLists(authInfo.getIntraId(), paginationDto);

        return new MentoringInfoDto(MentoringLogsDtos, MentoringLogsDtos.size());
    }


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
