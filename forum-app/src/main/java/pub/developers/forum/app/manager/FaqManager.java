package pub.developers.forum.app.manager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.request.faq.*;
import pub.developers.forum.api.response.faq.FaqHotsResponse;
import pub.developers.forum.api.response.faq.FaqInfoResponse;
import pub.developers.forum.api.response.faq.FaqUserPageResponse;
import pub.developers.forum.app.support.IsLogin;
import pub.developers.forum.app.support.LoginUserContext;
import pub.developers.forum.app.support.Pair;
import pub.developers.forum.app.transfer.FaqTransfer;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.enums.ErrorCodeEn;
import pub.developers.forum.common.enums.PostsCategoryEn;
import pub.developers.forum.common.enums.UserRoleEn;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.common.support.CheckUtil;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.Comment;
import pub.developers.forum.domain.entity.Faq;
import pub.developers.forum.domain.entity.Tag;
import pub.developers.forum.domain.entity.User;
import pub.developers.forum.domain.entity.value.PostsPageQueryValue;
import pub.developers.forum.domain.repository.CommentRepository;
import pub.developers.forum.domain.repository.FaqRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/1
 * @desc
 **/
@Component
public class FaqManager extends AbstractPostsManager {

    @Resource
    private FaqRepository faqRepository;

    @Resource
    private CommentRepository commentRepository;

    @IsLogin
    @Transactional
    public Long saveFaq(FaqSaveFaqRequest request) {
        // 创建
        if (ObjectUtils.isEmpty(request.getId())) {
            // 校验标签
            Set<Tag> selectTags = checkTags(request.getTagIds());

            Faq faq = FaqTransfer.toFaq(request, selectTags, false);
            faqRepository.save(faq);

            // 触发创建事件
            EventBus.emit(EventBus.Topic.FAQ_CREATE, faq);

            return faq.getId();
        }

        // 更新
        // 校验问答
        Faq faq = faqRepository.get(request.getId());
        CheckUtil.isEmpty(faq, ErrorCodeEn.FAQ_NOT_EXIST);
        CheckUtil.isFalse(LoginUserContext.getUser().getId().equals(faq.getAuthor().getId()), ErrorCodeEn.FAQ_NOT_EXIST);

        // 校验标签
        Set<Tag> selectTags = checkTags(request.getTagIds());

        // 删除旧标签关联关系
        tagRepository.deletePostsMapping(request.getId());

        Faq oldFaq = faq.copy();
        Faq newFaq = FaqTransfer.toFaq(request, selectTags, true);

        faqRepository.update(newFaq);

        // 触发更新事件
        EventBus.emit(EventBus.Topic.FAQ_UPDATE, Pair.build(oldFaq, newFaq));

        return request.getId();
    }

    public PageResponseModel<FaqUserPageResponse> userPage(PageRequestModel<FaqUserPageRequest> pageRequestModel) {
        FaqUserPageRequest pageRequest = pageRequestModel.getFilter();

        PostsPageQueryValue pageQueryValue = PostsPageQueryValue.builder()
                .auditStates(Arrays.asList(AuditStateEn.PASS.getValue()))
                .build();

        if (!ObjectUtils.isEmpty(pageRequest.getSolution())) {
            if (pageRequest.getSolution() == 0) {
                pageQueryValue.setCommentId(1L);
            }
            if (pageRequest.getSolution() == 1) {
                pageQueryValue.setCommentId(2L);
            }
        }

        return pageQuery(pageRequestModel, pageQueryValue);
    }

