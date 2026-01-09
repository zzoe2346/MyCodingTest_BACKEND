package com.mycodingtest.problem.domain;

import com.mycodingtest.common.entity.BaseEntity;
import com.mycodingtest.common.exception.InvalidOwnershipException;
import com.mycodingtest.review.Review;
import com.mycodingtest.solvedproblemtag.SolvedProblemTag;
import com.mycodingtest.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
