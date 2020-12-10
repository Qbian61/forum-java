package pub.developers.forum.api.service;

import pub.developers.forum.api.model.ResultModel;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/1
 * @desc
 **/
public interface ApprovalApiService {

    ResultModel<Long> create(Long postsId);

    ResultModel<Long> delete(Long postsId);

    ResultModel<Boolean> hasApproval(Long postsId);

}
