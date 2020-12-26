package pub.developers.forum.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/26
 * @desc
 **/
@Getter
@AllArgsConstructor
public enum ConfigTypeEn {

    /**
     * 首页轮播图
     */
    HOME_CAROUSEL("HOME_CAROUSEL", "首页轮播图"),

    /**
     * 侧边栏轮播图
     */
    SIDEBAR_CAROUSEL("SIDEBAR_CAROUSEL", "侧边栏轮播图"),
    ;

    private String value;
    private String desc;

    public static ConfigTypeEn getEntity(String value) {
        for (ConfigTypeEn contentType : values()) {
            if (contentType.getValue().equals(value)) {
                return contentType;
            }
        }
        return null;
    }

}
