package pub.developers.forum.domain.service;

import pub.developers.forum.domain.entity.Message;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/7
 * @desc
 **/
public interface MailService {

    void sendHtml(Message mailMessage);

    void sendText(Message mailMessage);

}
