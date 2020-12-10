package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.app.support.Pair;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.UserFood;
import pub.developers.forum.domain.repository.PostsRepository;
import pub.developers.forum.domain.repository.UserFoodRepository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 20/11/19
 * @desc
 **/
@Component
public class FoodUserFollowListener extends EventBus.EventHandler<Pair<Long>> {

    @Resource
    private PostsRepository postsRepository;

    @Resource
    private UserFoodRepository userFoodRepository;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.USER_FOLLOW;
    }

    @Override
    public void onMessage(Pair<Long> pair) {
        Long followed = pair.getValue0();
        Long follower = pair.getValue1();

        List<Long> postsIds = postsRepository.getAllIdByAuthorId(followed);
        if (ObjectUtils.isEmpty(postsIds)) {
            return;
        }

        List<UserFood> userFoods = postsIds.stream().map(postsId -> {
            return UserFood.builder()
                    .postsId(postsId)
                    .userId(follower)
                    .build();
        }).collect(Collectors.toList());

        userFoodRepository.batchSave(userFoods);
    }
}
