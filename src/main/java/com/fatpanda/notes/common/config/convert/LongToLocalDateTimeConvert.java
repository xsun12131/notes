package com.fatpanda.notes.common.config.convert;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Set;

/**
 * @author xyy
 * @date 2021/7/13
 */
public class LongToLocalDateTimeConvert implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Long.class, LocalDateTime.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        Long obj = (Long) source;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(obj), ZoneId.systemDefault());
    }
}
