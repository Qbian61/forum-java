package pub.developers.forum.infrastructure.dal.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Qiangqiang.Bian
 * @create 20/11/19
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowDO extends BaseDO {

    /**
     * 被关注主体ID
     */
    private Long followed;

    /**
     * 关注类型
     */
    private String followedType;

    /**
     * 关注人ID
     */
    private Long follower;

}
