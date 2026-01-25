package com.mycodingtest.infra.judgment;

import com.mycodingtest.domain.judgment.BojMetaData;
import com.mycodingtest.domain.judgment.MetaData;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

@Converter
public class MetaDataConverter implements AttributeConverter<MetaData, String> {

    private static final String DELIMITER = "\\|";
    private static final String JOIN_DELIMITER = "|";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public String convertToDatabaseColumn(MetaData meta) {
        if (meta == null) return null;

        if (meta instanceof BojMetaData boj) {
            return new StringJoiner(JOIN_DELIMITER)
                    .add("BOJ")
                    .add(String.valueOf(boj.getSubmissionId()))
                    .add(boj.getBaekjoonId())
                    .add(boj.getResultText())
                    .add(String.valueOf(boj.getMemory()))
                    .add(String.valueOf(boj.getTime()))
                    .add(boj.getLanguage())
                    .add(String.valueOf(boj.getCodeLength()))
                    .add(boj.getSubmittedAt() != null ? boj.getSubmittedAt().format(FORMATTER) : "null")
                    .toString();
        }
        return null;
    }

    @Override
    public MetaData convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return null;

        String[] parts = dbData.split(DELIMITER);
        String type = parts[0];

        if ("BOJ" .equals(type)) {
            return BojMetaData.builder()
                    .submissionId(parseLong(parts[1]))
                    .baekjoonId(parts[2])
                    .resultText(parts[3])
                    .memory(parseInt(parts[4]))
                    .time(parseInt(parts[5]))
                    .language(parts[6])
                    .codeLength(parseInt(parts[7]))
                    .submittedAt(parseDateTime(parts[8]))
                    .build();
        }
        return null;
    }

    private Long parseLong(String s) {
        return "null" .equals(s) ? null : Long.parseLong(s);
    }

    private int parseInt(String s) {
        return Integer.parseInt(s);
    }

    private LocalDateTime parseDateTime(String s) {
        return "null" .equals(s) ? null : LocalDateTime.parse(s, FORMATTER);
    }

}

