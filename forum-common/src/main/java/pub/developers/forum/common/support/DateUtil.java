package pub.developers.forum.common.support;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/8
 * @desc
 **/
public class DateUtil {

    public static String toyyyyMMddHHmmss(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
