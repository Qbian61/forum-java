package pub.developers.forum.api.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Qiangqiang.Bian
 * @create 2020/9/8
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPageResponse implements Serializable {

    private Long id;

    private String email;

    private String role;

    private String nickname;

    private String sex;

    private String state;

    private String avatar;

    private String signature;

    private String createAt;

    private String updateAt;

}
