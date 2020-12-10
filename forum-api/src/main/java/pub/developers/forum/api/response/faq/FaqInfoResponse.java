package pub.developers.forum.api.response.faq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pub.developers.forum.api.vo.TagVO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/17
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaqInfoResponse implements Serializable {

    private Long id;

    private String auditState;

    private String title;

    private String htmlContent;

    private String markdownContent;

    private Long authorId;

    private String authorNickname;

    private String authorAvatar;

    private Date createAt;

    private Date updateAt;

    private Long views;

    private Long approvals;

    private Long comments;

    private List<TagVO> tags;

}
