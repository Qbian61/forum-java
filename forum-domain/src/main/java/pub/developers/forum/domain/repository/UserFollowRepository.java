package pub.developers.forum.domain.repository;

import pub.developers.forum.common.enums.FollowedTypeEn;

import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/3
 * @desc
 **/
public interface UserFollowRepository {

    List<Long> getAllFollowerIds(Long follower, FollowedTypeEn type);
}
