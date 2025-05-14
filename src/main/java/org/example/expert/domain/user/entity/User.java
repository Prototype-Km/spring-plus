package org.example.expert.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.enums.UserRole;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String userNickName;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String email, String password,String userNickName, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userNickName = userNickName;
        this.userRole = userRole;
    }

    private User(Long id, String email,String userNickName ,UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userNickName = userNickName;
        this.userRole = userRole;
    }

    //엔티티로 변환
    public static User of(String email,String password, String userNickName,UserRole userRole){
        return new User(email, password, userNickName, userRole);
    }

    //토큰교환 ?
    public static User fromAuthUser(AuthUser authUser) {
        return new User(authUser.getId(), authUser.getEmail(),authUser.getUserNickName(), authUser.getUserRole());
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
