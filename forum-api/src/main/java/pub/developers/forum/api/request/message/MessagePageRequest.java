package pub.developers.forum.api.request.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Qiangqiang.Bian
 * @create 2020/7/31
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessagePageRequest implements Serializable {

    private String typeDesc;

}
