package org.example.expert.domain.todo.dto.request;

import java.time.LocalDate;

public record TodoSearchDTO(
        String keyword, //검색(제목)
        LocalDate startDate, //생성일
        LocalDate endDate,
        String managerNickName//담당자닉네임
) { }

