package pub.developers.forum.domain.entity.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pub.developers.forum.common.enums.IdValueTypeEn;

import java.io.Serializable;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/22
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdValue implements Serializable {

    private String id;

    private IdValueTypeEn type;

}
