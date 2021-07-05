package com.fatpanda.notes.common.result.entity;


import com.fatpanda.notes.common.exceptionHandler.entity.IResponseEnum;

/**
 * @Author: fatpanda
 * @Description: 返回码定义
 * 规定:
 * #200 表示成功
 * #1001～1999 区间表示参数错误
 * #2001～2999 区间表示用户错误
 * #3001～3999 区间表示接口异常
 * @Date Create in 2019/7/22 19:28
 */
public enum ResultCode implements IResponseEnum {
    /* 成功 */
    SUCCESS(200, "成功"),

    /* 用户异常 */
    USER_NOT_LOGIN(3001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(3002, "账号已过期"),
    USER_CREDENTIALS_ERROR(3003, "密码错误"),
    USER_CREDENTIALS_EXPIRED(3004, "密码过期"),
    USER_ACCOUNT_DISABLE(3005, "账号不可用"),
    USER_ACCOUNT_LOCKED(3006, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(3007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(3008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(3009, "账号下线"),

    /* 业务错误 */
    NO_PERMISSION(3021, "没有权限"),

    /* 客户端异常 */
    CLIENT_ERROR(4000, "请求异常"),
    /* 参数错误 */
    PARAM_ERROR(4001, "参数错误"),
    PARAM_NOT_VALID(4001, "参数无效"),
    PARAM_IS_BLANK(4002, "参数为空"),
    PARAM_TYPE_ERROR(4003, "参数类型错误"),
    PARAM_NOT_COMPLETE(4004, "参数缺失"),

    /* 服务端异常 */
    SERVER_ERROR(5000, "业务异常");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (ResultCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
