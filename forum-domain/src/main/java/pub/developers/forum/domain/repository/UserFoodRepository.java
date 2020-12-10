package pub.developers.forum.domain.repository;

import pub.developers.forum.common.model.PageRequest;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.domain.entity.Posts;
import pub.developers.forum.domain.entity.UserFood;

import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/3
 * @desc
 **/
public interface UserFoodRepository {

    void batchSave(List<UserFood> userFoods);

    PageResult<Posts> pagePosts(PageRequest<Long> pageRequest);

    void deleteByPostsId(Long postsId);
}
