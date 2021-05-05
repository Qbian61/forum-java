package pub.developers.forum.infrastructure;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.common.model.PageRequest;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.domain.entity.Posts;
import pub.developers.forum.domain.entity.UserFood;
import pub.developers.forum.domain.repository.UserFoodRepository;
import pub.developers.forum.infrastructure.dal.dao.UserFoodDAO;
import pub.developers.forum.infrastructure.dal.dataobject.UserFoodDO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/3
 * @desc
 **/
@Repository
public class UserFoodRepositoryImpl extends AbstractPostsRepository implements UserFoodRepository {

    @Resource
    private UserFoodDAO userFoodDAO;

    @Override
    public void batchSave(List<UserFood> userFoods) {
        if (ObjectUtils.isEmpty(userFoods)) {
            return;
        }

        userFoods.forEach(userFood -> {
            try {
                UserFoodDO userFoodDO = UserFoodDO.builder()
                        .userId(userFood.getUserId())
                        .postsId(userFood.getPostsId())
                        .build();
                userFoodDO.initBase();

                userFoodDAO.insert(userFoodDO);
            } catch (Exception e) {
                // 唯一健冲突忽略
            }
        });
    }

    @Override
    public PageResult<Posts> pagePosts(PageRequest<Long> pageRequest) {
        PageHelper.startPage(pageRequest.getPageNo(), pageRequest.getPageSize());

        List<UserFoodDO> userFoodDOS = userFoodDAO.query(pageRequest.getFilter());
        PageInfo<UserFoodDO> pageInfo = new PageInfo<>(userFoodDOS);

        if (ObjectUtils.isEmpty(userFoodDOS)) {
            return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), new ArrayList<>());
        }

        List<Long> postsIds = new ArrayList<>();
        userFoodDOS.forEach(userFoodDO -> postsIds.add(userFoodDO.getPostsId()));

        return basePagePosts(postsIds, pageInfo, null);
    }

    @Override
    public void deleteByPostsId(Long postsId) {
        userFoodDAO.deleteByPostsId(postsId);
    }
}
