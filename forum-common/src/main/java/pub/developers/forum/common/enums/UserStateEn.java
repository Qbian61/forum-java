package pub.developers.forum.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/8
 * @desc
 **/
@Getter
@AllArgsConstructor
public enum UserStateEn {
    /**
     *
     */
    UN_ACTIVATION("UN_ACTIVATION", "未激活"),
    ENABLE("ENABLE", "启用"),
    DISABLE("DISABLE", "禁用"),
    ;

    private String value;
    private String desc;

    public static UserStateEn getEntity(String value) {
        for (UserStateEn userSexEn : values()) {
            if (userSexEn.getValue().equalsIgnoreCase(value)) {
                return userSexEn;
            }
        }

        return null;
    }
}
