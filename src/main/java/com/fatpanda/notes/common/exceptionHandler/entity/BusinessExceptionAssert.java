package com.fatpanda.notes.common.exceptionHandler.entity;

import java.text.MessageFormat;

/**
 * @author fatPanda
 */
public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    /**
     * 创建异常
     *
     * @param args
     * @return
     */
    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new BusinessException(this, args, msg);
    }

    /**
     * 创建异常
     *
     * @param t
     * @param args
     * @return
     */
    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new BusinessException(this, args, msg, t);
    }

}