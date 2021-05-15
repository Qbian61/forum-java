package pub.developers.forum.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.message.MessagePageRequest;
import pub.developers.forum.api.response.message.MessagePageResponse;
import pub.developers.forum.api.service.MessageApiService;
import pub.developers.forum.common.constant.Constant;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.portal.request.MessageRequest;
import pub.developers.forum.common.support.GlobalViewConfig;
import pub.developers.forum.portal.support.ViewException;
import pub.developers.forum.portal.support.WebConst;
import pub.developers.forum.portal.support.WebUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/5
 * @desc
 **/
@Controller
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageApiService messageApiService;

    @Resource
    private GlobalViewConfig globalViewConfig;

    @Resource
    private WebUtil webUtil;

    private static final String FOLLOW_USER = "用户关注";

    private static final String COMMENT_ARTICLE = "评论文章";
    private static final String APPROVAL_ARTICLE = "点赞文章";

    private static final String COMMENT_FAQ = "评论问答";
    private static final String APPROVAL_FAQ = "关注问答";


    private static final List<String> TYPE_LIST = Arrays.asList(FOLLOW_USER, COMMENT_ARTICLE, APPROVAL_ARTICLE, COMMENT_FAQ, APPROVAL_FAQ);

    @GetMapping
    public String index(MessageRequest messageRequest, Model model, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        if (ObjectUtils.isEmpty(messageRequest.getType())) {
            messageRequest.setType(TYPE_LIST.get(0));
        }
        model.addAttribute("typeList", typeList(messageRequest));

        ResultModel<PageResponseModel<MessagePageResponse>> responseModelResultModel = messagePageList(messageRequest);
        if (!responseModelResultModel.getSuccess()) {
            throw ViewException.build(responseModelResultModel.getMessage());
        }

        if (!ObjectUtils.isEmpty(responseModelResultModel.getData())) {
            PageResponseModel<MessagePageResponse> pageResponseModel = responseModelResultModel.getData();

            model.addAttribute("messageList", messageList(pageResponseModel.getList()));
            model.addAttribute("pager", pager(messageRequest, pageResponseModel));
        } else {
            model.addAttribute("messageList", messageList(new ArrayList<>()));

            PageResponseModel pageResponseModel = new PageResponseModel();
            pageResponseModel.setTotal(0L);
            model.addAttribute("pager", pager(messageRequest, pageResponseModel));
        }

        return "message";
    }

    private List<Map<String, Object>> messageList(List<MessagePageResponse> responses) {
        List<Map<String, Object>> res = new ArrayList<>();

        SafesUtil.ofList(responses).forEach(response -> {
            Map<String, Object> entity = new HashMap<>();
            entity.put("id", response.getId());
            entity.put("read", response.getRead());
            entity.put("senderAvatar", response.getSenderAvatar());
            entity.put("sender", response.getSender());
            entity.put("senderName", response.getSenderName());
            entity.put("typeDesc", response.getTypeDesc());
            entity.put("title", response.getTitle());
            entity.put("infoAction", infoAction(response));
            entity.put("createAt", response.getCreateAt());

            res.add(entity);
        });

        return res;
    }

    private String infoAction(MessagePageResponse response) {
        String pre = "/user/";
        if (COMMENT_ARTICLE.equals(response.getTypeDesc())
                || APPROVAL_ARTICLE.equals(response.getTypeDesc())) {
            pre = "/article/";
        }
        if (COMMENT_FAQ.equals(response.getTypeDesc())
                || APPROVAL_FAQ.equals(response.getTypeDesc())) {
            pre = "/faq/";
        }

        return pre + response.getInfoId();
    }

    private Map<String, Object> pager(MessageRequest messageRequest, PageResponseModel pageResponseModel) {
        String queryPath = "?type=" + messageRequest.getType() + "&" + WebConst.PAGE_NO_NAME + "=";
        return webUtil.buildPager(messageRequest.getPageNo(), queryPath, pageResponseModel);
    }

    private ResultModel<PageResponseModel<MessagePageResponse>> messagePageList(MessageRequest pageRequest) {
        PageRequestModel<MessagePageRequest> pageRequestModel = new PageRequestModel<>();
        pageRequestModel.setPageSize(globalViewConfig.getPageSize());
        pageRequestModel.setPageNo(pageRequest.getPageNo());
        pageRequestModel.setFilter(MessagePageRequest.builder()
                .typeDesc(pageRequest.getType())
                .build());

        return messageApiService.page(pageRequestModel);
    }

    private List<Map<String, Object>> typeList(MessageRequest messageRequest) {
        List<Map<String, Object>> typeList = new ArrayList<>();

        TYPE_LIST.forEach(type -> {
            Map<String, Object> all = new HashMap<>();
            all.put("name", type);
            all.put("selected", messageRequest.getType().equals(type));
            typeList.add(all);
        });

        return typeList;
    }
}
