package com.monash.analytics.common;


import com.monash.analytics.utils.common.vo.BasePageVO;
import com.monash.analytics.utils.common.vo.BaseRequestVO;
import com.monash.analytics.utils.common.vo.BaseResponseVO;
import com.monash.analytics.utils.exception.CommonServiceException;
import org.junit.jupiter.api.Test;

public class UtilsTest {
    @Test
    public void test() throws CommonServiceException {
        BasePageVO basePageVO = new BasePageVO();
        basePageVO.checkParam();
    }
}
