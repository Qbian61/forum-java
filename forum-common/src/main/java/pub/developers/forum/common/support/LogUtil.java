package pub.developers.forum.common.support;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/30
 * @desc
 **/
public class LogUtil {

    public static void info(Logger logger, String format, Object... args) {
        logger.info(getMsg(format, args));
    }

    public static void info(Logger logger, String msg) {
        logger.info(getMsg(msg));
    }

    public static void info(Logger logger, Throwable throwable, String format, Object... args) {
        logger.info(getMsg(format, args), throwable);
    }

    public static void info(Logger logger, String msg, Throwable throwable) {
        logger.info(getMsg(msg), throwable);
    }

    private static String getMsg(String format, Object... arguments) {
        StringBuilder sb = new StringBuilder()
                .append("[traceId-")
                .append(RequestContext.getTraceId())
                .append("] - ");

        if (null != arguments && arguments.length > 0) {
            sb.append(MessageFormatter.arrayFormat(format, arguments).getMessage());
        }else{
            sb.append(format);
        }

        return sb.toString();
    }
}
