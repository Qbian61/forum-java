package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
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
public class SearchFaqCreateListener extends EventBus.EventHandler<Faq> {

    @Resource
    private SearchService searchService;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.FAQ_CREATE;
    }

    @Override
    public void onMessage(Faq faq) {
        searchService.deleteByPostsId(faq.getId());

        searchService.save(Search.builder()
                .content(faq.getMarkdownContent())
                .entityId(faq.getId())
                .title(faq.getTitle())
                .type(SearchTypeEn.POSTS)
                .build());
    }
}
