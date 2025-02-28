package com.mycodingtest.storage;

import java.util.List;

public interface StorageService {

    String getCodeReadUrl(String submissionId, Long userId);

    String getCodeUpdateUrl(String submissionId, Long userId);

    String getMemoReadUrl(String reviewId, Long userId);

    String getMemoUpdateUrl(String reviewId, Long userId);

    void deleteCodes(List<String> submissionIdList, Long userId);

    void deleteMemo(String reviewId, Long userId);
}
