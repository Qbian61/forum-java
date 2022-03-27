package pub.developers.forum.portal.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.response.article.ArticleInfoResponse;
import pub.developers.forum.api.response.article.ArticleUserPageResponse;
import pub.developers.forum.api.response.config.ConfigResponse;
import pub.developers.forum.api.response.faq.FaqInfoResponse;
import pub.developers.forum.api.response.faq.FaqUserPageResponse;
import pub.developers.forum.api.response.tag.TagQueryResponse;
import pub.developers.forum.api.service.TagApiService;
import pub.developers.forum.api.vo.PostsVO;
import pub.developers.forum.common.constant.Constant;
import pub.developers.forum.common.enums.ConfigTypeEn;
import pub.developers.forum.common.support.GlobalViewConfig;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.common.support.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/25
 * @desc
 **/
@Data
@ConfigurationProperties(prefix = "custom-config.upload-file.qiniu")
@Component
public class WebUtil {

    @Resource
    private GlobalViewConfig globalViewConfig;

    @Resource
    private TagApiService tagApiService;

    private String accessDomain;

    public static String cookieGetSid(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (!ObjectUtils.isEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (WebConst.COOKIE_SID_KEY.equals(cookie.getName()) && !ObjectUtils.isEmpty(cookie.getValue())) {
                    return cookie.getValue();
                }
            }
        }

        String headerSid = request.getHeader(Constant.REQUEST_HEADER_TOKEN_KEY);
        if (!ObjectUtils.isEmpty(headerSid)) {
            return headerSid;
        }

        String querySid = request.getParameter(Constant.REQUEST_QUERY_TOKEN_KEY);
        if (!ObjectUtils.isEmpty(querySid)) {
            return querySid;
        }

