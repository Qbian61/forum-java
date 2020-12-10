package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.common.enums.FollowedTypeEn;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.Article;
import pub.developers.forum.domain.entity.UserFood;
import pub.developers.forum.domain.repository.UserFollowRepository;
import pub.developers.forum.domain.repository.UserFoodRepository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/3
 * @desc
 **/
@Component
public class FoodArticleCreateListener extends EventBus.EventHandler<Article> {

    @Resource
    private UserFollowRepository userFollowRepository;

    @Resource
    private UserFoodRepository userFoodRepository;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.ARTICLE_CREATE;
    }

    @Override
    public void onMessage(Article article) {
        List<Long> followedIds = userFollowRepository.getAllFollowerIds(article.getAuthorId(), FollowedTypeEn.USER);
        if (ObjectUtils.isEmpty(followedIds)) {
            return;
        }

        List<UserFood> userFoods = followedIds.stream().map(userId -> {
            return UserFood.builder()
                    .postsId(article.getId())
                    .userId(userId)
                    .build();
        }).collect(Collectors.toList());

        userFoodRepository.batchSave(userFoods);
    }
}
