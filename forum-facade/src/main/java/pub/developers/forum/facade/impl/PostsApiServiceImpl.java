package pub.developers.forum.facade.impl;

import org.springframework.stereotype.Service;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.AdminBooleanRequest;
import pub.developers.forum.api.service.PostsApiService;
import pub.developers.forum.api.vo.PostsVO;
import pub.developers.forum.app.manager.PostsManager;
import pub.developers.forum.facade.support.ResultModelUtil;
import pub.developers.forum.facade.validator.ArticleValidator;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/25
 * @desc
 **/
@Service
public class PostsApiServiceImpl implements PostsApiService {

    @Resource
    private PostsManager postsManager;

    @Override
    public ResultModel delete(Long id) {
        postsManager.delete(id);

        return ResultModelUtil.success();
    }

    @Override
    public ResultModel<PageResponseModel<PostsVO>> pagePostsFood(PageRequestModel pageRequestModel) {
        return ResultModelUtil.success(postsManager.pagePostsFood(pageRequestModel));
    }

    @Override
    public ResultModel auditState(AdminBooleanRequest booleanRequest) {
        ArticleValidator.validatorBooleanRequest(booleanRequest);

        postsManager.auditState(booleanRequest);

        return ResultModelUtil.success();
    }

}
