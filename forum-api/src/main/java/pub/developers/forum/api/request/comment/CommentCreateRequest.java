package pub.developers.forum.api.request.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/6
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequest implements Serializable {

    private Long postsId;

    private String content;

    private Long replyId;
}
