package com.mycodingtest.problem.domain;

import com.mycodingtest.common.entity.BaseEntity;
import com.mycodingtest.review.domain.Review;
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

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Review review;

}
