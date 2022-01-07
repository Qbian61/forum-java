package pub.developers.forum.app.manager;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.request.AdminBooleanRequest;
import pub.developers.forum.api.request.user.*;
import pub.developers.forum.api.response.user.UserInfoResponse;
import pub.developers.forum.api.response.user.UserOptLogPageResponse;
import pub.developers.forum.api.response.user.UserPageResponse;
import pub.developers.forum.app.support.IsLogin;
import pub.developers.forum.app.support.Pair;
import pub.developers.forum.app.transfer.OptLogTransfer;
import pub.developers.forum.common.enums.UserStateEn;
import pub.developers.forum.common.support.*;
import pub.developers.forum.domain.entity.Follow;
import pub.developers.forum.domain.repository.OptLogRepository;
import pub.developers.forum.app.support.LoginUserContext;
import pub.developers.forum.app.transfer.UserTransfer;
import pub.developers.forum.app.support.PageUtil;
import pub.developers.forum.common.enums.CacheBizTypeEn;
import pub.developers.forum.common.enums.ErrorCodeEn;
import pub.developers.forum.common.enums.UserRoleEn;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.domain.entity.OptLog;
import pub.developers.forum.domain.entity.User;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/9/8
 * @desc
 **/
@Component
public class UserManager extends AbstractLoginManager {

    @Resource
    private OptLogRepository optLogRepository;

