package pub.developers.forum.api.request;

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
public class AdminBooleanRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Boolean is;

}
