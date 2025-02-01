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
import com._polar._polar_backend_spring.v1.mentors.dto.common.MentorUpdateDto;
import com._polar._polar_backend_spring.v1.mentors.dto.request.AvailableTimeDto;
import com._polar._polar_backend_spring.v1.mentors.dto.request.JoinMentorDto;
import com._polar._polar_backend_spring.v1.mentors.dto.request.UpdateMentorDto;
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
import java.util.Objects;

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
        String availableTimeToString = null;
        if (body.getIsActive()) {
            //availableTime検証
            validationAvailableTimeThrowEx(body.getAvailableTime(), bindingResult);

            //availableTimeの文字列化とアップデート用DTO生成：Jsonレガーをサービスではなくコントローラーで処理
            availableTimeToString = convertAvailableTimeToStringThrowEx(body.getAvailableTime());
        }

        MentorEnrollDto mentorEnrollDto = new MentorEnrollDto(body.getName(), body.getSlackId(), availableTimeToString, body.getIsActive(), body.getCompany(), body.getDuty());

        boolean isUpdated = mentorsService.enrollMentorInfo(authInfo.getIntraId(), mentorEnrollDto);
        if (!isUpdated) {
            throw new SQLException(GlobalExceptionHandler.CONFLICTEXCEPTION_UPDATE);
        }

        return true;
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

    /*
     * フロントのメンター詳細ページでメンター情報をアップデートするAPI
     * Email、キーワードは別のコントローラーによって登録される→メール認証のため
     */
    @PatchMapping("{intraId}")
    @AuthGuard({ROLES.MENTOR})
    public Boolean updateMentorDetails(@RequestBody @Valid UpdateMentorDto updateMentorDto,
                                       @AuthInfoResolver AuthInfo authInfo,
                                       @PathVariable String intraId, BindingResult bindingResult)
            throws BadRequestException, SQLException, JsonProcessingException {
        if (!Objects.equals(authInfo.getIntraId(), intraId)) {
            throw new BadRequestException(GlobalExceptionHandler.BADREQUESTEXCEPTION);
        }

        String availableTimeToString = null;
        if (updateMentorDto.getAvailableTime() != null) {
            //availableTime検証
            validationAvailableTimeThrowEx(updateMentorDto.getAvailableTime(), bindingResult);

            //availableTimeの文字列化とアップデート用DTO生成：Jsonレガーをサービスではなくコントローラーで処理
            availableTimeToString = convertAvailableTimeToStringThrowEx(updateMentorDto.getAvailableTime());
        }

        MentorUpdateDto mentorUpdateDto = new MentorUpdateDto(
                updateMentorDto.getName(), updateMentorDto.getSlackId(),
                availableTimeToString, updateMentorDto.getIsActive(),
                updateMentorDto.getCompany(), updateMentorDto.getDuty(),
                updateMentorDto.getIntroduction(), updateMentorDto.getTags(),
                updateMentorDto.getMarkdownContent());

        boolean isUpdated = mentorsService.updateMentorDetails(authInfo.getIntraId(), mentorUpdateDto);
        if (!isUpdated) {
            throw new SQLException(GlobalExceptionHandler.CONFLICTEXCEPTION_UPDATE);
        }

        return true;
    }

    private String convertAvailableTimeToStringThrowEx(List<List<AvailableTimeDto>> availableTime) throws JsonProcessingException {
        try {
            return objectMapper.writeValueAsString(availableTime);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private void validationAvailableTimeThrowEx(List<List<AvailableTimeDto>> availableTime, BindingResult bindingResult) throws BadRequestException {
        if (availableTime == null || availableTime.isEmpty()) {
            throw new BadRequestException("メンタリング可能に設定する際には、利用可能な時間を入力する必要があります");
        }

        availableTimesValidator.validate(availableTime, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new CustomValidationException("正しいメンタリング可能時間ではありません。", bindingResult);
        }
    }
}
