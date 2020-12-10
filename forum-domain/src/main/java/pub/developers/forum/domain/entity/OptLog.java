package pub.developers.forum.domain.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pub.developers.forum.common.enums.OptLogTypeEn;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/20
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptLog extends BaseEntity {

    private OptLogTypeEn type;

    private Long operatorId;

    private String content;

    public static OptLog createUserRegisterRecordLog(Long operatorId, User user) {
        return OptLog.builder()
                .type(OptLogTypeEn.USER_REGISTER)
                .operatorId(operatorId)
                .content(JSON.toJSONString(user))
                .build();
    }

    public static OptLog createUserLoginRecordLog(Long operatorId, String content) {
        return OptLog.builder()
                .type(OptLogTypeEn.USER_LOGIN)
                .operatorId(operatorId)
                .content(content)
                .build();
    }

    public static OptLog createUserLogoutRecordLog(Long operatorId, String content) {
        return OptLog.builder()
                .type(OptLogTypeEn.USER_LOGOUT)
                .operatorId(operatorId)
                .content(content)
                .build();
    }
}
