package pub.developers.forum.common.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/3
 * @desc
 **/
@Data
@ConfigurationProperties(prefix = "custom-config.view.global")
@Component
public class GlobalViewConfig {

    private String cdnImgStyle;

    private String websiteRecord;

    private Integer pageSize;

    private String websiteName;

    private String websiteLogoUrl;

    private String websiteFaviconIconUrl;

    private String contactMeWxQrCode;

    private String contactMeTitle;

    private String githubClientId;

    private String githubOauthUrl;

    public String getGithubOauthUrl() {
        // https://github.com/login/oauth/authorize?client_id=5c00b7f2065217732aa3&scope=user
        return  "https://github.com/login/oauth/authorize?client_id=" + githubClientId + "&scope=user";
    }
}
