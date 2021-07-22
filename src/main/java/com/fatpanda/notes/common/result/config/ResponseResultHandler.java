package com.fatpanda.notes.common.result.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fatpanda.notes.common.result.annotation.ResponseResult;
import com.fatpanda.notes.common.result.entity.Result;
import com.fatpanda.notes.common.result.entity.ResultCode;
import com.fatpanda.notes.common.utils.JsonUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fatPanda
 * @date 2020/10/14
 */
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    public static final String RESPONSE_RESULT_ANN = "RESPONSE-RESULT-ANN";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        ServletRequestAttributes sra = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = sra.getRequest();
        ResponseResult responseResultAnn = (ResponseResult) request.getAttribute(RESPONSE_RESULT_ANN);
        if (responseResultAnn == null) {
            return false;
        } else {
            request.setAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, MediaType.APPLICATION_JSON);
            return true;
        }
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        if (o instanceof Result || o instanceof byte[]) {
            return o;
        } else if (o instanceof String) {
            try {
                return JsonUtil.toJson(Result.OK(o));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return Result.ERROR(ResultCode.SERVER_ERROR);
            }
        }

        return Result.OK(o);
    }
}
