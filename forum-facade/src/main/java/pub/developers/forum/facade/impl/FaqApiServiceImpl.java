package pub.developers.forum.facade.impl;

import org.springframework.stereotype.Service;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.faq.*;
import pub.developers.forum.api.response.faq.FaqHotsResponse;
import pub.developers.forum.api.response.faq.FaqInfoResponse;
import pub.developers.forum.api.response.faq.FaqUserPageResponse;
import pub.developers.forum.api.service.FaqApiService;
import pub.developers.forum.app.manager.FaqManager;
import pub.developers.forum.app.support.IsLogin;
import pub.developers.forum.common.enums.UserRoleEn;
import pub.developers.forum.facade.support.ResultModelUtil;
import pub.developers.forum.facade.validator.FaqValidator;
import pub.developers.forum.facade.validator.PageRequestModelValidator;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/1
 * @desc
 **/
@Service
public class FaqApiServiceImpl implements FaqApiService {

    @Resource
    private FaqManager faqManager;

    @Override
    public ResultModel<Long> saveFaq(FaqSaveFaqRequest request) {
        FaqValidator.saveFaq(request);

        return ResultModelUtil.success(faqManager.saveFaq(request));
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    @Override
    public ResultModel<PageResponseModel<FaqUserPageResponse>> adminPage(PageRequestModel<FaqAdminPageRequest> pageRequestModel) {
        PageRequestModelValidator.validator(pageRequestModel);

        return ResultModelUtil.success(faqManager.adminPage(pageRequestModel));
    }

    @Override
    public ResultModel<PageResponseModel<FaqUserPageResponse>> userPage(PageRequestModel<FaqUserPageRequest> pageRequestModel) {
        PageRequestModelValidator.validator(pageRequestModel);

        return ResultModelUtil.success(faqManager.userPage(pageRequestModel));
    }

    @Override
    public ResultModel<PageResponseModel<FaqUserPageResponse>> authorPage(PageRequestModel<FaqAuthorPageRequest> pageRequestModel) {
        PageRequestModelValidator.validator(pageRequestModel);

        return ResultModelUtil.success(faqManager.authorPage(pageRequestModel));
    }

    @Override
    public ResultModel<FaqInfoResponse> info(Long id) {
        return ResultModelUtil.success(faqManager.info(id));
    }

    @Override
    public ResultModel<List<FaqHotsResponse>> hots(int size) {
        return ResultModelUtil.success(faqManager.hots(size));
    }

    @Override
    public ResultModel solution(FaqSolutionRequest request) {
        FaqValidator.solution(request);

        faqManager.solution(request);

        return ResultModelUtil.success();
    }
}
