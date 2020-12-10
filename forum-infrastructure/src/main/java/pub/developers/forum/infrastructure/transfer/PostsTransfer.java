package pub.developers.forum.infrastructure.transfer;

import org.springframework.util.ObjectUtils;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.enums.ContentTypeEn;
import pub.developers.forum.common.enums.PostsCategoryEn;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.*;
import pub.developers.forum.infrastructure.dal.dataobject.PostsDO;
import pub.developers.forum.infrastructure.dal.dataobject.TagPostsMappingDO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
public class PostsTransfer {

    public static List<Posts> toPostsList(List<PostsDO> postsDOS
            , List<User> users
            , List<TagPostsMappingDO> tagPostsMappingDOS
            , List<Tag> tags) {
        List<Posts> res = new ArrayList<>();

        SafesUtil.ofList(postsDOS).forEach(postsDO -> {
            SafesUtil.ofList(users).forEach(user -> {
                if (!postsDO.getAuthorId().equals(user.getId())) {
                    return;
                }

                Posts posts = Posts.builder()
                        .type(null)
                        .headImg(postsDO.getHeadImg())
                        .marrow(postsDO.getMarrow())
                        .official(postsDO.getOfficial())
                        .sort(postsDO.getSort())
                        .top(postsDO.getTop())
                        .solutionId(postsDO.getCommentId())
                        .build();
                pickBasePosts(posts, postsDO, user, toTags(postsDO, tagPostsMappingDOS, tags));

                res.add(posts);
            });
        });

        return res;
    }

    public static List<Faq> toFaqs(List<PostsDO> postsDOS
            , List<User> users
            , List<TagPostsMappingDO> tagPostsMappingDOS
            , List<Tag> tags) {
        List<Faq> faqs = new ArrayList<>();

        SafesUtil.ofList(postsDOS).forEach(postsDO -> {
            SafesUtil.ofList(users).forEach(user -> {
                if (!postsDO.getAuthorId().equals(user.getId())) {
                    return;
                }

                faqs.add(toFaq(postsDO, user, toTags(postsDO, tagPostsMappingDOS, tags)));
            });
        });

        return faqs;
    }

    public static List<Article> toArticles(List<PostsDO> postsDOS
            , List<User> users
            , List<TagPostsMappingDO> tagPostsMappingDOS
            , List<Tag> tags) {
        List<Article> articles = new ArrayList<>();

        SafesUtil.ofList(postsDOS).forEach(postsDO -> {
            SafesUtil.ofList(users).forEach(user -> {
                if (!postsDO.getAuthorId().equals(user.getId())) {
                    return;
                }

                articles.add(toArticle(postsDO, user, null, toTags(postsDO, tagPostsMappingDOS, tags)));
            });
        });

        return articles;
    }

    private static List<Tag> toTags(PostsDO postsDO, List<TagPostsMappingDO> tagPostsMappingDOS, List<Tag> tags) {
        List<Tag> tagList = new ArrayList<>();

        SafesUtil.ofList(tagPostsMappingDOS).forEach(tagPostsMappingDO -> {
            if (!tagPostsMappingDO.getPostsId().equals(postsDO.getId())) {
                return;
            }

            SafesUtil.ofList(tags).forEach(tag -> {
                if (tag.getId().equals(tagPostsMappingDO.getTagId())) {
                    tagList.add(tag);
                }
            });
        });

        return tagList;
    }

    public static PostsDO toPostsDO(Article article) {
        PostsDO postsDO = PostsDO.builder()
                .category(PostsCategoryEn.ARTICLE.getValue())
                .commentId(0L)
                .headImg(article.getHeadImg())
                .marrow(article.getMarrow())
                .official(article.getOfficial())
                .sort(article.getSort())
                .top(article.getTop())
                .typeId(article.getType().getId())
                .build();

        pickPostsDO(postsDO, article);

        return postsDO;
    }

