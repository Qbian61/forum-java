package pub.developers.forum.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Qiangqiang.Bian
 * @create 2020/7/30
 * @desc
 **/
@Getter
@AllArgsConstructor
public enum MessageTypeEn {
    USER_REGISTER_NOTIFY_ADMIN("USER_REGISTER_NOTIFY_ADMIN", "新用户注册通知管理员"),
    APPROVAL_ARTICLE("APPROVAL_ARTICLE", "点赞文章"),
    COMMENT_ARTICLE("COMMENT_ARTICLE", "评论文章"),

    APPROVAL_FAQ("APPROVAL_FAQ", "关注问答"),
    COMMENT_FAQ("COMMENT_FAQ", "评论问答"),

    FOLLOW_USER("FOLLOW_USER", "用户关注"),
    ;
    private String value;
    private String desc;

    public static MessageTypeEn getEntity(String value) {
        for (MessageTypeEn contentType : values()) {
            if (contentType.getValue().equals(value)) {
                return contentType;
            }
        }
        return null;
    }

    public static MessageTypeEn getEntityByDesc(String desc) {
        for (MessageTypeEn contentType : values()) {
            if (contentType.getDesc().equals(desc)) {
                return contentType;
            }
        }
        return null;
    }

}
