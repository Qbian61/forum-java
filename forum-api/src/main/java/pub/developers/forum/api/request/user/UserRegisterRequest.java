package pub.developers.forum.api.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Qiangqiang.Bian
 * @create 20/7/30
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest extends UserBaseLoginRequest implements Serializable {

    private String email;

    private String nickname;

    private String password;

}
