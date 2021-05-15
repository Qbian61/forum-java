package pub.developers.forum.domain.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Qiangqiang.Bian
 * @create 2021/5/15
 * @desc
 **/
public interface GithubService {

    JSONObject getUserInfo(String code);

}
