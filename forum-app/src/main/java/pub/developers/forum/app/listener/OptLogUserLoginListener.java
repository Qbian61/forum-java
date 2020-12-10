package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.OptLog;
import pub.developers.forum.domain.repository.OptLogRepository;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/4
 * @desc
 **/
@Component
public class OptLogUserLoginListener extends EventBus.EventHandler<OptLog> {

    @Resource
    private OptLogRepository optLogRepository;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.USER_LOGIN;
    }

    @Override
    public void onMessage(OptLog optLog) {
        optLogRepository.save(optLog);
    }
}
