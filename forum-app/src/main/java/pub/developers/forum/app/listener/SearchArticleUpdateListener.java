package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
import pub.developers.forum.app.support.Pair;
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
public class SearchArticleUpdateListener  extends EventBus.EventHandler<Pair<Article>> {

    @Resource
    private SearchService searchService;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.ARTICLE_UPDATE;
    }

    @Override
    public void onMessage(Pair<Article> pair) {
        Article newArticle = pair.getValue1();

        searchService.deleteByPostsId(newArticle.getId());

        searchService.save(Search.builder()
                .content(newArticle.getMarkdownContent())
                .entityId(newArticle.getId())
                .title(newArticle.getTitle())
                .type(SearchTypeEn.POSTS)
                .build());
    }
}
