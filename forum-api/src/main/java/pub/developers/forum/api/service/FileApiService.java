package pub.developers.forum.api.service;

import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.file.FileUploadImgRequest;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/23
 * @desc
 **/
public interface FileApiService {

    ResultModel<String> uploadImg(FileUploadImgRequest request);

}
