package pub.developers.forum.app.manager;

import org.springframework.stereotype.Component;
import pub.developers.forum.api.request.file.FileUploadImgRequest;
import pub.developers.forum.app.support.IsLogin;
import pub.developers.forum.domain.service.FileService;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/23
 * @desc
 **/
@Component
public class FileManager {

    @Resource
    private FileService fileService;

    @IsLogin
    public String uploadImg(FileUploadImgRequest request) {
        return fileService.uploadImg(request.getBase64(), request.getFileName());
    }
}
