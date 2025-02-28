package com.mycodingtest.storage;

import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class S3Service {

    private final S3Template s3Template;
    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Value("${aws.s3.urlExpirationSeconds}")
    private int urlExpirationSeconds;

    public S3Service(S3Template s3Template) {
        this.s3Template = s3Template;
    }

    public String getCodeReadUrl(String submissionId, Long userId) {
        String key = userId + "/codes/" + submissionId + ".txt";
        return s3Template.createSignedGetURL(bucketName, key, Duration.ofSeconds(urlExpirationSeconds)).toString();
    }

    public String getCodeUpdateUrl(String submissionId, Long userId) {
        String key = userId + "/codes/" + submissionId + ".txt";
        return s3Template.createSignedPutURL(bucketName, key, Duration.ofSeconds(urlExpirationSeconds)).toString();
    }

    public String getMemoReadUrl(String reviewId, Long userId) {
        String key = userId + "/memos/" + reviewId + ".txt";
        if (s3Template.objectExists(bucketName, key)) {
            return s3Template.createSignedGetURL(bucketName, key, Duration.ofSeconds(urlExpirationSeconds)).toString();
        }
        return "noMemo";
    }

    public String getMemoUpdateUrl(String reviewId, Long userId) {
        String key = userId + "/memos/" + reviewId + ".txt";
        return s3Template.createSignedPutURL(bucketName, key, Duration.ofSeconds(urlExpirationSeconds)).toString();
    }


    public void deleteCodes(List<String> submissionIdList, Long userId) {
        for (String submissionId : submissionIdList) {
            String key = userId + "/codes/" + submissionId + ".txt";
            s3Template.deleteObject(bucketName, key);
        }
    }

    public void deleteMemo(String reviewId, Long userId) {
        String key = userId + "/memos/" + reviewId + ".txt";
        s3Template.deleteObject(bucketName, key);
    }
}
