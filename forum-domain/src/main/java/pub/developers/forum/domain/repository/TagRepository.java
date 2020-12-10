package pub.developers.forum.domain.repository;

import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.model.PageRequest;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.domain.entity.Posts;
import pub.developers.forum.domain.entity.Tag;

import java.util.List;
import java.util.Set;

/**
 * @author Qiangqiang.Bian
 * @create 2020/7/31
 * @desc
 **/
public interface TagRepository {

    void save(Tag tag);

    List<Tag> queryByIds(Set<Long> ids);

    List<Tag> queryByState(AuditStateEn auditState);

    void deletePostsMapping(Long articleId);

    void increaseRefCount(Set<Long> ids);

    void decreaseRefCount(Set<Long> ids);

    Tag getByNameAndState(String name, AuditStateEn pass);

    PageResult<Posts> pagePosts(PageRequest<Long> longPageRequest);
}
