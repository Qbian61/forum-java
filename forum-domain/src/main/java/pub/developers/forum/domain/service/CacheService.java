package pub.developers.forum.domain.service;

import pub.developers.forum.common.enums.CacheBizTypeEn;

/**
 * @author Qiangqiang.Bian
 * @create 20/7/23
 * @desc
 **/
public interface CacheService {

    /**
     * 存储
     * @param key
     * @param value
     * @return
     */
    boolean set(CacheBizTypeEn bizType, String key, String value);

    /**
     * 存储并设置超时时长(单位:秒)
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    boolean setAndExpire(CacheBizTypeEn bizType, String key, String value, Long seconds);

    /**
     * 获取值
     * @param key
     * @return
     */
    String get(CacheBizTypeEn bizType, String key);

    /**
     * 判断是否存在
     * @param key
     * @return
     */
    Boolean exists(CacheBizTypeEn bizType, String key);

    /**
     * 删除
     * @param key
     * @return
     */
    Boolean del(CacheBizTypeEn bizType, String key);

}
