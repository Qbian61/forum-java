package pub.developers.forum.portal.controller.rest;

import org.springframework.web.bind.annotation.*;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.faq.FaqSaveFaqRequest;
import pub.developers.forum.api.request.faq.FaqSolutionRequest;
import pub.developers.forum.api.response.faq.FaqInfoResponse;
import pub.developers.forum.api.service.FaqApiService;
import pub.developers.forum.common.constant.Constant;
import pub.developers.forum.portal.support.WebUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/1
 * @desc
 **/
@RestController
@RequestMapping("/faq-rest")
public class FaqRestController {

    @Resource
    private FaqApiService faqApiService;

    @PostMapping("/save")
    public ResultModel<Long> save(@RequestBody FaqSaveFaqRequest faqRequest, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return faqApiService.saveFaq(faqRequest);
    }

    @PostMapping("/{id}")
    public ResultModel<FaqInfoResponse> get(@PathVariable("id") Long id, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));
        return faqApiService.info(id);
    }

    @PostMapping("/solution")
    public ResultModel solution(@RequestBody FaqSolutionRequest solutionRequest, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        return faqApiService.solution(solutionRequest);
    }
}
