package pub.developers.forum.infrastructure;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.enums.ErrorCodeEn;
import pub.developers.forum.common.exception.BizException;
import pub.developers.forum.common.model.PageRequest;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.Posts;
import pub.developers.forum.domain.entity.Tag;
import pub.developers.forum.domain.repository.TagRepository;
import pub.developers.forum.infrastructure.dal.dao.*;
import pub.developers.forum.infrastructure.dal.dataobject.TagDO;
import pub.developers.forum.infrastructure.dal.dataobject.TagPostsMappingDO;
import pub.developers.forum.infrastructure.transfer.TagTransfer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/8/3
 * @desc
 **/
@Repository
public class TagRepositoryImpl extends AbstractPostsRepository implements TagRepository {

    @Resource
    private ArticleTypeDAO articleTypeDAO;

    @Override
    public void save(Tag tag) {
        TagDO tagDO = TagTransfer.toTagDO(tag);

        try {
            tagDAO.insert(tagDO);
            tag.setId(tagDO.getId());
        } catch (DuplicateKeyException e) {
            throw new BizException(ErrorCodeEn.TAG_IS_EXIST);
        }
    }

    @Override
    public List<Tag> queryByIds(Set<Long> ids) {
        return TagTransfer.toTags(tagDAO.queryInIds(ids));
    }

    @Override
    public List<Tag> queryByState(AuditStateEn auditState) {
        return TagTransfer.toTags(tagDAO.query(TagDO.builder()
                .auditState(auditState.getValue())
                .build()));
    }

    @Override
    public void deletePostsMapping(Long articleId) {
        List<TagPostsMappingDO> tagPostsMappingDOS = tagPostsMappingDAO.queryInPostsIds(Sets.newHashSet(articleId));
        Set<Long> tagIds = SafesUtil.ofList(tagPostsMappingDOS).stream().map(TagPostsMappingDO::getTagId).collect(Collectors.toSet());
        if (!ObjectUtils.isEmpty(tagIds)) {
            tagDAO.decreaseRefCount(tagIds);
        }
        tagPostsMappingDAO.deleteByPostsId(articleId);
    }

    @Override
    public void increaseRefCount(Set<Long> ids) {
        tagDAO.increaseRefCount(ids);
    }

    @Override
    public void decreaseRefCount(Set<Long> ids) {
        tagDAO.decreaseRefCount(ids);
    }

    @Override
    public Tag getByNameAndState(String name, AuditStateEn pass) {
        List<TagDO> tagDOS = tagDAO.query(TagDO.builder()
                .auditState(pass.getValue())
                .name(name)
                .build());
        if (ObjectUtils.isEmpty(tagDOS)) {
            return null;
        }

        return TagTransfer.toTag(tagDOS.get(0));
    }

    @Override
    public PageResult<Posts> pagePosts(PageRequest<Long> pageRequest) {
        PageHelper.startPage(pageRequest.getPageNo(), pageRequest.getPageSize());
        List<TagPostsMappingDO> tagPostsMappingDOS = tagPostsMappingDAO.query(TagPostsMappingDO.builder()
                .tagId(pageRequest.getFilter())
                .build());
        PageInfo<TagPostsMappingDO> pageInfo = new PageInfo<>(tagPostsMappingDOS);

        if (ObjectUtils.isEmpty(tagPostsMappingDOS)) {
            return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), new ArrayList<>());
        }

        Set<Long> postsIds = tagPostsMappingDOS.stream()
                .map(TagPostsMappingDO::getPostsId)
                .collect(Collectors.toSet());

        return basePagePosts(postsIds, pageInfo);
    }
}