    public static PostsDO toPostsDO(Faq faq) {
        PostsDO postsDO = PostsDO.builder()
                .category(PostsCategoryEn.FAQ.getValue())
                .commentId(faq.getSolutionId())
                .headImg("")
                .marrow(false)
                .official(false)
                .sort(1000L)
                .top(false)
                .typeId(0L)
                .build();

        pickPostsDO(postsDO, faq);

        return postsDO;
    }

    private static void pickPostsDO(PostsDO postsDO, BasePosts basePosts) {
        postsDO.initBase();
        postsDO.setId(basePosts.getId());
        postsDO.setApprovals(basePosts.getApprovals());
        postsDO.setAuditState(basePosts.getAuditState().getValue());
        postsDO.setAuthorId(basePosts.getAuthor().getId());
        postsDO.setComments(basePosts.getComments());
        postsDO.setContentType(basePosts.getContentType().getValue());
        postsDO.setHtmlContent(basePosts.getHtmlContent());
        postsDO.setMarkdownContent(basePosts.getMarkdownContent());
        postsDO.setTitle(basePosts.getTitle());
        postsDO.setViews(basePosts.getViews());
    }

    public static Article toArticle(PostsDO postsDO, User user, ArticleType articleType, List<Tag> tags) {
        Article article = Article.builder()
                .type(articleType)
                .headImg(postsDO.getHeadImg())
                .marrow(postsDO.getMarrow())
                .official(postsDO.getOfficial())
                .sort(postsDO.getSort())
                .top(postsDO.getTop())
                .build();

        pickBasePosts(article, postsDO, user, tags);

        return article;
    }

    public static Faq toFaq(PostsDO postsDO, User user, List<Tag> tags) {
        Faq faq = Faq.builder()
                .solutionId(postsDO.getCommentId())
                .build();

        pickBasePosts(faq, postsDO, user, tags);

        return faq;
    }

    public static List<BasePosts> toBasePostsList(List<PostsDO> postsDOs) {
        List<BasePosts> res = new ArrayList<>();

        if (ObjectUtils.isEmpty(postsDOs)) {
            return res;
        }

        SafesUtil.ofList(postsDOs).forEach(postsDO -> {
            res.add(toBasePosts(postsDO));
        });

        return res;
    }

    public static BasePosts toBasePosts(PostsDO postsDO) {
        if (ObjectUtils.isEmpty(postsDO)) {
            return null;
        }

        BasePosts basePosts = new BasePosts();

        pickBasePosts(basePosts, postsDO, null, new ArrayList<>());

        return basePosts;
    }

    private static void pickBasePosts(BasePosts basePosts, PostsDO postsDO, User user, List<Tag> tags) {
        basePosts.setTags(new HashSet<>(tags));
        basePosts.setAuthor(user);

        basePosts.setUpdateAt(postsDO.getUpdateAt());
        basePosts.setCreateAt(postsDO.getCreateAt());
        basePosts.setId(postsDO.getId());

        basePosts.setCategory(PostsCategoryEn.valueOf(postsDO.getCategory()));
        basePosts.setTitle(postsDO.getTitle());
        basePosts.setAuditState(AuditStateEn.getEntity(postsDO.getAuditState()));
        basePosts.setContentType(ContentTypeEn.getEntity(postsDO.getContentType()));
        basePosts.setMarkdownContent(postsDO.getMarkdownContent());
        basePosts.setHtmlContent(postsDO.getHtmlContent());
        basePosts.setComments(postsDO.getComments());
        basePosts.setApprovals(postsDO.getApprovals());
        basePosts.setViews(postsDO.getViews());
        basePosts.setTypeId(postsDO.getTypeId());
        basePosts.setAuthorId(postsDO.getAuthorId());

        basePosts.setOfficial(postsDO.getOfficial());
        basePosts.setMarrow(postsDO.getMarrow());
        basePosts.setTop(postsDO.getTop());
        basePosts.setSort(postsDO.getSort());
    }

}
