package pub.developers.forum.api.request.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/23
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadImgRequest implements Serializable {

    private byte[] base64;
    private String fileName;
}
