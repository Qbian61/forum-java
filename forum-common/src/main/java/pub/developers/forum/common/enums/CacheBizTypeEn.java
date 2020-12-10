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
public enum CacheBizTypeEn {
    USER_LOGIN_TOKEN("USER_LOGIN_TOKEN", "用户登录凭证 token"),
    ;

    private String value;
    private String desc;
}
