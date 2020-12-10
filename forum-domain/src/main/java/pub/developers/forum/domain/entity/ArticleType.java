package pub.developers.forum.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pub.developers.forum.common.enums.ArticleTypeScopeEn;
import pub.developers.forum.common.enums.AuditStateEn;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleType extends BaseEntity {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 引用统计
     */
    private Long refCount;

    /**
     * 作用域
     */
    private ArticleTypeScopeEn scope;

    /**
     * 创建人
     */
    private Long creatorId;

    /**
     * 审核状态
     */
    private AuditStateEn auditState;

}
