package pub.developers.forum.portal.controller.admin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.AdminBooleanRequest;
import pub.developers.forum.api.request.faq.FaqAdminPageRequest;
import pub.developers.forum.api.response.faq.FaqUserPageResponse;
import pub.developers.forum.api.service.FaqApiService;
import pub.developers.forum.api.service.PostsApiService;
import pub.developers.forum.common.constant.Constant;
import pub.developers.forum.portal.support.WebUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/27
 * @desc
 **/
@RestController
@RequestMapping("/admin-rest/faq")
public class AdminRestFaqController {

    @Resource
    private FaqApiService faqApiService;

    @Resource
    private PostsApiService postsApiService;

    @PostMapping("/page")
    public ResultModel<PageResponseModel<FaqUserPageResponse>> page(@RequestBody PageRequestModel<FaqAdminPageRequest> pageRequestModel
            , HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return faqApiService.adminPage(pageRequestModel);
    }

    @PostMapping("/audit-state")
    public ResultModel auditState(@RequestBody AdminBooleanRequest booleanRequest, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return postsApiService.auditState(booleanRequest);
    }

}
