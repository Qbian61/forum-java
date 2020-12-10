package pub.developers.forum.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/20
 * @desc
 **/
@Getter
@AllArgsConstructor
public enum OptLogTypeEn {
    /**
     *
     */
    USER_LOGIN("USER_LOGIN", "用户登录记录"),
    USER_LOGOUT("USER_LOGOUT", "用户登出记录"),
    USER_REGISTER("USER_REGISTER", "用户注册记录"),
    ;

    private String value;
    private String desc;

    public static OptLogTypeEn getEntity(String value) {
        for (OptLogTypeEn userSexEn : values()) {
            if (userSexEn.getValue().equalsIgnoreCase(value)) {
                return userSexEn;
            }
        }

        return null;
    }
}
