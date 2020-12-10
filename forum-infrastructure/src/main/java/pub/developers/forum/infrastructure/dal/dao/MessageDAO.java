package pub.developers.forum.infrastructure.dal.dao;

import org.apache.ibatis.annotations.Param;
import pub.developers.forum.infrastructure.dal.dataobject.MessageDO;

import java.util.List;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/5
 * @desc
 **/
public interface MessageDAO {

    void insert(MessageDO messageDO);

    MessageDO get(Long id);

    List<MessageDO> query(MessageDO messageDO);

    void updateToRead(MessageDO messageDO);

    Long countUnRead(@Param("receiver") String receiver, @Param("read") String read);

    void deleteInTypesAndTitle(@Param("types") List<String> types, @Param("title") String title);
}
