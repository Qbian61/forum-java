package pub.developers.forum.app.transfer;

import pub.developers.forum.api.request.faq.FaqSaveFaqRequest;
import pub.developers.forum.api.response.article.ArticleUserPageResponse;
import pub.developers.forum.api.response.faq.FaqHotsResponse;
import pub.developers.forum.api.response.faq.FaqInfoResponse;
import pub.developers.forum.api.response.faq.FaqUserPageResponse;
import pub.developers.forum.api.vo.SolutionVO;
import pub.developers.forum.api.vo.TagVO;
import pub.developers.forum.app.support.LoginUserContext;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.enums.ContentTypeEn;
import pub.developers.forum.common.enums.PostsCategoryEn;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.Article;
import pub.developers.forum.domain.entity.Comment;
import pub.developers.forum.domain.entity.Faq;
import pub.developers.forum.domain.entity.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/1
 * @desc
 **/
public class FaqTransfer {

    public static FaqInfoResponse toFaqInfoResponse(Faq faq) {
        if (faq == null) {
            return null;
        }
        FaqInfoResponse faqInfoResponse = new FaqInfoResponse();
        faqInfoResponse.setAuditState(faq.getAuditState().getValue());
        faqInfoResponse.setTitle(faq.getTitle());
        faqInfoResponse.setMarkdownContent(faq.getMarkdownContent());
        faqInfoResponse.setHtmlContent(faq.getHtmlContent());
        faqInfoResponse.setTags(SafesUtil.ofSet(faq.getTags()).stream().map(tag -> {
            return TagVO.builder()
                    .id(tag.getId())
                    .name(tag.getName())
                    .build();
        }).collect(Collectors.toList()));
        faqInfoResponse.setViews(faq.getViews());
        faqInfoResponse.setApprovals(faq.getApprovals());
        faqInfoResponse.setComments(faq.getComments());
        faqInfoResponse.setId(faq.getId());
        faqInfoResponse.setCreateAt(faq.getCreateAt());
        faqInfoResponse.setUpdateAt(faq.getUpdateAt());
        faqInfoResponse.setAuthorId(faq.getAuthor().getId());
        faqInfoResponse.setAuthorAvatar(faq.getAuthor().getAvatar());
        faqInfoResponse.setAuthorNickname(faq.getAuthor().getNickname());
        return faqInfoResponse;
    }

    public static List<FaqUserPageResponse> toFaqUserPageResponses(List<Faq> faqs, List<Comment> comments) {
        return SafesUtil.ofList(faqs).stream().map(faq -> {
            SolutionVO solution = null;
            for (Comment comment : comments) {
                if (comment.getId().equals(faq.getSolutionId())) {
                    solution = SolutionVO.builder()
                            .content(comment.getContent())
                            .id(comment.getId())
                            .build();
                }
            }
            return FaqUserPageResponse.builder()
                    .category(PostsCategoryEn.FAQ.getValue())
                    .auditState(faq.getAuditState().getDesc())
                    .updateAt(faq.getUpdateAt())
                    .categoryDesc(PostsCategoryEn.FAQ.getDesc())
                    .authorAvatar(faq.getAuthor().getAvatar())
                    .authorId(faq.getAuthor().getId())
                    .authorNickname(faq.getAuthor().getNickname())
                    .comments(faq.getComments())
                    .createAt(faq.getCreateAt())
                    .id(faq.getId())
                    .introduction(faq.getMarkdownContent())
                    .tags(SafesUtil.ofSet(faq.getTags()).stream().map(tag -> {
                        return TagVO.builder()
                                .id(tag.getId())
                                .name(tag.getName())
                                .build();
                    }).collect(Collectors.toList()))
                    .title(faq.getTitle())
                    .views(faq.getViews())
                    .approvals(faq.getApprovals())
                    .solution(solution)
                    .solutionDesc(solution == null ? "未解决" : "已解决")
                    .build();
        }).collect(Collectors.toList());
    }

    public static Faq toFaq(FaqSaveFaqRequest request, Set<Tag> selectTags, Boolean update) {
        Faq faq = Faq.builder().build();
        faq.setId(request.getId());
        faq.setContentType(ContentTypeEn.getEntity(request.getContentType()));
        faq.setHtmlContent(request.getHtmlContent());
        faq.setMarkdownContent(request.getMarkdownContent());
        faq.setTags(selectTags);
        faq.setTitle(request.getTitle());
        faq.setAuthor(LoginUserContext.getUser());

        faq.setAuditState(AuditStateEn.WAIT);

        if (!update) {
            faq.setSolutionId(0L);
            faq.setViews(0L);
            faq.setApprovals(0L);
            faq.setComments(0L);
        }

        return faq;
    }

    public static List<FaqHotsResponse> FaqHotsResponses(List<Faq> faqs) {
        List<FaqHotsResponse> res = new ArrayList<>();

        SafesUtil.ofList(faqs).forEach(faq -> {
            res.add(FaqHotsResponse.builder()
                    .createAt(faq.getCreateAt())
                    .id(faq.getId())
                    .title(faq.getTitle())
                    .build());
        });

        return res;
    }
}
