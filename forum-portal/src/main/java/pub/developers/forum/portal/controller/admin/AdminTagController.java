package pub.developers.forum.portal.controller.admin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.tag.TagCreateRequest;
import pub.developers.forum.api.service.TagApiService;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
@RestController
@RequestMapping("/admin/tag")
public class AdminTagController {

    @Resource
    private TagApiService tagApiService;

    @PostMapping("/add")
    public ResultModel add(@RequestBody TagCreateRequest request) {
        return tagApiService.create(request);
    }

}
