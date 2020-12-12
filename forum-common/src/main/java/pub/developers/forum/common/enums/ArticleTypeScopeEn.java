package pub.developers.forum.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
@Getter
@AllArgsConstructor
public enum ArticleTypeScopeEn {
    /**
     *
     */
    OFFICIAL("OFFICIAL", "官方"),
    USER("USER", "用户"),
    ;
    private String value;
    private String desc;

    public static ArticleTypeScopeEn getEntity(String value) {
        for (ArticleTypeScopeEn entity : values()) {
            if (entity.getValue().equalsIgnoreCase(value)) {
                return entity;
            }
        }

        return null;
    }
}
