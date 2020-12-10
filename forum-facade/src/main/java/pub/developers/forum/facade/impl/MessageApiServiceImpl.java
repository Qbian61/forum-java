package pub.developers.forum.facade.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.message.MessagePageRequest;
import pub.developers.forum.api.response.message.MessagePageResponse;
import pub.developers.forum.api.service.MessageApiService;
import pub.developers.forum.app.manager.MessageManager;
import pub.developers.forum.facade.support.ResultModelUtil;
import pub.developers.forum.facade.validator.MessageValidator;
import pub.developers.forum.facade.validator.PageRequestModelValidator;

import javax.annotation.Resource;

/**
 * @author Qiangqiang.Bian
 * @create 2020/12/5
 * @desc
 **/
@Service
public class MessageApiServiceImpl implements MessageApiService {

    @Resource
    private MessageManager messageManager;

    @Override
    public ResultModel<PageResponseModel<MessagePageResponse>> page(PageRequestModel<MessagePageRequest> pageRequestModel) {
        PageRequestModelValidator.validator(pageRequestModel);
        pageRequestModel.setFilter(JSON.parseObject(JSON.toJSONString(pageRequestModel.getFilter()), MessagePageRequest.class));
        MessageValidator.page(pageRequestModel.getFilter());

        return ResultModelUtil.success(messageManager.page(pageRequestModel));
    }

    @Override
    public ResultModel markIsRead(Long messageId) {
        messageManager.markIsRead(messageId);

        return ResultModelUtil.success();
    }

    @Override
    public ResultModel<Long> countUnRead() {
        return ResultModelUtil.success(messageManager.countUnRead());
    }
}
