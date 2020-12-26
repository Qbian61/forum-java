package pub.developers.forum.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.enums.ConfigTypeEn;

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
public class Config extends BaseEntity {

    private AuditStateEn state;

    private ConfigTypeEn type;

    private String name;

    private String content;

    private Date startAt;

    private Date endAt;

    private Long creator;
}
