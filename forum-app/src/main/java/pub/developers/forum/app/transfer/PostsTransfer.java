package pub.developers.forum.app.transfer;

import pub.developers.forum.api.vo.PostsVO;
import pub.developers.forum.api.vo.SolutionVO;
import pub.developers.forum.api.vo.TagVO;
import pub.developers.forum.common.enums.PostsCategoryEn;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.Comment;
import pub.developers.forum.domain.entity.Posts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/20
 * @desc
 **/
public class PostsTransfer {

    public static List<PostsVO> toPostsVOs(List<Posts> postsList, List<Comment> comments) {
        List<PostsVO> res = new ArrayList<>();

        SafesUtil.ofList(postsList).forEach(posts -> {
            SolutionVO solution = null;
            for (Comment comment : comments) {
                if (comment.getId().equals(posts.getSolutionId())) {
                    solution = SolutionVO.builder()
                            .content(comment.getContent())
                            .id(comment.getId())
                            .build();
                }
            }

            PostsVO postsVO = PostsVO.builder()
                    .category(posts.getCategory().getValue())
                    .categoryDesc(posts.getCategory().getDesc())
                    .authorAvatar(posts.getAuthor().getAvatar())
                    .authorId(posts.getAuthor().getId())
                    .authorNickname(posts.getAuthor().getNickname())
                    .comments(posts.getComments())
                    .createAt(posts.getCreateAt())
                    .headImg(posts.getHeadImg())
                    .id(posts.getId())
                    .introduction(posts.getMarkdownContent())
                    .marrow(posts.getMarrow())
                    .official(posts.getOfficial())
                    .tags(SafesUtil.ofSet(posts.getTags()).stream().map(tag -> {
                        return TagVO.builder()
                                .id(tag.getId())
                                .name(tag.getName())
                                .build();
                    }).collect(Collectors.toList()))
                    .title(posts.getTitle())
                    .top(posts.getTop())
                    .views(posts.getViews())
                    .approvals(posts.getApprovals())
                    .solution(solution)
                    .build();

            res.add(postsVO);
        });

        return res;
    }
}
