package pub.developers.forum.portal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.article.ArticleUserPageRequest;
import pub.developers.forum.api.response.article.ArticleQueryTypesResponse;
import pub.developers.forum.api.response.article.ArticleUserPageResponse;
import pub.developers.forum.api.response.faq.FaqHotsResponse;
import pub.developers.forum.api.service.ArticleApiService;
import pub.developers.forum.api.service.FaqApiService;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.portal.support.GlobalViewConfig;
import pub.developers.forum.portal.support.WebConst;
import pub.developers.forum.portal.request.IndexRequest;
import pub.developers.forum.portal.support.WebUtil;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Qiangqiang.Bian
 * @create 2020/9/15
 * @desc
 **/
@Slf4j
@Controller
@RequestMapping("/")
public class IndexController {

    private static final String ALL_TYPE_NAME = "全部文章";

    @Resource
    private ArticleApiService articleApiService;

    @Value("${custom-config.view.index-page.sidebar-type-names}")
    private String sideBarTypeNames;

    @Resource
    private FaqApiService faqApiService;

    @Resource
    private WebUtil webUtil;

    @Resource
    private GlobalViewConfig globalViewConfig;

    @GetMapping
    public String index(IndexRequest request, Model model) {
        request.setType(ObjectUtils.isEmpty(request.getType()) ? ALL_TYPE_NAME : request.getType());

        model.addAttribute("currentDomain", WebConst.DOMAIN_ARTICLE);
        model.addAttribute("toast", request.getToast());

        model.addAttribute("carouselList", carouselList());
        model.addAttribute("sideBarTypes", sideBarTypes());

//        model.addAttribute("announcementList", sideBarTypes());
//        model.addAttribute("activityList", typeList(activityTypeName));
        model.addAttribute("hotFaqList", hotFaqList());
        model.addAttribute("typeList", typeList(request));
//        model.addAttribute("activityTypeName", activityTypeName);
//        model.addAttribute("announcementTypeName", announcementTypeName);


        // 文章列表请求参数
        ResultModel<PageResponseModel<ArticleUserPageResponse>> resultModel = userPage(request);
        if (resultModel.getSuccess() && !ObjectUtils.isEmpty(resultModel.getData())) {
            PageResponseModel<ArticleUserPageResponse> pageResponseModel = resultModel.getData();

            model.addAttribute("articleList", webUtil.buildArticles(pageResponseModel.getList()));
            model.addAttribute("pager", pager(request, pageResponseModel));
        } else {
            model.addAttribute("articleList", webUtil.buildArticles(new ArrayList<>()));

            PageResponseModel pageResponseModel = new PageResponseModel();
            pageResponseModel.setTotal(0L);
            model.addAttribute("pager", pager(request, pageResponseModel));
        }

        return "index";
    }

    private ResultModel<PageResponseModel<ArticleUserPageResponse>> userPage(IndexRequest request) {
        PageRequestModel<ArticleUserPageRequest> pageRequestModel = new PageRequestModel<>();
        pageRequestModel.setPageNo(request.getPageNo());
        pageRequestModel.setPageSize(globalViewConfig.getPageSize());
        pageRequestModel.setFilter(ArticleUserPageRequest.builder()
                .typeName(ALL_TYPE_NAME.equals(request.getType()) ? null : request.getType())
                .build());
        return articleApiService.userPage(pageRequestModel);
    }

    private Map<String, Object> pager(IndexRequest request, PageResponseModel pageResponseModel) {
        String queryPath = "?type=" + request.getType() + "&" + WebConst.PAGE_NO_NAME + "=";
        return webUtil.buildPager(request.getPageNo(), queryPath, pageResponseModel);
    }

