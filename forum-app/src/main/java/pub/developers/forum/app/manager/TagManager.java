package pub.developers.forum.app.manager;

import org.springframework.stereotype.Component;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.request.tag.TagCreateRequest;
import pub.developers.forum.api.response.tag.TagQueryResponse;
import pub.developers.forum.api.vo.PostsVO;
import pub.developers.forum.app.support.IsLogin;
import pub.developers.forum.app.support.PageUtil;
import pub.developers.forum.app.transfer.TagTransfer;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.enums.ErrorCodeEn;
import pub.developers.forum.common.enums.UserRoleEn;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.common.support.CheckUtil;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.Posts;
import pub.developers.forum.domain.entity.Tag;
import pub.developers.forum.domain.repository.TagRepository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/7/31
 * @desc
 **/
@Component
public class TagManager extends AbstractPostsManager {

    @Resource
    private TagRepository tagRepository;

    @IsLogin(role = UserRoleEn.ADMIN)
    public void create(TagCreateRequest request) {
        tagRepository.save(TagTransfer.toTag(request));
    }

    public List<TagQueryResponse> queryAll() {
        return TagTransfer.toTagQueryAllResponses(tagRepository.queryByState(AuditStateEn.PASS));
    }

    public List<TagQueryResponse> queryInIds(Set<Long> ids) {
        List<Tag> tags = tagRepository.queryByIds(ids);

        return TagTransfer.toTagQueryAllResponses(SafesUtil.ofList(tags).stream()
                .filter(tag -> AuditStateEn.PASS.equals(tag.getAuditState()))
                .collect(Collectors.toList()));
    }

    public TagQueryResponse getByName(String name) {
        Tag tag = tagRepository.getByNameAndState(name, AuditStateEn.PASS);
        CheckUtil.isEmpty(tag, ErrorCodeEn.TAG_NOT_EXIST);

        return TagTransfer.toTagQueryAllResponse(tag);
    }

    public PageResponseModel<PostsVO> pagePosts(PageRequestModel<Long> pageRequestModel) {
        PageResult<Posts> pageResult = tagRepository.pagePosts(PageUtil.buildPageRequest(pageRequestModel, pageRequestModel.getFilter()));

        return pagePostsVO(pageResult);
    }
}
