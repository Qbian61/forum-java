package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
import pub.developers.forum.app.support.Pair;
import pub.developers.forum.common.enums.SearchTypeEn;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.Faq;
import pub.developers.forum.domain.entity.Search;
import pub.developers.forum.domain.service.SearchService;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/2
 * @desc
 **/
@Component
public class SearchFaqUpdateListener extends EventBus.EventHandler<Pair<Faq>> {

    @Resource
    private SearchService searchService;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.FAQ_UPDATE;
    }

    @Override
    public void onMessage(Pair<Faq> pair) {
        Faq newFaq = pair.getValue1();

        searchService.deleteByPostsId(newFaq.getId());

        searchService.save(Search.builder()
                .content(newFaq.getMarkdownContent())
                .entityId(newFaq.getId())
                .title(newFaq.getTitle())
                .type(SearchTypeEn.POSTS)
                .build());
    }
}
