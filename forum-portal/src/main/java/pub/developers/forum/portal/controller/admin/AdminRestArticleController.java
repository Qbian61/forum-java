package pub.developers.forum.portal.controller.admin;

import org.springframework.web.bind.annotation.*;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.article.ArticleAddTypeRequest;
import pub.developers.forum.api.request.AdminBooleanRequest;
import pub.developers.forum.api.request.article.ArticleAdminPageRequest;
import pub.developers.forum.api.request.article.ArticleAdminTypePageRequest;
import pub.developers.forum.api.response.article.ArticleQueryTypesResponse;
import pub.developers.forum.api.response.article.ArticleUserPageResponse;
import pub.developers.forum.api.service.ArticleApiService;
import pub.developers.forum.api.service.PostsApiService;
import pub.developers.forum.common.constant.Constant;
import pub.developers.forum.portal.support.WebUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
@RestController
@RequestMapping("/admin-rest/article")
public class AdminRestArticleController {

    @Resource
    private ArticleApiService articleApiService;

    @Resource
    private PostsApiService postsApiService;

    @PostMapping("/all-type")
    public ResultModel<List<ArticleQueryTypesResponse>> allAdminTypes(HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return articleApiService.queryAdminTypes();
    }

    @PostMapping("/page")
    public ResultModel<PageResponseModel<ArticleUserPageResponse>> page(@RequestBody PageRequestModel<ArticleAdminPageRequest> pageRequestModel
            , HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return articleApiService.adminPage(pageRequestModel);
    }

    @PostMapping("/top")
    public ResultModel top(@RequestBody AdminBooleanRequest booleanRequest, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return articleApiService.adminTop(booleanRequest);
    }

    @PostMapping("/official")
    public ResultModel official(@RequestBody AdminBooleanRequest booleanRequest, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return articleApiService.adminOfficial(booleanRequest);
    }

    @PostMapping("/marrow")
    public ResultModel marrow(@RequestBody AdminBooleanRequest booleanRequest, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return articleApiService.adminMarrow(booleanRequest);
    }

    @PostMapping("/audit-state")
    public ResultModel auditState(@RequestBody AdminBooleanRequest booleanRequest, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return postsApiService.auditState(booleanRequest);
    }

    @PostMapping("/type-page")
    public ResultModel<PageResponseModel<ArticleQueryTypesResponse>> typePage(@RequestBody PageRequestModel<ArticleAdminTypePageRequest> pageRequestModel, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return articleApiService.typePage(pageRequestModel);
    }

    @PostMapping("/type-audit-state")
    public ResultModel typeAuditState(@RequestBody AdminBooleanRequest booleanRequest, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return articleApiService.typeAuditState(booleanRequest);
    }

    @PostMapping("/type-add")
    public ResultModel addType(@RequestBody ArticleAddTypeRequest articleAddTypeRequest, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return articleApiService.addType(articleAddTypeRequest);
    }

}
