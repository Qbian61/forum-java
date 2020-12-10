package pub.developers.forum.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pub.developers.forum.common.exception.BizException;

/**
 * @author Qiangqiang.Bian
 * @create 2020/7/30
 * @desc
 **/
@AllArgsConstructor
@Getter
public enum ContentTypeEn {
    HTML("HTML", "html富文本"),
    MARKDOWN("MARKDOWN", "markdown内容"),
    ;

    private String value;
    private String desc;

    public static ContentTypeEn getEntity(String value) {
        for (ContentTypeEn contentType : values()) {
            if (contentType.getValue().equals(value)) {
                return contentType;
            }
        }
        throw new BizException(ErrorCodeEn.CONTENT_TYPE_NOT_EXIST);
    }
}
