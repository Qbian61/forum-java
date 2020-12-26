package pub.developers.forum.api.response.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
public class ConfigResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String state;

    private String type;

    private String name;

    private String content;

    private Date startAt;

    private Date endAt;

    private Long creator;

    private Date createAt;

    private Date updateAt;

}
