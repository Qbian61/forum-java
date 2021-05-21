package pub.developers.forum.app.manager;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.api.request.github.GithubOauthLoginRequest;
import pub.developers.forum.app.transfer.UserTransfer;
import pub.developers.forum.common.enums.ErrorCodeEn;
import pub.developers.forum.common.enums.UserSourceEn;
import pub.developers.forum.common.enums.UserStateEn;
import pub.developers.forum.common.support.CheckUtil;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.User;
import pub.developers.forum.domain.service.GithubService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Qiangqiang.Bian
 * @create 2021/5/15
 * @desc
 **/
@Component
public class GithubManager extends AbstractLoginManager {

    @Resource
    private GithubService githubService;

    public String oauthLogin(GithubOauthLoginRequest request) {
        // 获取 user info = {"login":"Qbian61","id":16890764,"node_id":"MDQ6VXNlcjE2ODkwNzY0","avatar_url":"https://avatars.githubusercontent.com/u/16890764?v=4","gravatar_id":"","url":"https://api.github.com/users/Qbian61","html_url":"https://github.com/Qbian61","followers_url":"https://api.github.com/users/Qbian61/followers","following_url":"https://api.github.com/users/Qbian61/following{/other_user}","gists_url":"https://api.github.com/users/Qbian61/gists{/gist_id}","starred_url":"https://api.github.com/users/Qbian61/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/Qbian61/subscriptions","organizations_url":"https://api.github.com/users/Qbian61/orgs","repos_url":"https://api.github.com/users/Qbian61/repos","events_url":"https://api.github.com/users/Qbian61/events{/privacy}","received_events_url":"https://api.github.com/users/Qbian61/received_events","type":"User","site_admin":false,"name":"Qbian","company":null,"blog":"","location":null,"email":"15620608572@163.com","hireable":null,"bio":"打工人","twitter_username":null,"public_repos":6,"public_gists":0,"followers":22,"following":4,"created_at":"2016-01-26T02:17:29Z","updated_at":"2021-05-15T05:10:24Z","private_gists":0,"total_private_repos":3,"owned_private_repos":2,"disk_usage":20878,"collaborators":1,"two_factor_authentication":false,"plan":{"name":"free","space":976562499,"collaborators":0,"private_repos":10000}}
        JSONObject githubUser = githubService.getUserInfo(request.getCode());

        String loginName = githubUser.getString("login");
        String email = ObjectUtils.isEmpty(githubUser.getString("email")) ? loginName + "@github.com" : githubUser.getString("email");
        String nickname = ObjectUtils.isEmpty(githubUser.getString("name")) ? loginName : githubUser.getString("name");
        String signature = ObjectUtils.isEmpty(githubUser.getString("bio")) ? "" : githubUser.getString("bio");
        String avatar = ObjectUtils.isEmpty(githubUser.getString("avatar_url")) ? "" : githubUser.getString("avatar_url");

        User user = userRepository.getByEmail(email);

        if (ObjectUtils.isEmpty(user)) {
            user = UserTransfer.toGithubUser(githubUser, email, nickname, signature, avatar);
            // 保存注册用户
            userRepository.save(user);

            // 触发保存操作日志事件
            EventBus.emit(EventBus.Topic.USER_REGISTER, user);
        } else {
            CheckUtil.isTrue(UserStateEn.DISABLE.equals(user.getState()), ErrorCodeEn.USER_STATE_IS_DISABLE);
            user.setLastLoginTime(new Date());
            user.setSource(UserSourceEn.GITHUB);
            user.setGithubUser(githubUser);
            user.setNickname(nickname);
            if (!ObjectUtils.isEmpty(signature)) {
                user.setSignature(signature);
            }
            if (!ObjectUtils.isEmpty(avatar)) {
                user.setAvatar(avatar);
            }

            userRepository.update(user);
        }

        return login(user, request);
    }
}
