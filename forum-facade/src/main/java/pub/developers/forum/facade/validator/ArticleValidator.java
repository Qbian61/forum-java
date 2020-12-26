package pub.developers.forum.facade.validator;

import pub.developers.forum.api.request.article.ArticleAddTypeRequest;
import pub.developers.forum.api.request.AdminBooleanRequest;
import pub.developers.forum.api.request.article.ArticleSaveArticleRequest;
import pub.developers.forum.common.support.CheckUtil;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
public class ArticleValidator {

    public static void validatorBooleanRequest(AdminBooleanRequest booleanRequest) {
        CheckUtil.checkParamToast(booleanRequest, "request");
        CheckUtil.checkParamToast(booleanRequest.getId(), "id");
        CheckUtil.checkParamToast(booleanRequest.getIs(), "is");
    }

    public static void addType(ArticleAddTypeRequest request) {

    }

    public static void saveArticle(ArticleSaveArticleRequest request) {
        CheckUtil.checkParamToast(request, "request");
        CheckUtil.checkParamToast(request.getTypeId(), "typeId");
        CheckUtil.checkParamToast(request.getTitle(), "title");
        CheckUtil.checkParamToast(request.getContentType(), "contentType");
        CheckUtil.checkParamToast(request.getMarkdownContent(), "markdownContent");
        CheckUtil.checkParamToast(request.getHtmlContent(), "htmlContent");
        CheckUtil.checkParamToast(request.getTagIds(), "tagIds");
    }
}
