//package com.mycodingtest.infra.query;
//
//import com.mycodingtest.domain.query.dto.ReviewSummaryResponse;
//import jakarta.persistence.EntityManager;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//@RequiredArgsConstructor
//public class DashBoardQueryRepository {
//
//    private final EntityManager em;
//
//    /**
//     * @param userId
//     * @param pageable
//     * @return
//     */
//    public Page<ReviewSummaryResponse> findAllProblemWithReviewByUserId(Long userId, Pageable pageable) {
//        String jpql = """
//                SELECT new com.mycodingtest.query.dto.ReviewSummaryResponse(p.id, p.problemNumber, p.problemTitle, r.recentSubmitAt, r.recentResult, r.favorited, r.id, r.difficultyLevel, r.importanceLevel, r.reviewed, r.reviewedAt) \
//                FROM Problem p \
//                INNER JOIN Review r \
//                ON p.id = r.problemId \
//                WHERE r.userId = :userId \
//                """;
//
//        List<ReviewSummaryResponse> content = em.createQuery(jpql, ReviewSummaryResponse.class)
//                .setParameter("userId", userId)
//                .setFirstResult((int) pageable.getOffset())
//                .setMaxResults(pageable.getPageSize())
//                .getResultList();
//
//        Long count = em.createQuery("select count(r) from Review r where r.userId = :userId", Long.class)
//                .setParameter("userId", userId)
//                .getSingleResult();
//
//        return new PageImpl<>(content, pageable, count);
//    }
//
//    public Page<ReviewSummaryResponse> findAllByUserIdAndReviewed(Long userId, boolean reviewed, Pageable pageable) {
//        String jpql = """
//                SELECT new com.mycodingtest.query.dto.ReviewSummaryResponse(p.id, p.problemNumber, p.problemTitle, r.recentSubmitAt, r.recentResult, r.favorited, r.id, r.difficultyLevel, r.importanceLevel, r.reviewed, r.reviewedAt) \
//                FROM Problem p \
//                INNER JOIN Review r \
//                ON p.id = r.problemId \
//                WHERE r.userId = :userId \
//                AND r.reviewed = :reviewed
//                """;
//
//        List<ReviewSummaryResponse> content = em.createQuery(jpql, ReviewSummaryResponse.class)
//                .setParameter("userId", userId)
//                .setParameter("reviewed", reviewed)
//                .setFirstResult((int) pageable.getOffset())
//                .setMaxResults(pageable.getPageSize())
//                .getResultList();
//
//        Long count = em.createQuery("select count(r) from Review r where r.userId = :userId and r.reviewed = :reviewed", Long.class)
//                .setParameter("userId", userId)
//                .setParameter("reviewed", reviewed)
//                .getSingleResult();
//
//        return new PageImpl<>(content, pageable, count);
//    }
//
//    public Page<ReviewSummaryResponse> findAllByUserIdAndFavoriteIsTrue(Long userId, Pageable pageable) {
//        String jpql = """
//                SELECT new com.mycodingtest.query.dto.ReviewSummaryResponse(p.id, p.problemNumber, p.problemTitle, r.recentSubmitAt, r.recentResult, r.favorited, r.id, r.difficultyLevel, r.importanceLevel, r.reviewed, r.reviewedAt) \
//                FROM Problem p \
//                LEFT JOIN Review r \
//                ON r.userId = :userId \
//                AND r.favorited = true
//                """;
//
//        List<ReviewSummaryResponse> content = em.createQuery(jpql, ReviewSummaryResponse.class)
//                .setParameter("userId", userId)
//                .setFirstResult((int) pageable.getOffset())
//                .setMaxResults(pageable.getPageSize())
//                .getResultList();
//
//        Long count = em.createQuery("select count(r) from Review r where r.userId = :userId and r.favorited = true", Long.class)
//                .setParameter("userId", userId)
//                .getSingleResult();
//
//        return new PageImpl<>(content, pageable, count);
//    }
//
//    //tag  보류
////    public Page<ProblemWithReviewResponse> findByAlgorithmTagAndUserId(Long tagId, Long userId, Pageable pageable) {
////        String jpql = """
////                SELECT new com.mycodingtest.query.dto.ReviewSummaryResponse(p.id, p.problemNumber, p.problemTitle, r.recentSubmitAt, r.recentResult, r.favorited, r.id, r.difficultyLevel, r.importanceLevel, r.reviewed, r.reviewedAt) \
////                FROM Problem p \
////                LEFT JOIN p.review r \
////                JOIN p.tags t \
////                WHERE t.tagId = :tagId \
////                AND p.user.id = :userId
////                """;
////        List<ProblemWithReviewResponse> content = em.createQuery(jpql, ProblemWithReviewResponse.class)
////                .setParameter("tagId", tagId)
////                .setParameter("userId", userId)
////                .setFirstResult((int) pageable.getOffset())
////                .setMaxResults(pageable.getPageSize())
////                .getResultList();
////
////        Long count = em.createQuery("select count(sp) from Problem sp join sp.tags t where t.tagId = :tagId and sp.user.id = :userId", Long.class)
////                .setParameter("tagId", tagId)
////                .setParameter("userId", userId)
////                .getSingleResult();
////
////        return new PageImpl<>(content, pageable, count);
////    }
//
////    public List<Long> findDistinctTagIdsByUserId(Long userId) {
////        return em.createQuery("SELECT DISTINCT t.tagId FROM Problem sp JOIN sp.tags t WHERE sp.user.id = :userId", Long.class)
////                .setParameter("userId", userId)
////                .getResultList();
////    }
////
////    public List<Integer> findTagIdsBySolvedProblemId(Long solvedProblemId) {
////        return em.createQuery("SELECT t.tagId FROM Problem sp JOIN sp.tags t WHERE sp.id = :solvedProblemId", Integer.class)
////                .setParameter("solvedProblemId", solvedProblemId)
////                .getResultList();
////    }
//}
