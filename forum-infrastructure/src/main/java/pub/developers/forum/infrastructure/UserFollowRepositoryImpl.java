package pub.developers.forum.infrastructure;

import org.springframework.stereotype.Repository;
import pub.developers.forum.common.enums.FollowedTypeEn;
import pub.developers.forum.domain.repository.UserFollowRepository;
import pub.developers.forum.infrastructure.dal.dao.UserFollowDAO;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/3
 * @desc
 **/
@Repository
public class UserFollowRepositoryImpl implements UserFollowRepository {

    @Resource
    private UserFollowDAO userFollowDAO;

    @Override
    public List<Long> getAllFollowerIds(Long follower, FollowedTypeEn type) {
        return userFollowDAO.getAllFollowerIds(follower, type.getValue());
    }
}
