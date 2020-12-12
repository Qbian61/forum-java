package pub.developers.forum.app.transfer;

import pub.developers.forum.api.request.article.ArticleAddTypeRequest;
import pub.developers.forum.api.request.article.ArticleSaveArticleRequest;
import pub.developers.forum.api.response.article.ArticleInfoResponse;
import pub.developers.forum.api.response.article.ArticleQueryTypesResponse;
import pub.developers.forum.api.response.article.ArticleUserPageResponse;
import pub.developers.forum.api.vo.TagVO;
import pub.developers.forum.app.support.LoginUserContext;
import pub.developers.forum.common.enums.ArticleTypeScopeEn;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.enums.ContentTypeEn;
import pub.developers.forum.common.enums.PostsCategoryEn;
import pub.developers.forum.common.support.DateUtil;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.Article;
import pub.developers.forum.domain.entity.ArticleType;
import pub.developers.forum.domain.entity.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
public class ArticleTransfer {

    public static ArticleInfoResponse toArticleInfoResponse(Article article) {
        return ArticleInfoResponse.builder()
                .typeId(article.getType().getId())
                .authorAvatar(article.getAuthor().getAvatar())
                .authorId(article.getAuthor().getId())
                .authorNickname(article.getAuthor().getNickname())
                .comments(article.getComments())
                .createAt(article.getCreateAt())
                .updateAt(article.getUpdateAt())
                .headImg(article.getHeadImg())
                .id(article.getId())
                .htmlContent(article.getHtmlContent())
                .markdownContent(article.getMarkdownContent())
                .marrow(article.getMarrow())
                .official(article.getOfficial())
                .tags(tags(article.getTags()))
                .title(article.getTitle())
                .top(article.getTop())
                .views(article.getViews())
                .approvals(article.getApprovals())
                .auditState(article.getAuditState().getValue())
                .build();
    }

    public static List<ArticleUserPageResponse> toArticleUserPageResponses(List<Article> articles) {
        return SafesUtil.ofList(articles).stream().map(article -> {
            return ArticleUserPageResponse.builder()
                    .auditState(article.getAuditState().getDesc())
                    .category(PostsCategoryEn.ARTICLE.getValue())
                    .categoryDesc(PostsCategoryEn.ARTICLE.getDesc())
                    .authorAvatar(article.getAuthor().getAvatar())
                    .authorId(article.getAuthor().getId())
                    .authorNickname(article.getAuthor().getNickname())
                    .comments(article.getComments())
                    .createAt(article.getCreateAt())
                    .updateAt(article.getUpdateAt())
                    .headImg(article.getHeadImg())
                    .id(article.getId())
                    .introduction(article.getMarkdownContent())
                    .marrow(article.getMarrow())
                    .official(article.getOfficial())
                    .tags(tags(article.getTags()))
                    .title(article.getTitle())
                    .top(article.getTop())
                    .views(article.getViews())
                    .approvals(article.getApprovals())
                    .build();
        }).collect(Collectors.toList());
    }

    private static List<TagVO> tags(Set<Tag> tags) {
        return SafesUtil.ofSet(tags).stream().map(tag -> {
            return TagVO.builder()
                    .id(tag.getId())
                    .name(tag.getName())
                    .build();
        }).collect(Collectors.toList());
    }

    public static Article toArticle(ArticleSaveArticleRequest request, ArticleType articleType, Set<Tag> selectTags, Boolean update, Long sort) {
        Article article = Article.builder()
                .headImg(request.getHeadImg())
                .type(articleType)
                .build();
        article.setId(request.getId());
        article.setContentType(ContentTypeEn.getEntity(request.getContentType()));
        article.setHtmlContent(request.getHtmlContent());
        article.setMarkdownContent(request.getMarkdownContent());
        article.setTags(selectTags);
        article.setTitle(request.getTitle());
        article.setAuthor(LoginUserContext.getUser());

        article.setAuditState(AuditStateEn.WAIT);

        if (!update) {
            article.setViews(0L);
            article.setApprovals(0L);
            article.setComments(0L);
            article.setMarrow(false);
            article.setOfficial(false);
            article.setSort(sort);
            article.setTop(false);
        }

        return article;
    }


    public static ArticleType toArticleType(ArticleAddTypeRequest request) {
        return ArticleType.builder()
                .scope(ArticleTypeScopeEn.getEntity(request.getScope()))
                .refCount(0L)
                .name(request.getName())
                .description(request.getDescription())
                .creatorId(LoginUserContext.getUser().getId())
                .auditState(AuditStateEn.WAIT)
                .build();
    }

    public static List<ArticleQueryTypesResponse> toArticleQueryTypesResponses(List<ArticleType> articleTypes) {
        List<ArticleQueryTypesResponse> responses = new ArrayList<>();

        SafesUtil.ofList(articleTypes).forEach(articleType -> responses.add(toArticleQueryTypesResponse(articleType)));

        return responses;
    }

    private static ArticleQueryTypesResponse toArticleQueryTypesResponse(ArticleType articleType) {
        return ArticleQueryTypesResponse.builder()
                .id(articleType.getId())
                .name(articleType.getName())
                .refCount(articleType.getRefCount())
                .description(articleType.getDescription())
                .scope(articleType.getScope().getDesc())
                .auditState(articleType.getAuditState().getDesc())
                .createAt(DateUtil.toyyyyMMddHHmmss(articleType.getCreateAt()))
                .updateAt(DateUtil.toyyyyMMddHHmmss(articleType.getUpdateAt()))
                .creatorId(articleType.getCreatorId())
                .build();
    }
}
