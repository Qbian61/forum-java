package pub.developers.forum.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/22
 * @desc
 **/
@Getter
@AllArgsConstructor
public enum IdValueTypeEn {
    SYSTEM("SYSTEM", "系统"),
    USER_ID("USER_ID", "用户ID"),
    EMAIL("MAIL", "邮箱"),
    ;
    private String value;
    private String desc;

    public static IdValueTypeEn getEntity(String value) {
        for (IdValueTypeEn contentType : values()) {
            if (contentType.getValue().equals(value)) {
                return contentType;
            }
        }
        return null;
    }
}
