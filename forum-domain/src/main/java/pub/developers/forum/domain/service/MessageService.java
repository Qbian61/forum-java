package pub.developers.forum.domain.service;

import pub.developers.forum.domain.entity.Message;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/22
 * @desc
 **/
public interface MessageService {

    void send(Message message);

}
