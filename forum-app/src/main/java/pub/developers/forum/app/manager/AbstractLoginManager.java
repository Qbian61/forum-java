package pub.developers.forum.app.manager;

import com.alibaba.fastjson.JSON;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.api.request.user.UserBaseLoginRequest;
import pub.developers.forum.common.enums.CacheBizTypeEn;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.common.support.StringUtil;
import pub.developers.forum.domain.entity.OptLog;
import pub.developers.forum.domain.entity.User;
import pub.developers.forum.domain.repository.UserRepository;
import pub.developers.forum.domain.service.CacheService;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2021/5/15
 * @desc
 **/
public class AbstractLoginManager {

    @Resource
    UserRepository userRepository;

    @Resource
    CacheService cacheService;

    /**
     * 用户登录凭证 token 过期时长（单位：秒）,7天
     */
    private static final Long USER_LOGIN_TOKEN_EXPIRE_TIMEOUT = 60 * 60 * 24 * 7L;

    protected String login(User user, UserBaseLoginRequest baseLoginRequest) {
        // 缓存登录凭证 token =》 user
        String token = StringUtil.generateUUID();
        cacheLoginUser(token, user);

        // 触发保存操作日志事件
        EventBus.emit(EventBus.Topic.USER_LOGIN, OptLog.createUserLoginRecordLog(user.getId(), JSON.toJSONString(baseLoginRequest)));

        return token;
    }

    protected void cacheLoginUser(String token, User user) {
        // 删除之前登录缓存
        deleteLoginUser(user.getId());

        // 重新保存缓存
        cacheService.setAndExpire(CacheBizTypeEn.USER_LOGIN_TOKEN
                , String.valueOf(user.getId()), token, USER_LOGIN_TOKEN_EXPIRE_TIMEOUT);
        cacheService.setAndExpire(CacheBizTypeEn.USER_LOGIN_TOKEN
                , token, JSON.toJSONString(user), USER_LOGIN_TOKEN_EXPIRE_TIMEOUT);
    }

    protected void deleteLoginUser(Long userId) {
        // 删除之前登录缓存
        String oldToken = cacheService.get(CacheBizTypeEn.USER_LOGIN_TOKEN, String.valueOf(userId));
        if (!ObjectUtils.isEmpty(oldToken)) {
            cacheService.del(CacheBizTypeEn.USER_LOGIN_TOKEN, String.valueOf(userId));
            cacheService.del(CacheBizTypeEn.USER_LOGIN_TOKEN, oldToken);
        }
    }
}
