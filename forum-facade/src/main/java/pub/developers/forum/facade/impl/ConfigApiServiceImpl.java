package pub.developers.forum.facade.impl;

import org.springframework.stereotype.Service;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.AdminBooleanRequest;
import pub.developers.forum.api.request.config.ConfigAddRequest;
import pub.developers.forum.api.request.config.ConfigPageRequest;
import pub.developers.forum.api.request.config.ConfigUpdateRequest;
import pub.developers.forum.api.response.config.ConfigResponse;
import pub.developers.forum.api.service.ConfigApiService;
import pub.developers.forum.app.manager.ConfigManager;
import pub.developers.forum.facade.support.ResultModelUtil;
import pub.developers.forum.facade.validator.PageRequestModelValidator;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/26
 * @desc
 **/
@Service
public class ConfigApiServiceImpl implements ConfigApiService {

    @Resource
    private ConfigManager configManager;

    @Override
    public ResultModel add(ConfigAddRequest request) {

        configManager.add(request);

        return ResultModelUtil.success();
    }

    @Override
    public ResultModel update(ConfigUpdateRequest request) {

        configManager.update(request);

        return ResultModelUtil.success();
    }

    @Override
    public ResultModel state(AdminBooleanRequest request) {

        configManager.state(request);

        return ResultModelUtil.success();
    }

    @Override
    public ResultModel<PageResponseModel<ConfigResponse>> page(PageRequestModel<ConfigPageRequest> pageRequestModel) {
        PageRequestModelValidator.validator(pageRequestModel);

        return ResultModelUtil.success(configManager.page(pageRequestModel));
    }

    @Override
    public ResultModel<List<ConfigResponse>> queryAvailable(Set<String> types) {

        return ResultModelUtil.success(configManager.queryAvailable(types));
    }
}
