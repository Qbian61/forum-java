package pub.developers.forum.app.transfer;

import pub.developers.forum.api.response.message.MessagePageResponse;
import pub.developers.forum.common.enums.MessageTypeEn;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.BasePosts;
import pub.developers.forum.domain.entity.Message;
import pub.developers.forum.domain.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/5
 * @desc
 **/
public class MessageTransfer {

    public static List<MessagePageResponse> toMessagePageResponses(List<Message> messages, List<User> users, List<BasePosts> postsList, User loginUser) {
        List<MessagePageResponse> res = new ArrayList<>();

        SafesUtil.ofList(messages).forEach(message -> {
            MessagePageResponse messagePageResponse = MessagePageResponse.builder()
                    .id(message.getId())
                    .read(message.getRead().getValue())
                    .sender(message.getSender().getId())
                    .typeDesc(message.getType().getDesc())
                    .createAt(message.getCreateAt())
                    .build();

            SafesUtil.ofList(users).forEach(user -> {
                if (user.getId().toString().equals(message.getSender().getId())) {
                    messagePageResponse.setSenderName(user.getNickname());
                    messagePageResponse.setSenderAvatar(user.getAvatar());
                }
            });

            if (MessageTypeEn.APPROVAL_ARTICLE.equals(message.getType())
                    || MessageTypeEn.APPROVAL_FAQ.equals(message.getType())
                    || MessageTypeEn.COMMENT_ARTICLE.equals(message.getType())
                    || MessageTypeEn.COMMENT_FAQ.equals(message.getType())) {
                SafesUtil.ofList(postsList).forEach(posts -> {
                    if (posts.getId().equals(Long.valueOf(message.getTitle()))) {
                        messagePageResponse.setTitle(posts.getTitle());
                        messagePageResponse.setInfoId(posts.getId().toString());
                    }
                });
            }
            if (MessageTypeEn.FOLLOW_USER.equals(message.getType())) {
                messagePageResponse.setTitle("关注了你");
                messagePageResponse.setInfoId(loginUser.getId().toString());
            }

            res.add(messagePageResponse);
        });

        return res;
    }
}
