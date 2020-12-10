package pub.developers.forum.app.manager;

import org.springframework.stereotype.Component;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.vo.PostsVO;
import pub.developers.forum.app.support.IsLogin;
import pub.developers.forum.app.support.LoginUserContext;
import pub.developers.forum.app.support.PageUtil;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.domain.entity.Posts;
import pub.developers.forum.domain.repository.UserFoodRepository;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/25
 * @desc
 **/
@Component
public class PostsManager extends AbstractPostsManager {

    @Resource
    private UserFoodRepository userFoodRepository;

    @IsLogin
    public PageResponseModel<PostsVO> pagePostsFood(PageRequestModel pageRequestModel) {
        PageResult<Posts> pageResult = userFoodRepository.pagePosts(PageUtil.buildPageRequest(pageRequestModel, LoginUserContext.getUser().getId()));

        return pagePostsVO(pageResult);
    }
}
