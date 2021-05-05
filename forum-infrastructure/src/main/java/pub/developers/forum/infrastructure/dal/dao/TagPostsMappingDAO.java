package pub.developers.forum.infrastructure.dal.dao;

import org.apache.ibatis.annotations.Param;
import pub.developers.forum.infrastructure.dal.dataobject.TagPostsMappingDO;

import java.util.List;
import java.util.Set;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/31
 * @desc
 **/
public interface TagPostsMappingDAO {

    void insert(TagPostsMappingDO tagPostsMappingDO);

    List<TagPostsMappingDO> query(TagPostsMappingDO tagPostsMappingDO);

    void deleteByPostsId(@Param("postsId") Long postsId);

    void batchInsert(List<TagPostsMappingDO> tagPostsMappingDOS);

    List<TagPostsMappingDO> queryInPostsIds(@Param("postsIds") Set<Long> postsIds);

    List<TagPostsMappingDO> queryInTagIds(@Param("tagIds") Set<Long> tagIds);
}
