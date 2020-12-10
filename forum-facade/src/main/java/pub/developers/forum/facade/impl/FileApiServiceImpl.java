package pub.developers.forum.facade.impl;

import org.springframework.stereotype.Service;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.file.FileUploadImgRequest;
import pub.developers.forum.api.service.FileApiService;
import pub.developers.forum.app.manager.FileManager;
import pub.developers.forum.facade.support.ResultModelUtil;
import pub.developers.forum.facade.validator.FileValidator;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/23
 * @desc
 **/
@Service
public class FileApiServiceImpl implements FileApiService {

    @Resource
    private FileManager fileManager;

    @Override
    public ResultModel<String> uploadImg(FileUploadImgRequest request) {
        FileValidator.uploadImg(request);

        return ResultModelUtil.success(fileManager.uploadImg(request));
    }
}
