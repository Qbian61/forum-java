package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
import pub.developers.forum.common.enums.SearchTypeEn;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.Article;
import pub.developers.forum.domain.entity.Search;
import pub.developers.forum.domain.service.SearchService;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/2
 * @desc
 **/
@Component
public class SearchArticleCreateListener extends EventBus.EventHandler<Article> {

    @Resource
    private SearchService searchService;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.ARTICLE_CREATE;
    }

    @Override
    public void onMessage(Article article) {
        searchService.deleteByPostsId(article.getId());

        searchService.save(Search.builder()
                .content(article.getMarkdownContent())
                .entityId(article.getId())
                .title(article.getTitle())
                .type(SearchTypeEn.POSTS)
                .build());
    }
}
