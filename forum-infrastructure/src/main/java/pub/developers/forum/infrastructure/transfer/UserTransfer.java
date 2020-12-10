package pub.developers.forum.infrastructure.transfer;

import org.springframework.util.ObjectUtils;
import pub.developers.forum.common.enums.FollowedTypeEn;
import pub.developers.forum.common.enums.UserRoleEn;
import pub.developers.forum.common.enums.UserSexEn;
import pub.developers.forum.common.enums.UserStateEn;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.Follow;
import pub.developers.forum.domain.entity.User;
import pub.developers.forum.infrastructure.dal.dataobject.UserDO;
import pub.developers.forum.infrastructure.dal.dataobject.UserFollowDO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/29
 * @desc
 **/
public class UserTransfer {

    public static Follow toFollow(UserFollowDO userFollowDO) {
        Follow follow = Follow.builder()
                .followed(userFollowDO.getFollowed())
                .followedType(FollowedTypeEn.getEntity(userFollowDO.getFollowedType()))
                .follower(userFollowDO.getFollower())
                .build();
        follow.setCreateAt(userFollowDO.getCreateAt());
        follow.setId(userFollowDO.getId());
        follow.setUpdateAt(userFollowDO.getUpdateAt());

        return follow;
    }

    public static UserDO toUserDO(User user) {
        if (ObjectUtils.isEmpty(user)) {
            return null;
        }

        UserDO userDO = UserDO.builder()
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .signature(user.getSignature())
                .lastLoginTime(user.getLastLoginTime())
                .build();
        if (!ObjectUtils.isEmpty(user.getRole())) {
            userDO.setRole(user.getRole().getValue());
        }
        if (!ObjectUtils.isEmpty(user.getState())) {
            userDO.setState(user.getState().getValue());
        }
        if (!ObjectUtils.isEmpty(user.getSex())) {
            userDO.setSex(user.getSex().getValue());
        }
        userDO.setId(user.getId());
        userDO.setCreateAt(user.getCreateAt());
        userDO.setUpdateAt(user.getUpdateAt());

        return userDO;
    }

    public static User toUser(UserDO userDO) {
        if (ObjectUtils.isEmpty(userDO)) {
            return null;
        }

        User user = User.builder()
                .avatar(userDO.getAvatar())
                .state(UserStateEn.getEntity(userDO.getState()))
                .email(userDO.getEmail())
                .nickname(userDO.getNickname())
                .password(userDO.getPassword())
                .role(UserRoleEn.getEntity(userDO.getRole()))
                .sex(UserSexEn.getEntity(userDO.getSex()))
                .signature(userDO.getSignature())
                .lastLoginTime(userDO.getLastLoginTime())
                .build();

        user.setId(userDO.getId());
        user.setCreateAt(userDO.getCreateAt());
        user.setUpdateAt(userDO.getUpdateAt());

        return user;
    }

    public static List<User> toUsers(List<UserDO> userDOS) {
        List<User> users = new ArrayList<>();

        SafesUtil.ofList(userDOS).forEach(userDO -> users.add(toUser(userDO)));

        return users;
    }
}
