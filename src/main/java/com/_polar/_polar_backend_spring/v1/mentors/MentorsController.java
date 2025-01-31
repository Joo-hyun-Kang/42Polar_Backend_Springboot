package com._polar._polar_backend_spring.v1.mentors;

import com._polar._polar_backend_spring.domain.entity.MentoringLogs;
import com._polar._polar_backend_spring.v1.auth.decorators.AuthGuard;
import com._polar._polar_backend_spring.v1.auth.decorators.AuthInfoResolver;
import com._polar._polar_backend_spring.v1.auth.dto.common.AuthInfo;
import com._polar._polar_backend_spring.v1.auth.enums.ROLES;
import com._polar._polar_backend_spring.v1.dto.request.PaginationDto;
import com._polar._polar_backend_spring.v1.exception.GlobalExceptionHandler;
import com._polar._polar_backend_spring.v1.exception.exceptions.CustomValidationException;
import com._polar._polar_backend_spring.v1.mentoringLogs.MentoringLogsService;
import com._polar._polar_backend_spring.v1.mentors.dto.common.MentorEnrollDto;
import com._polar._polar_backend_spring.v1.mentors.dto.request.JoinMentorDto;
import com._polar._polar_backend_spring.v1.mentors.dto.response.*;
import com._polar._polar_backend_spring.v1.mentors.validator.AvailableTimesValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/mentors")
@Slf4j
public class MentorsController {
    private final MentoringLogsService mentoringLogsService;
    private final MentorsService mentorsService;
    private final AvailableTimesValidator availableTimesValidator;
    private final ObjectMapper objectMapper;

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

    /*
     * フロントの会員登録ーメンターにサービズの利用前、必須情報登録するAPI
     */
    @PatchMapping("/join")
    @AuthGuard({ROLES.MENTOR})
    public Boolean join(@RequestBody @Valid JoinMentorDto body, @AuthInfoResolver AuthInfo authInfo, BindingResult bindingResult) throws BadRequestException, SQLException, JsonProcessingException {
        //availableTime検証
        if (body.getIsActive()) {
            if (body.getAvailableTime() == null || body.getAvailableTime().isEmpty()) {
                throw new BadRequestException("メンタリング可能に設定する際には、利用可能な時間を入力する必要があります");
            }

            availableTimesValidator.validate(body.getAvailableTime(), bindingResult);

            if (bindingResult.hasErrors()) {
                throw new CustomValidationException("正しいメンタリング可能時間ではありません。", bindingResult);
            }
        }

        //availableTimeの文字列化とアップデート用DTO生成：Jsonレガーをサービスではなくコントローラーで処理
        String availableTimeToString;
        try {
            availableTimeToString = objectMapper.writeValueAsString(body.getAvailableTime());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw e;
        }
        MentorEnrollDto mentorEnrollDto = new MentorEnrollDto(body.getName(), body.getSlackId(), availableTimeToString, body.getIsActive(), body.getCompany(), body.getDuty());

        boolean isUpdated = mentorsService.updateMentorDetails(authInfo.getIntraId(), mentorEnrollDto);
        if (!isUpdated) {
            throw new SQLException(GlobalExceptionHandler.CONFLICTEXCEPTION_UPDATE);
        }

        return isUpdated;
    }

    /*
     * フロントのメンター詳細ページでメンター情報を見せる時に使う
     */
    @GetMapping("{intraId}")
    public MentorDto getMentorDetails(@PathVariable String intraId) throws SQLException {
        MentorDto mentorDetails = this.mentorsService.getMentorDetails(intraId);
        if (mentorDetails == null) {
            throw new SQLException("該当するメンターの情報がDBにありません。");
        }

        return mentorDetails;
    }
}
