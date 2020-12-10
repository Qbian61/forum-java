package pub.developers.forum.common.support;

import pub.developers.forum.common.enums.ErrorCodeEn;
import pub.developers.forum.common.exception.BizException;
import org.springframework.util.ObjectUtils;

import java.text.MessageFormat;

/**
 * @author Qiangqiang.Bian
 * @create 20/7/23
 * @desc
 **/
public class CheckUtil {

    private CheckUtil() {
    }

    /**
     * 检查请求路径参数是否为空
     *
     * @param o
     */
    public static void checkParamToast(Object o, String message) {
        if (ObjectUtils.isEmpty(o)) {
            throw new BizException(ErrorCodeEn.PARAM_CHECK_ERROR.getCode(),
                    MessageFormat.format(ErrorCodeEn.PARAM_CHECK_ERROR.getMessage(), message));
        }
    }

    /**
     * 检查请求路径参数是否为空
     *
     * @param o
     */
    public static void checkEmptyToast(Object o, String message) {
        if (ObjectUtils.isEmpty(o)) {
            throw new BizException(ErrorCodeEn.CHECK_ERROR_TOAST.getCode(),
                    MessageFormat.format(ErrorCodeEn.CHECK_ERROR_TOAST.getMessage(), message));
        }
    }

    /**
     * @param o         被检查的对象
     * @param errorCode 抛出的异常信息
     * @desc 检查是否为空，为空则报异常
     */
    public static void isEmpty(Object o, ErrorCodeEn errorCode) {
        if (ObjectUtils.isEmpty(o)) {
            throw new BizException(errorCode);
        }
    }

    public static void isNotEmpty(Object o, ErrorCodeEn errorCode) {
        if (!ObjectUtils.isEmpty(o)) {
            throw new BizException(errorCode);
        }
    }

    public static void isFalse(Boolean b, ErrorCodeEn errorCode) {
        if (!b) {
            throw new BizException(errorCode);
        }
    }

    public static void isTrue(Boolean b, ErrorCodeEn errorCode) {
        if (b) {
            throw new BizException(errorCode);
        }
    }

}