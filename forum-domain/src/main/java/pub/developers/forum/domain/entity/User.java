package pub.developers.forum.domain.entity;

import lombok.*;
import org.springframework.beans.BeanUtils;
import pub.developers.forum.common.enums.UserRoleEn;
import pub.developers.forum.common.enums.UserSexEn;
import pub.developers.forum.common.enums.UserStateEn;
import pub.developers.forum.common.support.AvatarUtil;

import java.util.Date;

/**
 * @author Qiangqiang.Bian
 * @create 2020/7/30
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    /**
     * 角色
     */
    private UserRoleEn role;

    /**
     * 状态
     */
    private UserStateEn state;

    /**
     * 用户名
     */
    private String nickname;

    /**
     * 性别
     */
    private UserSexEn sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 签名
     */
    private String signature;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    public String getAvatar() {
        return AvatarUtil.get(avatar, email);
    }

    public User copy() {
        User user = new User();
        BeanUtils.copyProperties(this, user);

        return user;
    }

}