    /**
     * 邮箱 + 密码 登录
     * @param request
     * @return
     */
    public String emailRequestLogin(UserEmailLoginRequest request) {
        // 判断邮箱是否存在
        User user = userRepository.getByEmail(request.getEmail());
        CheckUtil.isEmpty(user, ErrorCodeEn.USER_NOT_EXIST);
        CheckUtil.isTrue(UserStateEn.DISABLE.equals(user.getState()), ErrorCodeEn.USER_STATE_IS_DISABLE);

        // 判断登录密码是否正确
        CheckUtil.isFalse(StringUtil.md5UserPassword(request.getPassword()).equals(user.getPassword()), ErrorCodeEn.USER_LOGIN_PWD_ERROR);

        // 更新最后登录时间
        user.setLastLoginTime(new Date());
        userRepository.update(user);

        return login(user, request);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public PageResponseModel<UserOptLogPageResponse> pageOptLog(PageRequestModel<UserOptLogPageRequest> pageRequestModel) {
        PageResult<OptLog> pageResult = optLogRepository.page(PageUtil.buildPageRequest(pageRequestModel, OptLogTransfer.toOptLog(pageRequestModel.getFilter())));

        if (ObjectUtils.isEmpty(pageResult.getList())) {
            return PageUtil.buildPageResponseModel(pageResult, new ArrayList<>());
        }
        List<Long> userIdList = pageResult.getList().stream().map(OptLog::getOperatorId).collect(Collectors.toList());
        List<User> userList = userRepository.queryByIds(userIdList);

        return PageUtil.buildPageResponseModel(pageResult, UserTransfer.toUserOptLogPageResponses(pageResult.getList(), userList));
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public void enable(Long uid) {
        User user = userRepository.get(uid);
        CheckUtil.isEmpty(user, ErrorCodeEn.USER_NOT_EXIST);

        user.setState(UserStateEn.ENABLE);
        userRepository.update(user);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public void disable(Long uid) {
        User user = userRepository.get(uid);
        CheckUtil.isEmpty(user, ErrorCodeEn.USER_NOT_EXIST);
        CheckUtil.isEmpty(UserRoleEn.SUPER_ADMIN.equals(user.getRole()), ErrorCodeEn.COMMON_TOKEN_NO_PERMISSION);

        user.setState(UserStateEn.DISABLE);
        userRepository.update(user);

        // 删除用户登录信息
        deleteLoginUser(uid);
    }

    @IsLogin
    public void follow(Long followed) {
        Long follower = LoginUserContext.getUser().getId();
        userRepository.follow(followed, follower);

        Pair<Long> follow = Pair.build(followed, follower);
        EventBus.emit(EventBus.Topic.USER_FOLLOW, follow);
    }

    @IsLogin
    public void cancelFollow(Long followed) {
        Follow follow = userRepository.getFollow(followed, LoginUserContext.getUser().getId());
        if (follow == null) {
            return;
        }
        userRepository.cancelFollow(follow.getId());

        EventBus.emit(EventBus.Topic.USER_CANCEL_FOLLOW, follow);
    }

    /**
     * 获取 token 对应用户详情
     * @param token
     * @return
     */
    public UserInfoResponse info(String token) {
        String cacheUserStr = cacheService.get(CacheBizTypeEn.USER_LOGIN_TOKEN, token);
        CheckUtil.isEmpty(cacheUserStr, ErrorCodeEn.USER_TOKEN_INVALID);

        return UserTransfer.toUserInfoResponse(JSON.parseObject(cacheUserStr, User.class));
    }

    public UserInfoResponse info(Long uid) {
        User user = userRepository.get(uid);
        CheckUtil.isEmpty(user, ErrorCodeEn.USER_NOT_EXIST);

        return UserTransfer.toUserInfoResponse(user);
    }

    /**
     * 用户注册
     * @param request
     */
    @Transactional
    public String register(UserRegisterRequest request) {
        // 判断邮箱是否已经被注册
        User user = userRepository.getByEmail(request.getEmail());
        CheckUtil.isNotEmpty(user, ErrorCodeEn.USER_REGISTER_EMAIL_IS_EXIST);

        User registerUser = UserTransfer.toUser(request);

        // 保存注册用户
        userRepository.save(registerUser);

        // 触发保存操作日志事件
        EventBus.emit(EventBus.Topic.USER_REGISTER, registerUser);

        return login(registerUser, request);
    }

    /**
     * 登出
     * @param request
     */
    public void logout(UserTokenLogoutRequest request) {
        User user = delCacheLoginUser(request.getToken());
        if (ObjectUtils.isEmpty(user)) {
            return;
        }

        // 触发保存操作日志事件
        EventBus.emit(EventBus.Topic.USER_LOGOUT, OptLog.createUserLogoutRecordLog(user.getId(), JSON.toJSONString(request)));
    }

    /**
     * 用户更新基本信息
     * @param request
     */
    @IsLogin
    @Transactional
    public void updateInfo(UserUpdateInfoRequest request) {
        User loginUser = LoginUserContext.getUser();

        User user = userRepository.getByEmail(request.getEmail());
        if (!ObjectUtils.isEmpty(user)) {
            CheckUtil.isFalse(user.getId().equals(loginUser.getId()), ErrorCodeEn.USER_REGISTER_EMAIL_IS_EXIST);
        }

        User updateUser = UserTransfer.toUser(loginUser, request);

        // 更新缓存中登录用户信息
        updateCacheUser(updateUser);
        userRepository.update(updateUser);
    }


    /**
     * 用户更新头像
     * @param request
     */
    @IsLogin
    @Transactional
    public void updateHeadimg(String linkFilenameData) {
        User loginUser = LoginUserContext.getUser();

       /* User user = userRepository.getByEmail(request.getEmail());
        if (!ObjectUtils.isEmpty(user)) {
            CheckUtil.isFalse(user.getId().equals(loginUser.getId()), ErrorCodeEn.USER_REGISTER_EMAIL_IS_EXIST);
        }*/
        loginUser.setAvatar(linkFilenameData);
        // 更新缓存中登录用户信息
        updateCacheUser(loginUser);
        userRepository.update(loginUser);
    }
    /**
     * 更新登录密码
     * @param request
     */
    @IsLogin
    @Transactional
    public void updatePwd(UserUpdatePwdRequest request) {
        User user = LoginUserContext.getUser();
        CheckUtil.isFalse(StringUtil.md5UserPassword(request.getOldPassword()).equals(user.getPassword()), ErrorCodeEn.USER_OLD_PASSWORD_ERROR);

        user.setPassword(StringUtil.md5UserPassword(request.getNewPassword()));

        // 更新缓存中登录用户信息
        updateCacheUser(user);
        userRepository.update(user);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public PageResponseModel<UserPageResponse> page(PageRequestModel<UserAdminPageRequest> pageRequestModel) {
        PageResult<User> pageResult = userRepository.page(PageUtil.buildPageRequest(pageRequestModel, UserTransfer.toUser(pageRequestModel.getFilter())));

        return PageUtil.buildPageResponseModel(pageResult, UserTransfer.toUserPageResponses(pageResult.getList()));
    }

    public PageResponseModel<UserPageResponse> pageFollower(PageRequestModel<Long> pageRequestModel) {
        PageResult<User> pageResult = userRepository.pageFollower(PageUtil.buildPageRequest(pageRequestModel, pageRequestModel.getFilter()));

        return PageUtil.buildPageResponseModel(pageResult, UserTransfer.toUserPageResponses(pageResult.getList()));
    }

    public PageResponseModel<UserPageResponse> pageFans(PageRequestModel<Long> pageRequestModel) {
        PageResult<User> pageResult = userRepository.pageFans(PageUtil.buildPageRequest(pageRequestModel, pageRequestModel.getFilter()));

        return PageUtil.buildPageResponseModel(pageResult, UserTransfer.toUserPageResponses(pageResult.getList()));
    }

    @IsLogin
    public Boolean hasFollow(Long followed) {
        Follow follow = userRepository.getFollow(followed, LoginUserContext.getUser().getId());
        return follow != null;
    }

    public PageResponseModel<UserPageResponse> pageActive(PageRequestModel pageRequestModel) {
        PageResult<User> pageResult = userRepository.pageActive(PageUtil.buildPageRequest(pageRequestModel));

        return PageUtil.buildPageResponseModel(pageResult, UserTransfer.toUserPageResponses(pageResult.getList()));
    }

    private void updateCacheUser(User updateUser) {
        LoginUserContext.setUser(updateUser);
        cacheLoginUser(LoginUserContext.getToken(), updateUser);
    }

    private User delCacheLoginUser(String token) {
        String oldUser = cacheService.get(CacheBizTypeEn.USER_LOGIN_TOKEN, token);
        if (ObjectUtils.isEmpty(oldUser)) {
            return null;
        }

        User loginUser = JSON.parseObject(oldUser, User.class);
        cacheService.del(CacheBizTypeEn.USER_LOGIN_TOKEN, String.valueOf(loginUser.getId()));
        cacheService.del(CacheBizTypeEn.USER_LOGIN_TOKEN, token);

        return loginUser;
    }


    @IsLogin(role = UserRoleEn.SUPER_ADMIN)
    public void updateRole(AdminBooleanRequest booleanRequest) {
        User user = userRepository.get(booleanRequest.getId());
        CheckUtil.isEmpty(user, ErrorCodeEn.USER_NOT_EXIST);

        user.setRole(booleanRequest.getIs() ? UserRoleEn.ADMIN : UserRoleEn.USER);
        userRepository.update(user);
    }
}
