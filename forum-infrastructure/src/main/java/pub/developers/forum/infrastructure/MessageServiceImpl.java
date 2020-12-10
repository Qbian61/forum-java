package pub.developers.forum.infrastructure;

import org.springframework.stereotype.Service;
import pub.developers.forum.common.enums.MessageChannelEn;
import pub.developers.forum.common.enums.MessageContentTypeEn;
import pub.developers.forum.domain.entity.Message;
import pub.developers.forum.domain.repository.MessageRepository;
import pub.developers.forum.domain.service.MailService;
import pub.developers.forum.domain.service.MessageService;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/22
 * @desc
 **/
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageRepository messageRepository;

    @Resource
    private MailService mailService;

    @Override
    public void send(Message message) {
        // 邮件
        if (MessageChannelEn.MAIL.equals(message.getChannel())) {
            if (MessageContentTypeEn.HTML.equals(message.getContentType())) {
                mailService.sendHtml(message);
            }
            if (MessageContentTypeEn.TEXT.equals(message.getContentType())) {
                mailService.sendText(message);
            }
        }

        // 站内信
        if (MessageChannelEn.STATION_LETTER.equals(message.getChannel())) {
            // do nothing
        }

        messageRepository.save(message);
    }

}
