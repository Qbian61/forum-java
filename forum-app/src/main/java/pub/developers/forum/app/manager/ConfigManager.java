package pub.developers.forum.app.manager;

import org.springframework.stereotype.Component;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.request.AdminBooleanRequest;
import pub.developers.forum.api.request.config.ConfigAddRequest;
import pub.developers.forum.api.request.config.ConfigPageRequest;
import pub.developers.forum.api.request.config.ConfigUpdateRequest;
import pub.developers.forum.api.response.config.ConfigResponse;
import pub.developers.forum.app.support.IsLogin;
import pub.developers.forum.app.support.LoginUserContext;
import pub.developers.forum.app.support.PageUtil;
import pub.developers.forum.app.transfer.ConfigTransfer;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.enums.ErrorCodeEn;
import pub.developers.forum.common.enums.UserRoleEn;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.common.support.CheckUtil;
import pub.developers.forum.domain.entity.Config;
import pub.developers.forum.domain.repository.ConfigRepository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/26
 * @desc
 **/
@Component
public class ConfigManager {

    @Resource
    private ConfigRepository configRepository;

    @IsLogin(role = UserRoleEn.ADMIN)
    public void add(ConfigAddRequest request) {
        Config config = ConfigTransfer.toConfig(request);
        config.setCreator(LoginUserContext.getUser().getId());

        configRepository.save(config);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public void update(ConfigUpdateRequest request) {
        Config config = configRepository.get(request.getId());
        CheckUtil.isEmpty(config, ErrorCodeEn.CONFIG_NOT_EXIST);

        ConfigTransfer.update(config, request);
        configRepository.update(config);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public void state(AdminBooleanRequest request) {
        Config config = configRepository.get(request.getId());
        CheckUtil.isEmpty(config, ErrorCodeEn.CONFIG_NOT_EXIST);

        config.setState(request.getIs() ? AuditStateEn.PASS : AuditStateEn.REJECT);
        configRepository.update(config);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public PageResponseModel<ConfigResponse> page(PageRequestModel<ConfigPageRequest> pageRequestModel) {
        ConfigPageRequest request = pageRequestModel.getFilter();

        PageResult<Config> pageResult = configRepository.page(PageUtil.buildPageRequest(pageRequestModel, ConfigTransfer.toConfig(request)));

        return PageResponseModel.build(pageResult.getTotal(), pageResult.getSize(), ConfigTransfer.toConfigResponses(pageResult.getList()));
    }

    public List<ConfigResponse> queryAvailable(Set<String> types) {
        return ConfigTransfer.toConfigResponses(configRepository.queryAvailable(types));
    }
}
