package pub.developers.forum.facade.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.AdminBooleanRequest;
import pub.developers.forum.api.request.user.*;
import pub.developers.forum.api.response.user.UserInfoResponse;
import pub.developers.forum.api.response.user.UserOptLogPageResponse;
import pub.developers.forum.api.response.user.UserPageResponse;
import pub.developers.forum.api.service.UserApiService;
import pub.developers.forum.app.manager.UserManager;
import pub.developers.forum.facade.support.ResultModelUtil;
import pub.developers.forum.facade.validator.ArticleValidator;
import pub.developers.forum.facade.validator.PageRequestModelValidator;
import pub.developers.forum.facade.validator.UserValidator;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Qiangqiang.Bian
 * @create 20/7/30
 * @desc
 **/
@Service
public class UserApiServiceImpl implements UserApiService {

    @Resource
    private UserManager userManager;

    @Override
    public ResultModel enable(Long uid) {
        userManager.enable(uid);

        return ResultModelUtil.success();
    }

    @Override
    public ResultModel disable(Long uid) {
        userManager.disable(uid);

        return ResultModelUtil.success();
    }

    @Override
    public ResultModel follow(Long followed) {
        userManager.follow(followed);

        return ResultModelUtil.success();
    }

    @Override
    public ResultModel cancelFollow(Long followed) {
        userManager.cancelFollow(followed);

        return ResultModelUtil.success();
    }

    @Override
    public ResultModel<PageResponseModel<UserPageResponse>> pageFollower(PageRequestModel<Long> pageRequestModel) {
        PageRequestModelValidator.validator(pageRequestModel);
        pageRequestModel.setFilter(JSON.parseObject(JSON.toJSONString(pageRequestModel.getFilter()), Long.class));

        return ResultModelUtil.success(userManager.pageFollower(pageRequestModel));
    }

    @Override
    public ResultModel<PageResponseModel<UserPageResponse>> pageFans(PageRequestModel<Long> pageRequestModel) {
        PageRequestModelValidator.validator(pageRequestModel);
        pageRequestModel.setFilter(JSON.parseObject(JSON.toJSONString(pageRequestModel.getFilter()), Long.class));

        return ResultModelUtil.success(userManager.pageFans(pageRequestModel));
    }

    @Override
    public ResultModel<Boolean> hasFollow(Long followed) {
        return ResultModelUtil.success(userManager.hasFollow(followed));
    }

    @Override
    public ResultModel<UserInfoResponse> info(String token) {
        return ResultModelUtil.success(userManager.info(token));
    }

    @Override
    public ResultModel<UserInfoResponse> info(Long uid) {
        return ResultModelUtil.success(userManager.info(uid));
    }

    @Override
    public ResultModel<String> register(UserRegisterRequest request) {
        UserValidator.register(request);

        return ResultModelUtil.success(userManager.register(request));
    }

    @Override
    public ResultModel<String> emailLogin(UserEmailLoginRequest request) {
        UserValidator.emailLogin(request);

        return ResultModelUtil.success(userManager.emailRequestLogin(request));
    }

    @Override
    public ResultModel logout(UserTokenLogoutRequest request) {
        UserValidator.logout(request);

        userManager.logout(request);

        return ResultModelUtil.success();
    }

    @Override
    public ResultModel updateInfo(UserUpdateInfoRequest request) {
        UserValidator.updateInfo(request);

        userManager.updateInfo(request);

        return ResultModelUtil.success();
    }
    @Override
    public ResultModel updateHeadImg(String linkFilenameData) {


        userManager.updateHeadimg(linkFilenameData);
        return ResultModelUtil.success();
    }

    @Override
    public ResultModel updatePwd(UserUpdatePwdRequest request) {
        UserValidator.updatePwd(request);

        userManager.updatePwd(request);

        return ResultModelUtil.success();
    }

    @Override
    public ResultModel<PageResponseModel<UserPageResponse>> adminPage(PageRequestModel<UserAdminPageRequest> pageRequestModel) {
        PageRequestModelValidator.validator(pageRequestModel);
        UserAdminPageRequest request = JSON.parseObject(JSON.toJSONString(pageRequestModel.getFilter()), UserAdminPageRequest.class);
        UserValidator.adminPage(request);
        pageRequestModel.setFilter(request);

        return ResultModelUtil.success(userManager.page(pageRequestModel));
    }

    @Override
    public ResultModel<PageResponseModel<UserPageResponse>> pageActive(PageRequestModel pageRequestModel) {
        return ResultModelUtil.success(userManager.pageActive(pageRequestModel));
    }

    @Override
    public ResultModel<PageResponseModel<UserOptLogPageResponse>> pageOptLog(PageRequestModel<UserOptLogPageRequest> pageRequestModel) {
        PageRequestModelValidator.validator(pageRequestModel);
        UserOptLogPageRequest request = JSON.parseObject(JSON.toJSONString(pageRequestModel.getFilter()), UserOptLogPageRequest.class);
        pageRequestModel.setFilter(request);

        return ResultModelUtil.success(userManager.pageOptLog(pageRequestModel));
    }

    @Override
    public ResultModel updateRole(AdminBooleanRequest booleanRequest) {
        ArticleValidator.validatorBooleanRequest(booleanRequest);

        userManager.updateRole(booleanRequest);

        return ResultModelUtil.success();
    }
}
