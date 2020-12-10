package pub.developers.forum.portal.controller.rest;

import org.springframework.web.bind.annotation.*;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.service.ApprovalApiService;
import pub.developers.forum.common.constant.Constant;
import pub.developers.forum.portal.support.WebUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/1
 * @desc
 **/
@RestController
@RequestMapping("/approval-rest")
public class ApprovalRestController {

    @Resource
    private ApprovalApiService approvalApiService;

    @PostMapping("/create/{postsId}")
    public ResultModel create(@PathVariable Long postsId, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return approvalApiService.create(postsId);
    }

    @PostMapping("/delete/{postsId}")
    public ResultModel delete(@PathVariable Long postsId, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return approvalApiService.delete(postsId);
    }
}
