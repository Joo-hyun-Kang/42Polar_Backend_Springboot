package com._polar._polar_backend_spring.v1.mentors.validator;

import com._polar._polar_backend_spring.v1.mentors.dto.request.AvailableTimeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@Slf4j
public class AvailableTimesValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return AvailableTimeDto.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        List<List<AvailableTimeDto>> availableTimes = null;

        try {
            availableTimes = (List<List<AvailableTimeDto>>) target;
        } catch (ClassCastException e) {
            log.error(e.toString());
            assert false : "AvailableTimesValidatorにはList<List<AvailableTimeDto>>しか、入れません。";
        }

        final int WEEK_DAYS = 7;

        //外部配列の数が一週間かのチェック
        if (availableTimes.size() != WEEK_DAYS) {
            errors.rejectValue(
                    "availableTime",
                    "availableTime.dayLength",
                    "availableTime配列のサイズは７（一周）です。"
            );

            return ;
        }

        //内部配列の時刻型の確認
        boolean isEmptyArray = true;
        for (int dayIndex = 0; dayIndex < availableTimes.size(); dayIndex++) {
            List<AvailableTimeDto> dayTimes = availableTimes.get(dayIndex);
            for (int timeIndex = 0; timeIndex < dayTimes.size(); timeIndex++) {
                isEmptyArray = false;

                AvailableTimeDto time = dayTimes.get(timeIndex);
                if (!isValidTime(time)) {
                    errors.rejectValue(
                            "availableTime[" + dayIndex + "][" + timeIndex + "]",
                            "availableTime.time",
                            "正しくない時間形式です。"
                    );

                    return ;
                }
            }
        }

        //内部配列が空かないか確認
        if (isEmptyArray) {
            errors.rejectValue(
                    "availableTime",
                    "availableTime.day",
                    "指定した予約可能な時間がありません。"
            );

            return ;
        }

        //内部配列の時刻の重複の確認
        for (int dayIndex = 0; dayIndex < availableTimes.size(); dayIndex++) {
            List<AvailableTimeDto> timesOfDay = availableTimes.get(dayIndex);
            int timesLength = timesOfDay.size();
            for (int i = 0; i < timesLength; i++) {
                for (int j = 0; j < timesLength; j++) {
                    if (i == j) {
                        continue;
                    }

                    if (!isOverlap(timesOfDay.get(i), timesOfDay.get(j))) {
                        errors.rejectValue(
                                "availableTime[" + dayIndex + "] : " + i + "と" + j,
                                "availableTime.overlap",
                                "時間帯を重なっております。"
                        );

                        return ;
                    }
                }
            }
        }
    }

    private boolean isValidTime(AvailableTimeDto time) {
        if (!(time.getStartHour() >= 0 && time.getStartHour() < 24) ||
            !(time.getStartMinute() == 0 || time.getStartMinute() == 30) ||
            !(time.getEndHour() >= 0 && time.getEndHour() < 24) ||
            !(time.getEndMinute() == 0 || time.getEndMinute() == 30)) {
            return false;
        }

        if (time.getStartHour() >= time.getEndHour()) {
            return false;
        }

        int endTotalMinute = time.getEndHour() * 60 + time.getEndMinute();
        int startTotalMinute = time.getStartHour() * 60 + time.getStartMinute();

        if ((endTotalMinute - startTotalMinute) < 60) {
            return false;
        }

        return true;
    }

    private boolean isOverlap(AvailableTimeDto time1, AvailableTimeDto time2) {
        if (time1.getStartHour() <= time2.getStartHour() && time1.getEndHour() > time2.getStartHour()) {
            return false;
        }

        if (time1.getEndHour() == time2.getStartHour() && time1.getEndMinute() == 30 && time2.getStartMinute() == 0) {
            return false;
        }
        if (time2.getStartHour() <= time1.getStartHour() && time2.getEndHour() > time1.getStartHour()) {
            return false;
        }

        if (time2.getEndHour() == time1.getStartHour() && time2.getEndMinute() == 30 && time1.getEndMinute() == 0) {
            return false;
        }

        return true;
    }
}
