package com.oyproj.common.exception;

import com.oyproj.common.api.IErrorCode;

/**
 * @author oy
 * @description 断言处理类，用于抛出各种异常
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
