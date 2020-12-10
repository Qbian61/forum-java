package pub.developers.forum.infrastructure.dal.dao;

import org.apache.ibatis.annotations.Param;
import pub.developers.forum.infrastructure.dal.dataobject.SearchDO;

import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/2
 * @desc
 **/
public interface SearchDAO {

    void insert(SearchDO searchDO);

    void delete(@Param("type") String type, @Param("entityId") Long entityId);

    List<SearchDO> query(SearchDO searchDO);
}
