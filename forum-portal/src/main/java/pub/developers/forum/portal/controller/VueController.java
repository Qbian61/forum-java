package pub.developers.forum.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.response.user.UserInfoResponse;
import pub.developers.forum.api.service.UserApiService;
import pub.developers.forum.common.enums.UserRoleEn;
import pub.developers.forum.portal.support.ViewException;
import pub.developers.forum.portal.support.WebContext;
import pub.developers.forum.portal.support.WebUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/9
 * @desc
 **/
@Controller
@RequestMapping("/")
public class VueController {

    @Resource
    private UserApiService userApiService;

    @GetMapping(value = {"/article/editor", "/faq/editor", "/faq/editor/{id}", "/article/editor/{id}"})
    public String editor() {
        return "vue-index";
    }

    @GetMapping(value = {"/admin"
            , "/admin/user-manager", "/admin/user-opt-log"
            , "/admin/article-manager", "/admin/article-type"
            , "/admin/faq-manager"
            , "/admin/message-manager"
            , "/admin/tag-manager"})
    public String admin(HttpServletRequest request) {
        String sid = WebUtil.cookieGetSid(request);
        if (ObjectUtils.isEmpty(sid)) {
            throw ViewException.build("用户未登录");
        }

        ResultModel<UserInfoResponse> resultModel = userApiService.info(sid);
        if (!resultModel.getSuccess() || ObjectUtils.isEmpty(resultModel.getData())) {
            throw ViewException.build(resultModel.getMessage());
        }
        UserInfoResponse userInfoResponse = resultModel.getData();
        if (UserRoleEn.USER.getValue().equals(userInfoResponse.getRole())) {
            throw ViewException.build("无权限访问");
        }

        return "vue-admin";
    }

}
