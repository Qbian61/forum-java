package pub.developers.forum.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/20
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Posts extends BasePosts {

    private ArticleType type;

    private String headImg;

    private Boolean official;

    private Boolean top;

    private Long sort;

    private Boolean marrow;

    /**
     * 最佳答案 ID
     */
    private Long solutionId;
}
