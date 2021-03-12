package pub.developers.forum.app.manager;

import org.springframework.stereotype.Component;
import pub.developers.forum.app.support.IsLogin;
import pub.developers.forum.app.support.LoginUserContext;
import pub.developers.forum.app.support.Pair;
import pub.developers.forum.common.enums.ErrorCodeEn;
import pub.developers.forum.common.support.CheckUtil;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.Approval;
import pub.developers.forum.domain.entity.BasePosts;
import pub.developers.forum.domain.repository.ApprovalRepository;
import pub.developers.forum.domain.repository.PostsRepository;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/1
 * @desc
 **/
@Component
public class ApprovalManager {

    @Resource
    private ApprovalRepository approvalRepository;

    @Resource
    private PostsRepository postsRepository;

    @IsLogin
    public Long create(Long postsId) {
        Approval approval = approvalRepository.get(postsId, LoginUserContext.getUser().getId());
        CheckUtil.isNotEmpty(approval, ErrorCodeEn.REPEAT_OPERATION);

        BasePosts basePosts = postsRepository.get(postsId);
        CheckUtil.isEmpty(basePosts, ErrorCodeEn.POSTS_NOT_EXIST);

        approvalRepository.save(Approval.builder()
                .postsId(postsId)
                .userId(LoginUserContext.getUser().getId())
                .build());
        postsRepository.increaseApproval(postsId, basePosts.getUpdateAt());

        EventBus.emit(EventBus.Topic.APPROVAL_CREATE, Pair.build(LoginUserContext.getUser().getId(), postsId));

        return basePosts.getApprovals() + 1;
    }

    @IsLogin
    public Long delete(Long postsId) {
        Approval approval = approvalRepository.get(postsId, LoginUserContext.getUser().getId());
        CheckUtil.isEmpty(approval, ErrorCodeEn.OPERATION_DATA_NOT_EXIST);

        BasePosts basePosts = postsRepository.get(postsId);
        CheckUtil.isEmpty(basePosts, ErrorCodeEn.POSTS_NOT_EXIST);

        approvalRepository.delete(approval.getId());
        postsRepository.decreaseApproval(postsId, basePosts.getUpdateAt());

        return basePosts.getApprovals() - 1;
    }

    @IsLogin
    public Boolean hasApproval(Long postsId) {
        Approval approval = approvalRepository.get(postsId, LoginUserContext.getUser().getId());

        return approval != null;
    }

}
