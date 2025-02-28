package com.mycodingtest.review;

import com.mycodingtest.storage.StorageService;
import org.springframework.stereotype.Service;

@Service
public class MemoService {

    private final StorageService storageService;

    public MemoService(StorageService storageService) {
        this.storageService = storageService;
    }

    public String getMemoUpdateUrl(String reviewId, Long userId) {
        return storageService.getMemoUpdateUrl(reviewId, userId);
    }

    public String getMemoReadUrl(String reviewId, Long userId) {
        return storageService.getMemoReadUrl(reviewId, userId);
    }
}
