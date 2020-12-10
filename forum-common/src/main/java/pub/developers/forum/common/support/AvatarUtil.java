package pub.developers.forum.common.support;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.ObjectUtils;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/29
 * @desc
 **/
public class AvatarUtil {

    private static final String GRAVATAR_URL = "https://www.gravatar.com/avatar/%s?d=retro";

    public static String get(String avatar, String email) {
        return ObjectUtils.isEmpty(avatar) ? String.format(GRAVATAR_URL, DigestUtils.md5Hex(ObjectUtils.isEmpty(email) ? "" : email)) : avatar;
    }

}
