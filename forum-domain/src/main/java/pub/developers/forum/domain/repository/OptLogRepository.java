package pub.developers.forum.domain.repository;

import pub.developers.forum.common.model.PageRequest;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.domain.entity.OptLog;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/20
 * @desc
 **/
public interface OptLogRepository {

    void save(OptLog optLog);

    PageResult<OptLog> page(PageRequest<OptLog> pageRequest);
}
