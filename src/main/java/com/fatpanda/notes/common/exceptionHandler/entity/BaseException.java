package com.fatpanda.notes.common.exceptionHandler.entity;

/**
 * @author fatPanda
 */

public class BaseException extends RuntimeException {
    /**
     * 错误码
     */
    private int code;

    private Object[] args;

    public BaseException(IResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
        this.args = null;
    }

    public BaseException(IResponseEnum responseEnum, String message) {
        super(message);
        this.code = responseEnum.getCode();
        this.args = null;
    }

    public BaseException(IResponseEnum responseEnum, Object[] args, String message) {
        super(message);
        this.code = responseEnum.getCode();
        this.args = args;
    }

    public BaseException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(message, cause);
        this.code = responseEnum.getCode();
        this.args = args;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getMsg() {
        return super.getMessage();
    }
}
