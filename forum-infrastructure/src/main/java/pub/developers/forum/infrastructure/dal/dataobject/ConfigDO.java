package pub.developers.forum.infrastructure.dal.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/26
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigDO extends BaseDO {

    private String state;

    private String type;

    private String name;

    private String content;

    private Date startAt;

    private Date endAt;

    private Long creator;

}
