package pub.developers.forum.facade.validator;

import pub.developers.forum.api.request.faq.FaqSaveFaqRequest;
import pub.developers.forum.api.request.faq.FaqSolutionRequest;
import pub.developers.forum.common.support.CheckUtil;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/1
 * @desc
 **/
public class FaqValidator {

    public static void saveFaq(FaqSaveFaqRequest request) {
        CheckUtil.checkParamToast(request, "request");
        CheckUtil.checkParamToast(request.getTitle(), "title");
        CheckUtil.checkParamToast(request.getContentType(), "contentType");
        CheckUtil.checkParamToast(request.getMarkdownContent(), "markdownContent");
        CheckUtil.checkParamToast(request.getHtmlContent(), "htmlContent");
        CheckUtil.checkParamToast(request.getTagIds(), "tagIds");
    }

    public static void solution(FaqSolutionRequest request) {
        CheckUtil.checkParamToast(request, "request");
        CheckUtil.checkParamToast(request.getFaqId(), "faqId");
        CheckUtil.checkParamToast(request.getCommentId(), "commentId");
    }
}
