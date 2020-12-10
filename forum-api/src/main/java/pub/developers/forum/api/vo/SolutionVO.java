package pub.developers.forum.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/1
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolutionVO implements Serializable {

    private Long id;

    private String content;

}