        return null;
    }

    public static void cookieAddSid(HttpServletResponse response, String sid) {
        Cookie cookie = new Cookie(WebConst.COOKIE_SID_KEY, sid);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void cookieDelSid(HttpServletResponse response) {
        Cookie cookie = new Cookie(WebConst.COOKIE_SID_KEY, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static String requestIp(HttpServletRequest request) {
        String ret = request.getHeader("X-forwarded-for");
        if(ObjectUtils.isEmpty(ret)) {
            ret = request.getHeader("X-Real-IP");
        }

        return ObjectUtils.isEmpty(ret) ? request.getRemoteAddr() : ret.split(",")[0];
    }

    public static String requestUa(HttpServletRequest request) {
        String value = request.getHeader("User-Agent");
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }

        return value;
    }

    public List<Map<String, Object>> relatedPosts(Set<Long> tagIds) {
        List<Map<String, Object>> res = new ArrayList<>();

        PageRequestModel<Set<Long>> pageRequestModel = new PageRequestModel<>();
        pageRequestModel.setPageNo(1);
        pageRequestModel.setPageSize(globalViewConfig.getPageSize());
        pageRequestModel.setFilter(tagIds);

        ResultModel<PageResponseModel<PostsVO>> resultModel = tagApiService.pagePostsByTagIds(pageRequestModel);
        if (resultModel.getSuccess() && !ObjectUtils.isEmpty(resultModel.getData())) {
            SafesUtil.ofList(resultModel.getData().getList()).forEach(response -> {
                Map<String, Object> posts = new HashMap<>();
                posts.put("id", response.getId());
                posts.put("category", response.getCategory());
                posts.put("categoryDesc", response.getCategoryDesc());
                posts.put("title", response.getTitle());
                posts.put("createdAt", dateShow(response.getCreateAt()));

                posts.put("views", response.getViews());
                posts.put("approvals", response.getApprovals());
                posts.put("comments", response.getComments());

                posts.put("authorId", response.getAuthorId());
                posts.put("authorHeadImg", response.getAuthorAvatar());
                posts.put("authorName", response.getAuthorNickname());

                List<Map<String, Object>> tagList = new ArrayList<>();
                SafesUtil.ofList(response.getTags()).forEach(tagVO -> {
                    Map<String, Object> tag = new HashMap<>();
                    tag.put("id", tagVO.getId());
                    tag.put("name", tagVO.getName());
                    tagList.add(tag);
                });
                posts.put("tagList", tagList);

                res.add(posts);
            });
        }

        return res;
    }

    public List<Map<String, Object>> usedTags() {
        List<Map<String, Object>> res = new ArrayList<>();

        ResultModel<List<TagQueryResponse>> resultModel = tagApiService.queryAllRef();
        if (!resultModel.getSuccess()) {
            return res;
        }

        SafesUtil.ofList(resultModel.getData()).forEach(tagQueryResponse -> {
            Map<String, Object> tag = new HashMap<>();
            tag.put("name", tagQueryResponse.getName());
            tag.put("color", StringUtil.getColor(tagQueryResponse.getName()));
            res.add(tag);
        });

        return res;
    }

    public Map<String, Object> buildPostsInfo(ArticleInfoResponse articleInfoResponse) {
        Map<String, Object> posts = new HashMap<>();
        posts.put("title", articleInfoResponse.getTitle());
        posts.put("authorName", articleInfoResponse.getAuthorNickname());
        posts.put("authorId", articleInfoResponse.getAuthorId());
        posts.put("authorAvatar", articleInfoResponse.getAuthorAvatar());
        posts.put("createAt", dateShow(articleInfoResponse.getCreateAt()));
        posts.put("updateAt", dateShow(articleInfoResponse.getUpdateAt()));
        posts.put("views", articleInfoResponse.getViews());
        posts.put("approvals", articleInfoResponse.getApprovals());
        posts.put("comments", articleInfoResponse.getComments());
        posts.put("content", htmlContent(articleInfoResponse.getHtmlContent()));
        posts.put("id", articleInfoResponse.getId());

        posts.put("tags", articleInfoResponse.getTags());

        return posts;
    }

    public Map<String, Object> buildPostsInfo(FaqInfoResponse faqInfoResponse) {
        Map<String, Object> posts = new HashMap<>();
        posts.put("title", faqInfoResponse.getTitle());
        posts.put("authorName", faqInfoResponse.getAuthorNickname());
        posts.put("authorId", faqInfoResponse.getAuthorId());
        posts.put("authorAvatar", faqInfoResponse.getAuthorAvatar());
        posts.put("createAt", dateShow(faqInfoResponse.getCreateAt()));
        posts.put("updateAt", dateShow(faqInfoResponse.getUpdateAt()));
        posts.put("views", faqInfoResponse.getViews());
        posts.put("approvals", faqInfoResponse.getApprovals());
        posts.put("comments", faqInfoResponse.getComments());
        posts.put("content", htmlContent(faqInfoResponse.getHtmlContent()));
        posts.put("id", faqInfoResponse.getId());

        posts.put("tags", faqInfoResponse.getTags());

        return posts;
    }

    public List<Map<String, Object>> buildPostsList(List<PostsVO> responses) {
        List<Map<String, Object>> articleList = new ArrayList<>();

        SafesUtil.ofList(responses).forEach(response -> {
            Map<String, Object> article = new HashMap<>();
            article.put("category", response.getCategory());
            article.put("categoryDesc", response.getCategoryDesc());
            article.put("id", response.getId());
            article.put("title", response.getTitle());
            article.put("createdAt", dateShow(response.getCreateAt()));
            article.put("desc", getIntroduction(response.getIntroduction(), !ObjectUtils.isEmpty(response.getHeadImg())));
            article.put("headImg", headImg(response.getHeadImg()));

            article.put("official", response.getOfficial());
            article.put("top", response.getTop());
            article.put("marrow", response.getMarrow());

            article.put("views", response.getViews());
            article.put("approvals", response.getApprovals());
            article.put("comments", response.getComments());

            article.put("authorId", response.getAuthorId());
            article.put("authorHeadImg", response.getAuthorAvatar());
            article.put("authorName", response.getAuthorNickname());

            List<Map<String, Object>> tagList = new ArrayList<>();
            SafesUtil.ofList(response.getTags()).forEach(tagVO -> {
                Map<String, Object> tag = new HashMap<>();
                tag.put("id", tagVO.getId());
                tag.put("name", tagVO.getName());
                tagList.add(tag);
            });
            article.put("tagList", tagList);

            article.put("isSolution", !ObjectUtils.isEmpty(response.getSolution()));
            if (!ObjectUtils.isEmpty(response.getSolution())) {
                article.put("solution", getIntroduction(response.getSolution().getContent(), false));
            }

            articleList.add(article);
        });

        return articleList;
    }

    public List<Map<String, Object>> buildArticles(List<ArticleUserPageResponse> responses) {
        List<Map<String, Object>> articleList = new ArrayList<>();

        SafesUtil.ofList(responses).forEach(response -> {
            Map<String, Object> article = new HashMap<>();
            article.put("category", response.getCategory());
            article.put("categoryDesc", response.getCategoryDesc());
            article.put("id", response.getId());
            article.put("title", response.getTitle());
            article.put("createdAt", dateShow(response.getCreateAt()));
            article.put("desc", getIntroduction(response.getIntroduction(), !ObjectUtils.isEmpty(response.getHeadImg())));
            article.put("headImg", headImg(response.getHeadImg()));

            article.put("official", response.getOfficial());
            article.put("top", response.getTop());
            article.put("marrow", response.getMarrow());

            article.put("views", response.getViews());
            article.put("approvals", response.getApprovals());
            article.put("comments", response.getComments());

            article.put("authorId", response.getAuthorId());
            article.put("authorHeadImg", response.getAuthorAvatar());
            article.put("authorName", response.getAuthorNickname());

            List<Map<String, Object>> tagList = new ArrayList<>();
            SafesUtil.ofList(response.getTags()).forEach(tagVO -> {
                Map<String, Object> tag = new HashMap<>();
                tag.put("id", tagVO.getId());
                tag.put("name", tagVO.getName());
                tagList.add(tag);
            });
            article.put("tagList", tagList);

            articleList.add(article);
        });

        return articleList;
    }

    public List<Map<String, Object>> buildFaqs(List<FaqUserPageResponse> responses) {
        List<Map<String, Object>> articleList = new ArrayList<>();

        SafesUtil.ofList(responses).forEach(response -> {
            Map<String, Object> article = new HashMap<>();
            article.put("id", response.getId());
            article.put("category", response.getCategory());
            article.put("categoryDesc", response.getCategoryDesc());
            article.put("title", response.getTitle());
            article.put("createdAt", dateShow(response.getCreateAt()));

            article.put("isSolution", !ObjectUtils.isEmpty(response.getSolution()));
            if (!ObjectUtils.isEmpty(response.getSolution())) {
                article.put("solution", getIntroduction(response.getSolution().getContent(), false));
            }

            article.put("views", response.getViews());
            article.put("approvals", response.getApprovals());
            article.put("comments", response.getComments());

            article.put("authorId", response.getAuthorId());
            article.put("authorHeadImg", response.getAuthorAvatar());
            article.put("authorName", response.getAuthorNickname());

            List<Map<String, Object>> tagList = new ArrayList<>();
            SafesUtil.ofList(response.getTags()).forEach(tagVO -> {
                Map<String, Object> tag = new HashMap<>();
                tag.put("id", tagVO.getId());
                tag.put("name", tagVO.getName());
                tagList.add(tag);
            });
            article.put("tagList", tagList);

            articleList.add(article);
        });

        return articleList;
    }

    public List<Map<String, Object>> carouselList(List<ConfigResponse> configResponses, ConfigTypeEn configType) {
        List<Map<String, Object>> carouselList = new ArrayList<>();

        SafesUtil.ofList(configResponses).forEach(configResponse -> {
            if (!configType.getDesc().equals(configResponse.getType())) {
                return;
            }

            JSONObject jsonObject = JSON.parseObject(configResponse.getContent());

            Map<String, Object> carousel = new HashMap<>();
            carousel.put("imgUrl", getImgUrl(jsonObject.getString("imgUrl")));
            carousel.put("name", configResponse.getName());
            carousel.put("actionUrl", jsonObject.get("actionUrl"));

            carouselList.add(carousel);
        });

        return carouselList;
    }

    public Map<String, Object> buildPager(Integer pageNum, String queryPath, PageResponseModel pageResponseModel) {
        Long pageNo = Long.valueOf(pageNum);

        Map<String, Object> pager = new HashMap<>();

        Long totalPage = pageResponseModel.getTotal() / globalViewConfig.getPageSize();
        if (pageResponseModel.getTotal() % globalViewConfig.getPageSize() != 0) {
            totalPage += 1;
        }

        boolean preMore = false;
        Long start = 0L;
        if (pageNo > 3) {
            preMore = true;
            start = pageNo - 2;
        }

        boolean nextMore = false;
        Long end = totalPage;
        if (totalPage - pageNo > 2) {
            nextMore = true;
            end = pageNo + 1;
        }

        pager.put("preMore", preMore);
        pager.put("nextMore", nextMore);
        pager.put("firstNoHref", queryPath + 1);
        pager.put("lastNoHref", queryPath + totalPage);
        pager.put("lastNo", totalPage);

        List<Map<String, Object>> pages = new ArrayList<>();
        for (long i = start; i < end; i ++) {
            Map<String, Object> page = new HashMap<>();
            long no = i + 1;
            page.put("no", no);
            page.put("href", queryPath + no);
            pages.add(page);
        }
        pager.put("pages", pages);
        pager.put("current", pageNo);

        boolean first = pageNo.equals(1L);
        boolean last = totalPage == 0L || pageNo.equals(totalPage);
        pager.put("first", first);
        pager.put("last", last);

        pager.put("pre", queryPath + (first ? pageNo : pageNo - 1));
        pager.put("next", queryPath + (last ? pageNo : pageNo + 1));

        return pager;
    }

    private String headImg(String headImgs) {
        if (ObjectUtils.isEmpty(headImgs)) {
            return "";
        }
        List<String> headImgList = JSON.parseArray(headImgs, String.class);
        if (ObjectUtils.isEmpty(headImgList)) {
            return "";
        }

        Map<String, String> img = JSON.parseObject(headImgList.get(0), Map.class);

        String url = img.get("url");

        if (!url.contains("?") && !ObjectUtils.isEmpty(globalViewConfig.getCdnImgStyle())) {
            return url + globalViewConfig.getCdnImgStyle();
        }
        return url;
    }

    public String getImgUrl(String imgUrl) {
        if (ObjectUtils.isEmpty(imgUrl) || !imgUrl.startsWith(accessDomain) || imgUrl.contains("?")) {
            return imgUrl;
        }
        return imgUrl + globalViewConfig.getCdnImgStyle();
    }

    private String htmlContent(String htmlContent) {
        String cdnImgStyle = globalViewConfig.getCdnImgStyle();
        if (ObjectUtils.isEmpty(cdnImgStyle)) {
            return htmlContent;
        }

        Document document = Jsoup.parse(htmlContent);
        Elements imgs = document.getElementsByTag("img");
        for (Element img: imgs) {
            String src = img.attr("src");
            img.attr("src", getImgUrl(src));
        }

        Elements scripts = document.getElementsByTag("script");
        for (Element script : scripts) {
            script.remove();
        }

        return document.toString();
    }

    private static final List<String> MARKDOWN_IDENTIFIERS = Arrays.asList("\\*", "#", "\\+\\+", "~", "==",
            "^", ":::", "hljs-left", "hljs-center", "hljs-right", ">", "- ", "```", " ");

    private static String getIntroduction(String content, Boolean hasHeadImg) {
        for (String identifier : MARKDOWN_IDENTIFIERS) {
            content = content.replaceAll(identifier, "");
        }

        String result;
        if (hasHeadImg) {
            if (content.length() > 250) {
                result = content.substring(0, 250) + "...";
            } else {
                result = content;
            }
        } else {
            if (content.length() > 300) {
                result = content.substring(0, 300) + "...";
            } else {
                result = content;
            }
        }
        return result;
    }

    private static final Long ONE_SECOND_TIME = 1000L;
    private static final Long ONE_MINUTE_TIME = ONE_SECOND_TIME * 60;
    private static final Long ONE_HOUR_TIME = ONE_MINUTE_TIME * 60;
    private static final Long ONE_DAY_TIME = ONE_HOUR_TIME * 24;
    private static final Long ONE_MONTH_TIME = ONE_DAY_TIME * 30;

    private static String dateShow(Date date) {
        Long timeout = System.currentTimeMillis() - date.getTime();

        if (timeout > ONE_MONTH_TIME) {
            return new SimpleDateFormat("yyyy年MM月dd日").format(date);
        } else if (timeout > ONE_DAY_TIME) {
            return calculateTime(timeout, ONE_DAY_TIME) + "天前";
        } else if (timeout > ONE_HOUR_TIME) {
            return calculateTime(timeout, ONE_HOUR_TIME) + "小时前";
        } else if (timeout > ONE_MINUTE_TIME) {
            return calculateTime(timeout, ONE_MINUTE_TIME) + "分钟前";
        } else if (timeout > ONE_SECOND_TIME) {
            return calculateTime(timeout, ONE_SECOND_TIME) + "秒前";
        }

        return new SimpleDateFormat("yyyy年MM月dd日").format(date);
    }

    private static Long calculateTime(Long l1, Long l2) {
        return l1 % l2 > 0 ? l1 / l2 + 1 : l1 / l2;
    }

}
