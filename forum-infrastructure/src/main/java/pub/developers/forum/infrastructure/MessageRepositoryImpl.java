package pub.developers.forum.infrastructure;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.common.enums.MessageReadEn;
import pub.developers.forum.common.enums.MessageTypeEn;
import pub.developers.forum.common.model.PageRequest;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.Message;
import pub.developers.forum.domain.repository.MessageRepository;
import pub.developers.forum.infrastructure.dal.dao.MessageDAO;
import pub.developers.forum.infrastructure.dal.dataobject.MessageDO;
import pub.developers.forum.infrastructure.transfer.MessageTransfer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Qiangqiang.Bian
 * @create 20/10/22
 * @desc
 **/
@Repository
public class MessageRepositoryImpl implements MessageRepository {

    @Resource
    private MessageDAO messageDAO;

    @Override
    public void save(Message message) {
        String receiver = message.getReceiver().getId();
        String sender = message.getSender().getId();
        if (receiver.equals(sender)) {
            return;
        }

        MessageDO messageDO = MessageDO.builder()
                .channel(message.getChannel().getValue())
                .type(message.getType().getValue())
                .read(message.getRead().getValue())
                .sender(sender)
                .senderType(message.getSender().getType().getValue())
                .receiver(receiver)
                .receiverType(message.getReceiver().getType().getValue())
                .title(message.getTitle())
                .content(message.getContent())
                .contentType(message.getContentType().getValue())
                .build();
        messageDO.initBase();

        messageDAO.insert(messageDO);
    }

    @Override
    public Message get(Long id) {
        return MessageTransfer.toMessage(messageDAO.get(id));
    }

    @Override
    public void updateToRead(Message message) {
        MessageDO messageDO = MessageDO.builder()
                .read(message.getRead().getValue())
                .build();
        messageDO.setId(message.getId());
        messageDAO.updateToRead(messageDO);
    }

    @Override
    public Long countUnRead(Long receiver) {
        return messageDAO.countUnRead(receiver.toString(), MessageReadEn.NO.getValue());
    }

    @Override
    public void deleteInTypesAndTitle(List<MessageTypeEn> typeEns, String title) {
        List<String> types = SafesUtil.ofList(typeEns).stream().map(MessageTypeEn::getValue).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(types) || ObjectUtils.isEmpty(title)) {
            return;
        }
        messageDAO.deleteInTypesAndTitle(types, title);
    }

    @Override
    public PageResult<Message> page(PageRequest<Message> pageRequest) {
        PageHelper.startPage(pageRequest.getPageNo(), pageRequest.getPageSize());

        Message message = pageRequest.getFilter();
        MessageDO messageDO = MessageDO.builder().build();
        if (!ObjectUtils.isEmpty(message.getChannel())) {
            messageDO.setChannel(message.getChannel().getValue());
        }
        if (!ObjectUtils.isEmpty(message.getType())) {
            messageDO.setType(message.getType().getValue());
        }
        if (!ObjectUtils.isEmpty(message.getSender())) {
            messageDO.setSender(message.getSender().getId());
        }
        if (!ObjectUtils.isEmpty(message.getReceiver())) {
            messageDO.setReceiver(message.getReceiver().getId());
        }

        List<MessageDO> messageDOS = messageDAO.query(messageDO);
        PageInfo<MessageDO> pageInfo = new PageInfo<>(messageDOS);

        if (ObjectUtils.isEmpty(messageDOS)) {
            return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), new ArrayList<>());
        }

        return PageResult.build(pageInfo.getTotal(), pageInfo.getSize(), MessageTransfer.toMessages(messageDOS));
    }
}
