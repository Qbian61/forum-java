package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.Article;
import pub.developers.forum.domain.entity.Tag;
import pub.developers.forum.domain.repository.ArticleTypeRepository;
import pub.developers.forum.domain.repository.TagRepository;

import javax.annotation.Resource;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/4
 * @desc
 **/
@Component
public class ArticleCreateListener extends EventBus.EventHandler<Article> {

    @Resource
    private TagRepository tagRepository;

    @Resource
    private ArticleTypeRepository articleTypeRepository;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.ARTICLE_CREATE;
    }

    @Override
    public void onMessage(Article article) {
        Set<Long> tagIds = article.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        tagRepository.increaseRefCount(tagIds);

        articleTypeRepository.increaseRefCount(article.getType().getId());
    }
}