    public PageResponseModel<FaqUserPageResponse> authorPage(PageRequestModel<FaqAuthorPageRequest> pageRequestModel) {
        FaqAuthorPageRequest request = pageRequestModel.getFilter();

        List<String> auditStates = new ArrayList<>();
        auditStates.add(AuditStateEn.PASS.getValue());

        User user = LoginUserContext.getUser();
        if (!ObjectUtils.isEmpty(user) && user.getId().equals(request.getUserId())) {
            auditStates.add(AuditStateEn.WAIT.getValue());
            auditStates.add(AuditStateEn.REJECT.getValue());
        }

        PostsPageQueryValue pageQueryValue = PostsPageQueryValue.builder()
                .auditStates(auditStates)
                .authorId(request.getUserId())
                .build();

        return pageQuery(pageRequestModel, pageQueryValue);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public PageResponseModel<FaqUserPageResponse> adminPage(PageRequestModel<FaqAdminPageRequest> pageRequestModel) {
        FaqAdminPageRequest adminPageRequest = pageRequestModel.getFilter();
        PostsPageQueryValue pageQueryValue = PostsPageQueryValue.builder()
                .category(PostsCategoryEn.FAQ.getValue())
                .authorId(adminPageRequest.getUserId())
                .title(adminPageRequest.getTitle())
                .commentId(adminPageRequest.getCommentId())
                .build();

        if (!ObjectUtils.isEmpty(adminPageRequest.getAuditState()) && !ObjectUtils.isEmpty(AuditStateEn.getEntity(adminPageRequest.getAuditState()))) {
            List<String> auditStates = new ArrayList<>();
            auditStates.add(AuditStateEn.getEntity(adminPageRequest.getAuditState()).getValue());
            pageQueryValue.setAuditStates(auditStates);
        }

        return pageQuery(pageRequestModel, pageQueryValue);
    }

    public FaqInfoResponse info(Long id) {
        Faq faq = faqRepository.get(id);
        CheckUtil.isEmpty(faq, ErrorCodeEn.FAQ_NOT_EXIST);

        if (!AuditStateEn.PASS.equals(faq.getAuditState())) {
            User user = LoginUserContext.getUser();
            CheckUtil.isEmpty(user, ErrorCodeEn.FAQ_IN_AUDIT_PROCESS);
            CheckUtil.isFalse(user.getId().equals(faq.getAuthor().getId()), ErrorCodeEn.FAQ_IN_AUDIT_PROCESS);
        }

        // 触发查看详情事件
        EventBus.emit(EventBus.Topic.POSTS_INFO, faq);

        return FaqTransfer.toFaqInfoResponse(faq);
    }

    public List<FaqHotsResponse> hots(int size) {
        return FaqTransfer.FaqHotsResponses(faqRepository.hots(size));
    }

    @IsLogin
    public void solution(FaqSolutionRequest request) {
        Faq faq = faqRepository.get(request.getFaqId());
        CheckUtil.isEmpty(faq, ErrorCodeEn.FAQ_NOT_EXIST);
        CheckUtil.isFalse(LoginUserContext.getUser().getId().equals(faq.getAuthor().getId()), ErrorCodeEn.NO_PERMISSION);

        Comment comment = commentRepository.get(request.getCommentId());
        CheckUtil.isEmpty(comment, ErrorCodeEn.COMMENT_NOT_EXIST);

        faq.setSolutionId(request.getCommentId());
        faqRepository.updateEntity(faq);
    }

    private PageResponseModel<FaqUserPageResponse> pageQuery(PageRequestModel pageRequestModel, PostsPageQueryValue pageQueryValue) {
        pageQueryValue.setCategory(PostsCategoryEn.FAQ.getValue());
        PageResult<Faq> pageResult = faqRepository.page(pageRequestModel.getPageNo(), pageRequestModel.getPageSize(), pageQueryValue);

        Set<Long> solutionIds = SafesUtil.ofList(pageResult.getList()).stream()
                .filter(faq -> !ObjectUtils.isEmpty(faq.getSolutionId()) && faq.getSolutionId() != 0L)
                .map(Faq::getSolutionId).collect(Collectors.toSet());

        if (ObjectUtils.isEmpty(solutionIds)) {
            return PageResponseModel.build(pageResult.getTotal(), pageResult.getSize(), FaqTransfer.toFaqUserPageResponses(pageResult.getList(), new ArrayList<>()));
        }

        List<Comment> comments = commentRepository.queryInIds(solutionIds);
        if (ObjectUtils.isEmpty(comments)) {
            return PageResponseModel.build(pageResult.getTotal(), pageResult.getSize(), FaqTransfer.toFaqUserPageResponses(pageResult.getList(),  new ArrayList<>()));
        }

        return PageResponseModel.build(pageResult.getTotal(), pageResult.getSize(), FaqTransfer.toFaqUserPageResponses(pageResult.getList(), comments));
    }

}
