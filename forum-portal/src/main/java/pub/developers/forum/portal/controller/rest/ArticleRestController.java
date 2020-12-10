package pub.developers.forum.portal.controller.rest;

import org.springframework.web.bind.annotation.*;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.article.ArticleSaveArticleRequest;
import pub.developers.forum.api.response.article.ArticleInfoResponse;
import pub.developers.forum.api.response.article.ArticleQueryTypesResponse;
import pub.developers.forum.api.service.ArticleApiService;
import pub.developers.forum.common.constant.Constant;
import pub.developers.forum.portal.support.WebUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
@RestController
@RequestMapping("/article-rest")
public class ArticleRestController {

    @Resource
    private ArticleApiService articleApiService;

    @PostMapping("/save")
    public ResultModel<Long> save(@RequestBody ArticleSaveArticleRequest articleRequest, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return articleApiService.saveArticle(articleRequest);
    }

    @PostMapping("/{id}")
    public ResultModel<ArticleInfoResponse> get(@PathVariable("id") Long id, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));
        return articleApiService.info(id);
    }

    @PostMapping("/editArticleTypes")
    public ResultModel<List<ArticleQueryTypesResponse>> getAllType(HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return articleApiService.queryEditArticleTypes();
    }
}
