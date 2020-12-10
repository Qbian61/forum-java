package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
import pub.developers.forum.app.support.Pair;
import pub.developers.forum.common.enums.*;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.Message;
import pub.developers.forum.domain.entity.value.IdValue;
import pub.developers.forum.domain.repository.MessageRepository;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/5
 * @desc
 **/
@Component
public class MessageUserFollowListener extends EventBus.EventHandler<Pair<Long>> {

    @Resource
    private MessageRepository messageRepository;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.USER_FOLLOW;
    }

    @Override
    public void onMessage(Pair<Long> pair) {
        Long followed = pair.getValue0();
        Long follower = pair.getValue1();

        messageRepository.save(Message.builder()
                .channel(MessageChannelEn.STATION_LETTER)
                .type(MessageTypeEn.FOLLOW_USER)
                .receiver(IdValue.builder()
                        .id(followed.toString())
                        .type(IdValueTypeEn.USER_ID)
                        .build())
                .read(MessageReadEn.NO)
                .contentType(MessageContentTypeEn.TEXT)
                .content("")
                .sender(IdValue.builder()
                        .id(follower.toString())
                        .type(IdValueTypeEn.USER_ID)
                        .build())
                .title("")
                .build());
    }
}
