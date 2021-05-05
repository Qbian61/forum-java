package pub.developers.forum.infrastructure;

import com.github.pagehelper.PageInfo;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.Posts;
import pub.developers.forum.domain.entity.Tag;
import pub.developers.forum.domain.entity.User;
import pub.developers.forum.infrastructure.dal.dao.PostsDAO;
import pub.developers.forum.infrastructure.dal.dao.TagDAO;
import pub.developers.forum.infrastructure.dal.dao.TagPostsMappingDAO;
import pub.developers.forum.infrastructure.dal.dao.UserDAO;
import pub.developers.forum.infrastructure.dal.dataobject.PostsDO;
import pub.developers.forum.infrastructure.dal.dataobject.TagPostsMappingDO;
import pub.developers.forum.infrastructure.transfer.PostsTransfer;
import pub.developers.forum.infrastructure.transfer.TagTransfer;
import pub.developers.forum.infrastructure.transfer.UserTransfer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/2
 * @desc
 **/
public abstract class AbstractPostsRepository {

    @Resource
    PostsDAO postsDAO;

    @Resource
    UserDAO userDAO;

    @Resource
    TagPostsMappingDAO tagPostsMappingDAO;

    @Resource
    TagDAO tagDAO;

    public PageResult<Posts> basePagePosts(List<Long> postsIds, PageInfo pageInfo, AuditStateEn auditStateEn) {
        List<PostsDO> queryPostsDOS;
        if (ObjectUtils.isEmpty(auditStateEn)) {
            queryPostsDOS = postsDAO.queryInIds(new HashSet<>(postsIds));
        } else {
            queryPostsDOS = postsDAO.queryInIdsAndState(new HashSet<>(postsIds), auditStateEn.getValue());
        }

        if (ObjectUtils.isEmpty(queryPostsDOS)) {
            return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), new ArrayList<>());
        }

        // 按 postsIds 顺序排序
        List<PostsDO> postsDOS = postsIds.stream().map(postsId -> {
            for (PostsDO postsDO : queryPostsDOS) {
                if (postsDO.getId().equals(postsId)) {
                    return postsDO;
                }
            }
            return null;
        }).filter(postsDO -> !ObjectUtils.isEmpty(postsDO)).collect(Collectors.toList());

        Set<Long> userIds = SafesUtil.ofList(postsDOS).stream().map(PostsDO::getAuthorId).collect(Collectors.toSet());
        List<User> users = UserTransfer.toUsers(userDAO.queryInIds(userIds));

        List<TagPostsMappingDO> tagPostsMappingDOList = tagPostsMappingDAO.queryInPostsIds(new HashSet<>(postsIds));
        if (ObjectUtils.isEmpty(tagPostsMappingDOList)) {
            return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), PostsTransfer.toPostsList(postsDOS, users, tagPostsMappingDOList, new ArrayList<>()));
        }

        Set<Long> tagIds = SafesUtil.ofList(tagPostsMappingDOList).stream().map(TagPostsMappingDO::getTagId).collect(Collectors.toSet());
        List<Tag> tags = TagTransfer.toTags(tagDAO.queryInIds(tagIds));

        return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), PostsTransfer.toPostsList(postsDOS, users, tagPostsMappingDOList, tags));
    }
}
