package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.app.support.Pair;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.Faq;
import pub.developers.forum.domain.repository.TagRepository;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/24
 * @desc
 **/
@Component
public class FaqUpdateListener extends EventBus.EventHandler<Pair<Faq>> {

    @Resource
    private TagRepository tagRepository;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.FAQ_UPDATE;
    }

    @Override
    public void onMessage(Pair<Faq> pair) {
        Faq oldFaq = pair.getValue0();
        Faq newFaq = pair.getValue1();

        // 由于FaqManager里已经减了一遍标签的引用计数，需把原来的加回来
        Set<Long> oldTags=Pair.tagToLong(oldFaq.getTags());
        tagRepository.increaseRefCount(oldTags);

        // 更新标签引用计数
        Set<Long> addTags = Pair.diff(newFaq.getTags(), oldFaq.getTags());
        Set<Long> removeTags = Pair.diff(oldFaq.getTags(), newFaq.getTags());
        if (!ObjectUtils.isEmpty(addTags)) {
            tagRepository.increaseRefCount(addTags);
        }
        if (!ObjectUtils.isEmpty(removeTags)) {
            tagRepository.decreaseRefCount(removeTags);
        }
    }

}
