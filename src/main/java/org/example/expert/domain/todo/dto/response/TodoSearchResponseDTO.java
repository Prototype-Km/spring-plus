package org.example.expert.domain.todo.dto.response;

public record TodoSearchResponseDTO(
        Long todoId,
        String title,
        Long managerCount,
        Long commentCount
) { }
