package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
import pub.developers.forum.common.enums.MessageTypeEn;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.BasePosts;
import pub.developers.forum.domain.repository.MessageRepository;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/5
 * @desc
 **/
@Component
public class MessagePostsDeleteListener extends EventBus.EventHandler<BasePosts> {

    @Resource
    private MessageRepository messageRepository;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.POSTS_DELETE;
    }

    @Override
    public void onMessage(BasePosts basePosts) {
        messageRepository.deleteInTypesAndTitle(Arrays.asList(MessageTypeEn.APPROVAL_ARTICLE
                , MessageTypeEn.APPROVAL_FAQ
                , MessageTypeEn.COMMENT_ARTICLE
                , MessageTypeEn.COMMENT_FAQ), basePosts.getId().toString());
    }
}
