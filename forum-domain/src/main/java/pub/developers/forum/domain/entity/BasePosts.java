package pub.developers.forum.domain.entity;

import lombok.Data;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.enums.ContentTypeEn;
import pub.developers.forum.common.enums.PostsCategoryEn;

import java.util.Set;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
@Data
public class BasePosts extends BaseEntity {

    private AuditStateEn auditState;

    private PostsCategoryEn category;

    private User author;

    private String title;

    private ContentTypeEn contentType;

    private String markdownContent;

    private String htmlContent;

    private Set<Tag> tags;

    private Long views;

    private Long approvals;

    private Long comments;

    private Long typeId;

    private Long authorId;

    private Boolean official;

    private Boolean top;

    private Long sort;

    private Boolean marrow;
}
