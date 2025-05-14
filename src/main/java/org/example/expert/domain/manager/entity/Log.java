package org.example.expert.domain.manager.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;

@Builder
@AllArgsConstructor
@Entity
@Table(name="manager_regist_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String logMessage;

}