    private List<Map<String, Object>> carouselList() {
        List<Map<String, Object>> carouselList = new ArrayList<>();

        Map<String, Object> carousel0 = new HashMap<>();
        carousel0.put("img", "https://static.runoob.com/images/mix/img_fjords_wide.jpg");
        carousel0.put("title", "第一张图片");
        carousel0.put("href", "https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#link-urls");
        carouselList.add(carousel0);

        Map<String, Object> carousel1 = new HashMap<>();
        carousel1.put("img", "https://static.runoob.com/images/mix/img_nature_wide.jpg");
        carousel1.put("title", "Second slide label");
        carousel1.put("href", "https://v4.bootcss.com/docs/utilities/flex/");
        carouselList.add(carousel1);

        Map<String, Object> carousel2 = new HashMap<>();
        carousel2.put("img", "https://static.runoob.com/images/mix/img_mountains_wide.jpg");
        carousel2.put("title", "第三章图片");
        carousel2.put("href", "https://translate.google.cn/#view=home&op=translate&sl=auto&tl=en&text=%E7%AB%99%E5%86%85%E4%BF%A1");
        carouselList.add(carousel2);

        return carouselList;
    }

    private List<Map<String, Object>> hotFaqList() {
        List<Map<String, Object>> postsList = new ArrayList<>();

        ResultModel<List<FaqHotsResponse>> resultModel = faqApiService.hots(10);
        if (!resultModel.getSuccess()) {
            return postsList;
        }

        SafesUtil.ofList(resultModel.getData()).forEach(faqHotsResponse -> {
            Map<String, Object> posts = new HashMap<>();
            posts.put("id", faqHotsResponse.getId());
            posts.put("title", faqHotsResponse.getTitle());
            posts.put("createdAt", faqHotsResponse.getCreateAt());
            postsList.add(posts);
        });

        return postsList;
    }

    private List<Map<String, Object>> sideBarTypes() {
        List<Map<String, Object>> res = new ArrayList<>();

        if (ObjectUtils.isEmpty(sideBarTypeNames)) {
            return res;
        }

        String[] types = sideBarTypeNames.split(",");
        for (String typeName : types) {
            if (ObjectUtils.isEmpty(typeName)) {
                continue;
            }
            Map<String, Object> type = new HashMap<>();
            type.put("name", typeName);
            type.put("postsList", typeList(typeName));
            res.add(type);
        }

        return res;
    }

    private List<Map<String, Object>> typeList(String typeName) {
        List<Map<String, Object>> postsList = new ArrayList<>();
        if (ObjectUtils.isEmpty(typeName)) {
            return postsList;
        }

        PageRequestModel<ArticleUserPageRequest> pageRequestModel = new PageRequestModel<>();
        pageRequestModel.setPageNo(1);
        pageRequestModel.setPageSize(10);
        pageRequestModel.setFilter(ArticleUserPageRequest.builder()
                .typeName(typeName)
                .build());
        ResultModel<PageResponseModel<ArticleUserPageResponse>> resultModel = articleApiService.userPage(pageRequestModel);
        if (!resultModel.getSuccess()) {
            return postsList;
        }

        PageResponseModel<ArticleUserPageResponse> pageResponseModel = resultModel.getData();
        SafesUtil.ofList(pageResponseModel.getList()).forEach(articleUserPageResponse -> {
            Map<String, Object> posts = new HashMap<>();
            posts.put("id", articleUserPageResponse.getId());
            posts.put("title", articleUserPageResponse.getTitle());
            posts.put("createdAt", articleUserPageResponse.getCreateAt());
            postsList.add(posts);
        });

        return postsList;
    }

    private List<Map<String, Object>> typeList(IndexRequest request) {
        List<Map<String, Object>> typeList = new ArrayList<>();
        Long allRefCount = 0L;

        Map<String, Object> all = new HashMap<>();
        all.put("name", ALL_TYPE_NAME);
        all.put("selected", request.getType().equals(ALL_TYPE_NAME));
        typeList.add(all);

        ResultModel<List<ArticleQueryTypesResponse>> resultModel = articleApiService.queryAllTypes();
        if (resultModel.getSuccess() && !ObjectUtils.isEmpty(resultModel.getData())) {
            List<ArticleQueryTypesResponse> responses = resultModel.getData();
            for (ArticleQueryTypesResponse response : responses) {
                if (response.getRefCount().equals(0L)) {
                    continue;
                }

                allRefCount += response.getRefCount();

                Map<String, Object> type = new HashMap<>();
                type.put("name", response.getName());
                type.put("refCount", response.getRefCount());
                type.put("selected", request.getType().equals(response.getName()));
                typeList.add(type);
            }
        }

        all.put("refCount", allRefCount);

        return typeList;
    }
}
