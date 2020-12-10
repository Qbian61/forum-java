package pub.developers.forum.domain.repository;

import pub.developers.forum.domain.entity.BasePosts;

import java.util.List;
import java.util.Set;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/6
 * @desc
 **/
public interface PostsRepository {

    BasePosts get(Long id);

    List<BasePosts> queryInIds(Set<Long> postsIds);

    List<Long> getAllIdByAuthorId(Long authorId);

    void increaseComments(Long id);

    void decreaseComments(Long id);

    void increaseViews(Long id);

    void increaseApproval(Long id);

    void decreaseApproval(Long id);

    void delete(Long id);

    void update(BasePosts basePosts);
}
