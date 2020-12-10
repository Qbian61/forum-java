package pub.developers.forum.api.request.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/9
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleAdminPageRequest implements Serializable {

    private Long typeId;

    private String auditState;

    private Boolean official;

    private Boolean top;

    private Boolean marrow;

    private Long userId;

    private String title;

}
