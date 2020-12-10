package pub.developers.forum.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article extends BasePosts {

    private ArticleType type;

    private String headImg;

    private Boolean official;

    private Boolean top;

    private Long sort;

    private Boolean marrow;

    public Article copy() {
        Article article = new Article();
        BeanUtils.copyProperties(this, article);

        return article;
    }

}
