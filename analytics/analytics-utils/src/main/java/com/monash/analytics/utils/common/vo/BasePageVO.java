package com.monash.analytics.utils.common.vo;

import com.monash.analytics.utils.exception.CommonServiceException;
import lombok.Data;

/**
 * Base page value object
 * @author Xinyu Li
 */

@Data
public class BasePageVO extends BaseRequestVO{
    private Integer nowPage = 1;
    private Integer pageSize = 10;

    @Override
    public void checkParam() throws CommonServiceException {

    }
}
