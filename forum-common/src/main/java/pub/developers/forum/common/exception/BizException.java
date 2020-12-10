package pub.developers.forum.common.exception;

import pub.developers.forum.common.enums.ErrorCodeEn;
import lombok.Data;

/**
 * @author Qiangqiang.Bian
 * @create 20/7/23
 * @desc
 **/
@Data
public class BizException extends RuntimeException {

    private String message;
    private Integer code;

    /**
     * @desc 默认为系统异常
     */
    public BizException() {
        this(ErrorCodeEn.SYSTEM_ERROR);
    }

    /**
     * @desc 指定参数的业务异常
     * @param errorCode 异常枚举
     */
    public BizException(ErrorCodeEn errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * @desc 指定参数的业务异常
     * @param code code
     * @param message message
     */
    public BizException(Integer code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

}