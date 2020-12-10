package pub.developers.forum.domain.repository;

import pub.developers.forum.domain.entity.Approval;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/1
 * @desc
 **/
public interface ApprovalRepository {

    void save(Approval approval);

    void delete(Long approvalId);

    Approval get(Long postsId, Long userId);

}
