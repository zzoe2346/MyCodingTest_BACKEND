package com.mycodingtest.review;

import com.mycodingtest.storage.S3Service;
import org.springframework.stereotype.Service;

@Service
public class MemoService {

    private final S3Service s3Service;

    public MemoService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public String getMemoUpdateUrl(String reviewId, Long userId) {
        return s3Service.getMemoUpdateUrl(reviewId, userId);
    }

    public String getMemoReadUrl(String reviewId, Long userId) {
        return s3Service.getMemoReadUrl(reviewId, userId);
    }
}
