package pub.developers.forum.facade.impl;

import org.springframework.stereotype.Service;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.service.ApprovalApiService;
import pub.developers.forum.app.manager.ApprovalManager;
import pub.developers.forum.facade.support.ResultModelUtil;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/1
 * @desc
 **/
@Service
public class ApprovalApiServiceImpl implements ApprovalApiService {

    @Resource
    private ApprovalManager approvalManager;

    @Override
    public ResultModel<Long> create(Long postsId) {
        return ResultModelUtil.success(approvalManager.create(postsId));
    }

    @Override
    public ResultModel<Long> delete(Long postsId) {
        return ResultModelUtil.success(approvalManager.delete(postsId));
    }

    @Override
    public ResultModel<Boolean> hasApproval(Long postsId) {
        return ResultModelUtil.success(approvalManager.hasApproval(postsId));
    }

}
