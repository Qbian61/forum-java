package pub.developers.forum.domain.repository;

import pub.developers.forum.common.model.PageRequest;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.domain.entity.Follow;
import pub.developers.forum.domain.entity.User;

import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/7/31
 * @desc
 **/
public interface UserRepository {

    void save(User user);

    User get(Long id);

    User getByEmail(String email);

    void update(User user);

    List<User> queryByIds(List<Long> ids);

    PageResult<User> page(PageRequest<User> pageRequest);

    void follow(Long followed, Long id);

    PageResult<User> pageFollower(PageRequest<Long> longPageRequest);

    PageResult<User> pageFans(PageRequest<Long> longPageRequest);

    Follow getFollow(Long followed, Long follower);

    void cancelFollow(Long followId);

    PageResult<User> pageActive(PageRequest pageRequest);
}
