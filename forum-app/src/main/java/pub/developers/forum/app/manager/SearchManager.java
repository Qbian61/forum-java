package pub.developers.forum.app.manager;

import org.springframework.stereotype.Component;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.vo.PostsVO;
import pub.developers.forum.app.support.PageUtil;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.domain.entity.Posts;
import pub.developers.forum.domain.service.SearchService;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/2
 * @desc
 **/
@Component
public class SearchManager extends AbstractPostsManager {

    @Resource
    private SearchService searchService;

    public PageResponseModel<PostsVO> pagePostsSearch(PageRequestModel<String> pageRequestModel) {
        PageResult<Posts> pageResult = searchService.pagePosts(PageUtil.buildPageRequest(pageRequestModel, pageRequestModel.getFilter()));

        return pagePostsVO(pageResult);
    }
}
