package pub.developers.forum.infrastructure;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.common.enums.FollowedTypeEn;
import pub.developers.forum.common.model.PageRequest;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.common.support.LogUtil;
import pub.developers.forum.domain.entity.Follow;
import pub.developers.forum.domain.entity.User;
import pub.developers.forum.domain.repository.UserRepository;
import pub.developers.forum.infrastructure.dal.dao.UserDAO;
import pub.developers.forum.infrastructure.dal.dao.UserFollowDAO;
import pub.developers.forum.infrastructure.dal.dataobject.UserDO;
import pub.developers.forum.infrastructure.dal.dataobject.UserFollowDO;
import pub.developers.forum.infrastructure.transfer.UserTransfer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/8/3
 * @desc
 **/
@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Resource
    private UserDAO userDAO;

    @Resource
    private UserFollowDAO userFollowDAO;

    @Override
    public PageResult<User> pageFollower(PageRequest<Long> pageRequest) {
        PageHelper.startPage(pageRequest.getPageNo(), pageRequest.getPageSize());
        List<UserFollowDO> userFollowDOS = userFollowDAO.query(UserFollowDO.builder()
                .followedType(FollowedTypeEn.USER.getValue())
                .follower(pageRequest.getFilter())
                .build());

        PageInfo<UserFollowDO> pageInfo = new PageInfo<>(userFollowDOS);

        if (ObjectUtils.isEmpty(userFollowDOS)) {
            return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), new ArrayList<>());
        }

        List<Long> followedIds = new ArrayList<>();
        userFollowDOS.forEach(followDO -> followedIds.add(followDO.getFollowed()));

        return pageUser(pageInfo, followedIds);
    }

    @Override
    public PageResult<User> pageFans(PageRequest<Long> pageRequest) {
        PageHelper.startPage(pageRequest.getPageNo(), pageRequest.getPageSize());
        List<UserFollowDO> userFollowDOS = userFollowDAO.query(UserFollowDO.builder()
                .followed(pageRequest.getFilter())
                .followedType(FollowedTypeEn.USER.getValue())
                .build());
        PageInfo<UserFollowDO> pageInfo = new PageInfo<>(userFollowDOS);

        if (ObjectUtils.isEmpty(userFollowDOS)) {
            return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), new ArrayList<>());
        }

        List<Long> followerIds = new ArrayList<>();
        userFollowDOS.forEach(followDO -> followerIds.add(followDO.getFollower()));

        return pageUser(pageInfo, followerIds);
    }

    @Override
    public PageResult<User> pageActive(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNo(), pageRequest.getPageSize());
        List<UserDO> userDOS = userDAO.queryOrderLastLoginTime();
        PageInfo<UserDO> pageInfo = new PageInfo<>(userDOS);
        if (ObjectUtils.isEmpty(userDOS)) {
            return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), new ArrayList<>());
        }

        return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), UserTransfer.toUsers(userDOS));
    }

    @Override
    public Follow getFollow(Long followed, Long follower) {
        List<UserFollowDO> followDOList = userFollowDAO.query(UserFollowDO.builder()
                .followed(followed)
                .follower(follower)
                .build());
        if (ObjectUtils.isEmpty(followDOList)) {
            return null;
        }

        return UserTransfer.toFollow(followDOList.get(0));
    }

    @Override
    public void follow(Long followed, Long follower) {
        UserFollowDO userFollowDO = UserFollowDO.builder()
                .followed(followed)
                .followedType(FollowedTypeEn.USER.getValue())
                .follower(follower)
                .build();
        userFollowDO.initBase();

        try {
            userFollowDAO.insert(userFollowDO);
        } catch (DuplicateKeyException e) {
            LogUtil.info(log, "唯一键冲突，已存在关注关系，followed={}, follower={}", followed, follower);
        }
    }

    @Override
    public void cancelFollow(Long followId) {
        userFollowDAO.delete(followId);
    }

    @Override
    public User get(Long id) {
        UserDO userDO = userDAO.get(id);

        return UserTransfer.toUser(userDO);
    }

    @Override
    public void save(User user) {
        UserDO userDO = UserTransfer.toUserDO(user);
        userDO.initBase();
        userDAO.insert(userDO);
        user.setId(userDO.getId());
    }

    @Override
    public User getByEmail(String email) {
        UserDO userDO = userDAO.getByEmail(email);

        return UserTransfer.toUser(userDO);
    }

    @Override
    public void update(User user) {
        userDAO.update(UserTransfer.toUserDO(user));
    }

    @Override
    public List<User> queryByIds(List<Long> ids) {
        return UserTransfer.toUsers(userDAO.queryInIds(new HashSet<>(ids)));
    }

    @Override
    public PageResult<User> page(PageRequest<User> pageRequest) {
        PageHelper.startPage(pageRequest.getPageNo(), pageRequest.getPageSize());

        UserDO userDO = UserTransfer.toUserDO(pageRequest.getFilter());
        List<UserDO> userDOList = userDAO.query(userDO);
        PageInfo<UserDO> pageInfo = new PageInfo<>(userDOList);

        if (ObjectUtils.isEmpty(userDOList)) {
            return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), new ArrayList<>());
        }

        List<Long> uids = new ArrayList<>();
        userDOList.forEach(userDO1 -> uids.add(userDO1.getId()));

        return pageUser(pageInfo, uids);
    }

    private PageResult<User> pageUser(PageInfo pageInfo, List<Long> uids) {
        List<UserDO> userDOS = userDAO.queryInIds(new HashSet<>(uids));
        if (ObjectUtils.isEmpty(userDOS)) {
            return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), new ArrayList<>());
        }

        // 按 uids 顺序排序
        List<UserDO> userDOList = uids.stream().map(uid -> {
            for (UserDO userDO : userDOS) {
                if (userDO.getId().equals(uid)) {
                    return userDO;
                }
            }
            return null;
        }).collect(Collectors.toList());

        return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), UserTransfer.toUsers(userDOList));
    }

}
