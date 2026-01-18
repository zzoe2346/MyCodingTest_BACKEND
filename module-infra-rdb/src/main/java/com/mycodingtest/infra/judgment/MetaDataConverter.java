package com.mycodingtest.infra.judgment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycodingtest.domain.judgment.BojMetaData;
import com.mycodingtest.domain.judgment.MetaData;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class MetaDataConverter implements AttributeConverter<MetaData, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(MetaData meta) {
        try {
            return mapper.writeValueAsString(meta);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public MetaData convertToEntityAttribute(String dbData) {
        try {
            if (dbData.contains("baekjoonId")) return mapper.readValue(dbData, BojMetaData.class);
            // if (dbData.contains("PROG")) return mapper.readValue(dbData, PgsMetaData.class);
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}

