package com.mycodingtest.review;

import com.mycodingtest.storage.StorageService;
import org.springframework.stereotype.Service;

@Service
public class SourceCodeService {

    private final StorageService storageService;

    public SourceCodeService(StorageService storageService) {
        this.storageService = storageService;
    }

    public String getCodeUpdateUrl(String submissionId, Long userId) {
        return storageService.getCodeUpdateUrl(submissionId, userId);
    }

    public String getCodeReadUrl(String submissionId, Long userId) {
        return storageService.getCodeReadUrl(submissionId, userId);
    }
}
