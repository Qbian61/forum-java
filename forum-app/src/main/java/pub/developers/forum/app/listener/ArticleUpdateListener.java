package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.app.support.Pair;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.Article;
import pub.developers.forum.domain.repository.ArticleTypeRepository;
import pub.developers.forum.domain.repository.TagRepository;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/5
 * @desc
 **/
@Component
public class ArticleUpdateListener extends EventBus.EventHandler<Pair<Article>> {

    @Resource
    private ArticleTypeRepository articleTypeRepository;

    @Resource
    private TagRepository tagRepository;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.ARTICLE_UPDATE;
    }

    @Override
    public void onMessage(Pair<Article> pair) {
        Article oldArticle = pair.getValue0();
        Article newArticle = pair.getValue1();

        // 更新文章类型引用计数
        if (!oldArticle.getType().equals(newArticle.getType())) {
            articleTypeRepository.decreaseRefCount(oldArticle.getType().getId());
            articleTypeRepository.increaseRefCount(newArticle.getType().getId());
        }

        // 由于ArticleManager里已经减了一遍标签的引用计数，需把原来的加回来
        Set<Long> oldTags=Pair.tagToLong(oldArticle.getTags());
        tagRepository.increaseRefCount(oldTags);

        // 更新标签引用计数
        Set<Long> addTags = Pair.diff(newArticle.getTags(), oldArticle.getTags());
        Set<Long> removeTags = Pair.diff(oldArticle.getTags(), newArticle.getTags());
        if (!ObjectUtils.isEmpty(addTags)) {
            tagRepository.increaseRefCount(addTags);
        }
        if (!ObjectUtils.isEmpty(removeTags)) {
            tagRepository.decreaseRefCount(removeTags);
        }
    }

}
