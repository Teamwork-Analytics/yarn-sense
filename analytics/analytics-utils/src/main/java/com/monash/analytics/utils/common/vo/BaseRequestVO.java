package com.monash.analytics.utils.common.vo;


import com.monash.analytics.utils.exception.CommonServiceException;

public abstract class BaseRequestVO {

    public abstract void checkParam() throws CommonServiceException;
}
