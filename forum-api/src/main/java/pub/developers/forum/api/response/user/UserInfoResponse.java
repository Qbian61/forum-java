package pub.developers.forum.api.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/27
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse implements Serializable {

    private Long id;

    private String email;

    private String role;

    private String nickname;

    private String sex;

    private String avatar;

    private String signature;

    private Date createAt;

    private Date lastLoginTime;

}
