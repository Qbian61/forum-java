package pub.developers.forum.domain.repository;

import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.domain.entity.Comment;

import java.util.List;
import java.util.Set;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/5
 * @desc
 **/
public interface CommentRepository {

    void save(Comment comment);

    Comment get(Long id);

    List<Comment> queryByPostsId(Long postsId);

    List<Comment> queryInReplyIds(Set<Long> replyIds);

    PageResult<Comment> page(Integer pageNo, Integer pageSize, Long postsId);

    void deleteByPostsId(Long postsId);

    List<Comment> queryInIds(Set<Long> ids);
}
