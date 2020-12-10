package pub.developers.forum.portal.controller.rest;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.file.FileUploadImgRequest;
import pub.developers.forum.api.service.FileApiService;
import pub.developers.forum.common.constant.Constant;
import pub.developers.forum.common.enums.ErrorCodeEn;
import pub.developers.forum.common.support.CheckUtil;
import pub.developers.forum.common.support.StringUtil;
import pub.developers.forum.portal.support.WebUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/16
 * @desc
 **/
@Slf4j
@RestController
@RequestMapping("/file-rest")
public class FileRestController {

    @Resource
    private FileApiService fileApiService;

    // .css;.js;.png;.jpeg;.jpg;.woff2;.html;.ico;.gif;.bmp;.svg;.woff;.map
    private static final Set<String> ALLOW_TYPES = Sets.newHashSet("png", "jpeg", "jpg", "ico", "gif", "bmp", "svg");

    @PostMapping("/upload")
    public ResultModel<String> upload0(@RequestParam(value = "image") MultipartFile file, HttpServletRequest request) {
        request.setAttribute(Constant.REQUEST_HEADER_TOKEN_KEY, WebUtil.cookieGetSid(request));

        String fileType = file.getContentType();
        Boolean isAllowType = false;
        for (String allowType : ALLOW_TYPES) {
            if (fileType.contains(allowType)) {
                isAllowType = true;
            }
        }
        CheckUtil.isFalse(isAllowType, ErrorCodeEn.FILE_UPLOAD_NOT_SUPPORT_IMG_TYPE);

        FileUploadImgRequest uploadImgRequest = null;
        try {
            uploadImgRequest = FileUploadImgRequest.builder()
                    .base64(file.getBytes())
                    .fileName(StringUtil.generateUUID())
                    .build();
        } catch (Exception e) {
            CheckUtil.isTrue(true, ErrorCodeEn.FILE_UPLOAD_FAIL);
        }

        return fileApiService.uploadImg(uploadImgRequest);
    }

}
