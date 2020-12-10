package pub.developers.forum.portal.controller.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.response.tag.TagQueryResponse;
import pub.developers.forum.api.service.TagApiService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/13
 * @desc
 **/
@RestController
@RequestMapping("/tag-rest")
public class TagRestController {

    @Resource
    private TagApiService tagApiService;

    @PostMapping("/all")
    public ResultModel<List<TagQueryResponse>> all() {
        return tagApiService.queryAll();
    }
}
