package pub.developers.forum.domain.service;

import pub.developers.forum.common.model.PageRequest;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.domain.entity.Posts;
import pub.developers.forum.domain.entity.Search;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/2
 * @desc
 **/
public interface SearchService {

    void deleteByPostsId(Long postsId);

    void save(Search search);

    PageResult<Posts> pagePosts(PageRequest<String> pageRequest);

}
