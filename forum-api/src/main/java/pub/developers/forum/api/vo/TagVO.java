package pub.developers.forum.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Qiangqiang.Bian
 * @create 2020/8/6
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagVO implements Serializable {

    private Long id;

    private String name;

}
