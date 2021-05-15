package pub.developers.forum.infrastructure.dal.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/29
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDO extends BaseDO {

    /**
     * 角色
     */
    private String role;

    /**
     * 状态
     */
    private String state;

    /**
     * 用户名
     */
    private String nickname;

    /**
     * 性别
     */
    private String sex;

    /**
     * 来源
     */
    private String source;

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
     * 扩展信息
     */
    private String ext;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

}
