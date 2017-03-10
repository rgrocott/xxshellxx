package com.xxshellxx.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ZonedDateTimePersistenceConverter implements AttributeConverter<ZonedDateTime, java.sql.Timestamp> {

    @Override
    public java.sql.Timestamp convertToDatabaseColumn(ZonedDateTime entityValue) {
        if (entityValue != null) {
            return java.sql.Timestamp.valueOf(entityValue.toLocalDateTime());
        }
        return null;
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(java.sql.Timestamp databaseValue) {
        if (databaseValue != null) {
            return ZonedDateTime.of(databaseValue.toLocalDateTime(), ZoneId.systemDefault());
        }
        return null;
    }
}