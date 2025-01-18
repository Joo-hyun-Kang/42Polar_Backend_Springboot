package com._polar._polar_backend_spring.v1.mentoringLogs;

import com._polar._polar_backend_spring.domain.entity.MentoringLogs;
import com._polar._polar_backend_spring.v1.dto.request.PaginationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentoringLogsService {
    final private MentoringLogsRepository mentoringLogsRepository;

    public  List<MentoringLogs> getSimpleLogsPagination(String mentorIntraId, PaginationDto paginationDto) {
        return mentoringLogsRepository.getSimpleLogs(mentorIntraId, paginationDto.getTake(), paginationDto.getPage());
    }
}