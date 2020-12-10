package pub.developers.forum.api.response.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/6
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentPageResponse implements Serializable {

    private Long id;

    private Date createAt;

    private String content;

    private Long replyId;

    /**评论人*/
    private Commentator commentator;

    /**被回复人*/
    private Commentator respondent;

    /**回复列表*/
    private List<CommentPageResponse> replies;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Commentator {
        private Long id;
        private String nickname;
        private String avatar;
    }

}
