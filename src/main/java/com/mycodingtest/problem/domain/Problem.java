package com.mycodingtest.problem.domain;

import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Problem extends BaseEntity {

    private Integer problemNumber;
    private String problemTitle;
    @Enumerated(EnumType.STRING)
    private Platform platform;

    public static Problem of(Integer problemNumber, String problemTitle, Platform platform) {
        return new Problem(problemNumber, problemTitle, platform);
    }
}
