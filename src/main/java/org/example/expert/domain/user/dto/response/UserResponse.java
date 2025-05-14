package org.example.expert.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.expert.domain.user.entity.User;

@Getter
public class UserResponse {

    private final Long id;
    private final String email;
    private final String userNickName;

    @Builder
    public UserResponse(Long id, String email,String userNickName) {
        this.id = id;
        this.email = email;
        this.userNickName = userNickName;
    }

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userNickName(user.getUserNickName())
                .build();
    }


}
