package com.fatpanda.notes.common.config.convert;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author xyy
 * @date 2021/7/13
 */
@Configuration
public class EsConvertConfig {

    @Resource
    private ElasticsearchConverter elasticsearchConverter;

    @PostConstruct
    public void addConvert() {
        ConversionService conversionService = elasticsearchConverter.getConversionService();
        if (conversionService instanceof DefaultConversionService) {
            DefaultConversionService defaultConversionService = (DefaultConversionService) conversionService;
            defaultConversionService.addConverter(new LongToLocalDateTimeConvert());
        }
    }

}
