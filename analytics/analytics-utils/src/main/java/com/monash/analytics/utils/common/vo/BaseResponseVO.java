package com.monash.analytics.utils.common.vo;


import com.monash.analytics.utils.exception.CommonServiceException;
import lombok.Data;

/**
 * Base response value object
 * @author Xinyu Li
 */

@Data
public class BaseResponseVO<M> {
    private Integer code;
    private String message;
    private M data;

    private BaseResponseVO() {}

    public static BaseResponseVO success() {
        BaseResponseVO responseVO = new BaseResponseVO();
        responseVO.setCode(200);
        responseVO.setMessage("");
        return responseVO;
    }

    public static<M> BaseResponseVO success(M data) {
        BaseResponseVO responseVO = new BaseResponseVO();
        responseVO.setCode(200);
        responseVO.setMessage("");
        responseVO.setData(data);
        return responseVO;
    }

    public static<M> BaseResponseVO serviceException(CommonServiceException e) {
        BaseResponseVO responseVO = new BaseResponseVO();
        responseVO.setCode(e.getCode());
        responseVO.setMessage(e.getMessage());

        return responseVO;
    }
}
