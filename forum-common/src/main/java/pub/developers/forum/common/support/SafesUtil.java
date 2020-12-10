package pub.developers.forum.common.support;

import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Qiangqiang.Bian
 * @create 20/7/23
 * @desc
 **/
public class SafesUtil {
    private SafesUtil() {
    }

    /**
     * 返回空List对象
     *
     * @param value
     * @return
     */
    public static <T> List<T> ofList(List<T> value) {
        return ObjectUtils.isEmpty(value) ? Collections.emptyList() : value;
    }

    /**
     * 返回空Set对象
     *
     * @param value
     * @param <T>
     * @return
     */
    public static <T> Set<T> ofSet(Set<T> value) {
        return ObjectUtils.isEmpty(value) ? Collections.emptySet() : value;
    }

    /**
     * 返回空Map对象
     *
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> ofMap(Map<K, V> value) {
        return ObjectUtils.isEmpty(value) ? Collections.emptyMap() : value;
    }
}