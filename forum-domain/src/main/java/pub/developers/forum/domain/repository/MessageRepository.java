package pub.developers.forum.domain.repository;

import pub.developers.forum.common.enums.MessageTypeEn;
import pub.developers.forum.common.model.PageRequest;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.domain.entity.Message;

import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/22
 * @desc
 **/
public interface MessageRepository {

    void save(Message message);

    Message get(Long id);

    PageResult<Message> page(PageRequest<Message> pageRequest);

    void updateToRead(Message message);

    Long countUnRead(Long receiver);

    void deleteInTypesAndTitle(List<MessageTypeEn> typeEns, String title);
}
