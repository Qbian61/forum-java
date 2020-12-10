package pub.developers.forum.domain.service;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/23
 * @desc
 **/
public interface FileService {

    String uploadImg(byte[] base64, String fileName);
}
