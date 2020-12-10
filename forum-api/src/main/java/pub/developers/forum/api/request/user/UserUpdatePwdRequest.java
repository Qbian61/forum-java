package pub.developers.forum.api.request.user;

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
public class UserUpdatePwdRequest implements Serializable {

    private String oldPassword;

    private String newPassword;

}
