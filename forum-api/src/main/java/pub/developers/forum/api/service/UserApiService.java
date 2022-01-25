package pub.developers.forum.api.service;

import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.AdminBooleanRequest;
import pub.developers.forum.api.request.user.*;
import pub.developers.forum.api.response.user.UserInfoResponse;
import pub.developers.forum.api.response.user.UserOptLogPageResponse;
import pub.developers.forum.api.response.user.UserPageResponse;
import org.springframework.web.multipart.MultipartFile;
/**
 * @author Qiangqiang.Bian
 * @create 20/7/30
 * @desc
 **/
public interface UserApiService {

    ResultModel enable(Long uid);

    ResultModel disable(Long uid);

    ResultModel follow(Long followed);

    ResultModel cancelFollow(Long followed);

    ResultModel<PageResponseModel<UserPageResponse>> pageFollower(PageRequestModel<Long> pageRequestModel);

    ResultModel<PageResponseModel<UserPageResponse>> pageFans(PageRequestModel<Long> pageRequestModel);

    ResultModel<Boolean> hasFollow(Long followed);

    ResultModel<UserInfoResponse> info(String token);

    ResultModel<UserInfoResponse> info(Long uid);

    ResultModel<String> register(UserRegisterRequest request);

    ResultModel<String> emailLogin(UserEmailLoginRequest request);

    ResultModel logout(UserTokenLogoutRequest request);

    ResultModel updateInfo(UserUpdateInfoRequest request);

    ResultModel updateHeadImg(String linkFilenameData);

    ResultModel updatePwd(UserUpdatePwdRequest request);

    ResultModel<PageResponseModel<UserPageResponse>> adminPage(PageRequestModel<UserAdminPageRequest> pageRequestModel);

    ResultModel<PageResponseModel<UserPageResponse>> pageActive(PageRequestModel pageRequestModel);

    ResultModel<PageResponseModel<UserOptLogPageResponse>> pageOptLog(PageRequestModel<UserOptLogPageRequest> pageRequestModel);

    ResultModel updateRole(AdminBooleanRequest booleanRequest);
}
