package com.fatpanda.notes.common.result.entity;

import com.fatpanda.notes.common.utils.SystemClock;
import org.springframework.http.HttpStatus;

/**
 * @author fatPanda
 * @date 2020/10/14
 */
public class Result {

    private final static String success = "success";

    private Integer code;

    private String msg;

    private Object content;

    private Long ts;

    public Result(Integer code, String msg, Object content) {
        this.code = code;
        this.msg = msg;
        this.content = content;
        this.ts = SystemClock.now();
    }

    public static Result OK() {
        return new Result(HttpStatus.OK.value(), success, null);
    }

    public static Result OK(Object content) {
        return new Result(HttpStatus.OK.value(), success, content);
    }

    public static Result ERROR(int code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result ERROR(ResultCode resultCode) {
        return new Result(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }
}
