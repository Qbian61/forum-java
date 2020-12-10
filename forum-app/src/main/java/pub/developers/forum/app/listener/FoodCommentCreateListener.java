package pub.developers.forum.app.listener;

import org.springframework.stereotype.Component;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.Comment;
import pub.developers.forum.domain.entity.UserFood;
import pub.developers.forum.domain.repository.UserFoodRepository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/3
 * @desc
 **/
@Component
public class FoodCommentCreateListener extends EventBus.EventHandler<Map<String, Object>> {

    @Resource
    private UserFoodRepository userFoodRepository;

    @Override
    public EventBus.Topic topic() {
        return EventBus.Topic.COMMENT_CREATE;
    }

    @Override
    public void onMessage(Map<String, Object> msg) {
        Long userId = Long.valueOf(msg.get("commenter").toString());
        Comment comment = (Comment) msg.get("comment");

        List<UserFood> userFoods = Arrays.asList(UserFood.builder()
                .postsId(comment.getPostsId())
                .userId(userId)
                .build());

        userFoodRepository.batchSave(userFoods);
    }
}
