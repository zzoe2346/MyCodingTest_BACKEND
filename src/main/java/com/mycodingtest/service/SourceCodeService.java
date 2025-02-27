package com.mycodingtest.service;

import org.springframework.stereotype.Service;

@Service
public class SourceCodeService {

    private final S3Service s3Service;

    public SourceCodeService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public String getCodeUpdateUrl(String submissionId, Long userId) {
        return s3Service.getCodeUpdateUrl(submissionId, userId);
    }

    public String getCodeReadUrl(String submissionId, Long userId) {
        return s3Service.getCodeReadUrl(submissionId, userId);
    }
}
