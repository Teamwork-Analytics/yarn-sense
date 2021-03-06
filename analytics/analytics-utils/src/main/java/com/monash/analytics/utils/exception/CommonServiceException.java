package com.monash.analytics.utils.exception;

import lombok.Data;

/**
 * Common service exception
 * @author Xinyu Li
 */

@Data
public class CommonServiceException extends Exception {
    private Integer code;
    private String message;

    public CommonServiceException(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
