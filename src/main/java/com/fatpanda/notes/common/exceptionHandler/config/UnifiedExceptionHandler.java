package com.fatpanda.notes.common.exceptionHandler.config;

import com.fatpanda.notes.common.exceptionHandler.entity.BaseException;
import com.fatpanda.notes.common.exceptionHandler.entity.BusinessException;
import com.fatpanda.notes.common.result.entity.Result;
import com.fatpanda.notes.common.result.entity.ResultCode;
import com.fatpanda.notes.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ValidationException;

/**
 * @author fatPanda
 * @date 2020/10/14
 * enable unified Exception Handler
 */
@Component
@ControllerAdvice
@ConditionalOnWebApplication
public class UnifiedExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(UnifiedExceptionHandler.class);


    /**
     * 业务异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseEntity handleBusinessException(BaseException e) {
        log.error("业务异常" + e.getMsg(), e.getMsg());

        return ResponseEntity.status(e.getCode()).body(Result.ERROR(e.getCode(), e.getMsg()));
    }

    /**
     * 自定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public ResponseEntity handleBaseException(BaseException e) {
        log.error("自定义异常" + e.getMsg(), e.getMsg());

        return ResponseEntity.status(e.getCode()).body(Result.ERROR(e.getCode(), e.getMsg()));
    }

    /**
     * Controller上一层相关异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    @ResponseBody
    public ResponseEntity handleServletException(Exception e) {
        log.error("Controller上层相关异常" + e.getMessage(), e);

        return ResponseEntity.status(ResultCode.SERVER_ERROR.getCode()).body(Result.ERROR(ResultCode.SERVER_ERROR));
    }


    /**
     * 参数绑定异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ResponseEntity handleBindException(BindException e) {
        log.error("参数绑定校验异常", e.getMessage());

        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 参数校验异常，将校验失败的所有异常组合成一条错误信息
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity handleValidException(MethodArgumentNotValidException e) {
        log.error("参数绑定校验异常", e.getMessage());

        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 参数校验异常，将校验失败的所有异常组合成一条错误信息
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = ValidationException.class)
    @ResponseBody
    public ResponseEntity handleValidException(ValidationException e) {
        StringBuilder msg = new StringBuilder();
        msg.append(StringUtil.substringAfter(e.getMessage(), "."));
        log.error("参数绑定校验异常", e.getMessage());
        return ResponseEntity.status(ResultCode.PARAM_NOT_VALID.getCode())
                .body(Result.ERROR(ResultCode.PARAM_ERROR.getCode(), msg.toString()));
    }

    /**
     * 包装绑定异常结果
     *
     * @param bindingResult 绑定结果
     * @return 异常结果
     */
    private ResponseEntity wrapperBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();

        for (ObjectError error : bindingResult.getAllErrors()) {
            if (msg.length() != 0) {
                msg.append(", ");
            }

            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(ResultCode.PARAM_NOT_VALID.getMessage());
        }

        return ResponseEntity.status(ResultCode.PARAM_NOT_VALID.getCode())
                .body(Result.ERROR(ResultCode.PARAM_NOT_VALID));
    }

    /**
     * 未定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity handleException(Exception e) {
        log.error("未定义异常" + e.getMessage(), e);

        return ResponseEntity.status(ResultCode.SERVER_ERROR.getCode())
                .body(Result.ERROR(ResultCode.SERVER_ERROR));
    }

}