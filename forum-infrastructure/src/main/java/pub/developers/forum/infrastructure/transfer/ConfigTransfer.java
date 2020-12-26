package pub.developers.forum.infrastructure.transfer;

import org.springframework.util.ObjectUtils;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.enums.ConfigTypeEn;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.Config;
import pub.developers.forum.infrastructure.dal.dataobject.ConfigDO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/26
 * @desc
 **/
public class ConfigTransfer {

    public static List<Config> toConfigs(List<ConfigDO> configDOList) {
        List<Config> res = new ArrayList<>();

        SafesUtil.ofList(configDOList).forEach(configDO -> res.add(toConfig(configDO)));

        return res;
    }

    public static Config toConfig(ConfigDO configDO) {
        if (configDO == null) {
            return null;
        }
        Config config = new Config();
        config.setState(AuditStateEn.getEntity(configDO.getState()));
        config.setType(ConfigTypeEn.getEntity(configDO.getType()));
        config.setName(configDO.getName());
        config.setContent(configDO.getContent());
        config.setStartAt(configDO.getStartAt());
        config.setEndAt(configDO.getEndAt());
        config.setCreator(configDO.getCreator());
        config.setId(configDO.getId());
        config.setCreateAt(configDO.getCreateAt());
        config.setUpdateAt(configDO.getUpdateAt());
        return config;
    }

    public static ConfigDO toConfigDO(Config config) {
        if (config == null) {
            return null;
        }
        ConfigDO configDO = new ConfigDO();
        if (!ObjectUtils.isEmpty(config.getState())) {
            configDO.setState(config.getState().getValue());
        }
        if (!ObjectUtils.isEmpty(config.getType())) {
            configDO.setType(config.getType().getValue());
        }
        configDO.setName(config.getName());
        configDO.setContent(config.getContent());
        configDO.setStartAt(config.getStartAt());
        configDO.setEndAt(config.getEndAt());
        configDO.setCreator(config.getCreator());
        configDO.setId(config.getId());
        configDO.setCreateAt(config.getCreateAt());
        configDO.setUpdateAt(config.getUpdateAt());
        return configDO;
    }
}
