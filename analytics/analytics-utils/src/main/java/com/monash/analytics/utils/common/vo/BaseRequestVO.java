package com.monash.analytics.utils.common.vo;


import com.monash.analytics.utils.exception.CommonServiceException;

/**
 * Base request value object
 * @author Xinyu Li
 */

public abstract class BaseRequestVO {

    public abstract void checkParam() throws CommonServiceException;
}
