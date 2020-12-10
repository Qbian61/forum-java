package pub.developers.forum.portal.support;

import pub.developers.forum.api.response.user.UserInfoResponse;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/29
 * @desc
 **/
public class WebContext {

    /**
     * 当前登录用户
     */
    private static final ThreadLocal<UserInfoResponse> CURRENT_LOGIN_USER = new ThreadLocal<>();

    /**
     * 当前登录用户登录凭证 sid
     */
    private static final ThreadLocal<String> CURRENT_USER_LOGIN_SID = new ThreadLocal<>();

    /**
     * 保存当前登录用户
     * @param loginUser
     */
    public static void setCurrentUser(UserInfoResponse loginUser) {
        CURRENT_LOGIN_USER.set(loginUser);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public static UserInfoResponse getCurrentUser() {
        return CURRENT_LOGIN_USER.get();
    }

    /**
     * 保存当前登录凭证
     * @param sid
     */
    public static void setCurrentSid(String sid) {
        CURRENT_USER_LOGIN_SID.set(sid);
    }

    /**
     * 获取当前登录凭证
     * @return
     */
    public static String getCurrentSid() {
        return CURRENT_USER_LOGIN_SID.get();
    }

    public static void removeAll() {
        CURRENT_LOGIN_USER.remove();
        CURRENT_USER_LOGIN_SID.remove();
    }

}
