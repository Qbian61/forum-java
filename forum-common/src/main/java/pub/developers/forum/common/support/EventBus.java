package pub.developers.forum.common.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/22
 * @desc
 **/
@Slf4j
@Component
public class EventBus {

    /**
     * 消息类型枚举
     */
    public enum Topic {
        /**
         *
         */
        USER_LOGIN,
        USER_REGISTER,
        USER_LOGOUT,
        USER_FOLLOW,
        USER_CANCEL_FOLLOW,

        ARTICLE_CREATE,
        ARTICLE_UPDATE,

        POSTS_INFO,
        POSTS_DELETE,

        FAQ_CREATE,
        FAQ_UPDATE,

        APPROVAL_CREATE,

        COMMENT_CREATE,
        ;
    }

    /**
     * 事件/事件处理 映射
     */
    private static final Map<Topic, List<EventBus.EventHandler>> EVENT_HANDLER_MAP = new ConcurrentHashMap<>();

    /**
     * 线程池
     */
    private final static ExecutorService executorService = ExecutorFactory.getExecutorService(EventBus.class, 4);

    @PreDestroy
    public void post() {
        executorService.shutdown();
    }

    /**
     * 触发事件，默认异步执行
     * @param eventEn
     * @param message
     */
    public static void emit(final Topic eventEn, final Object message) {
        processEmitEvent(eventEn, message, true);
    }

    /**
     * 同步触发事件
     * @param eventEn
     * @param message
     */
    public static void emitSync(final Topic eventEn, final Object message) {
        processEmitEvent(eventEn, message, false);
    }

    /**
     * 执行处理的事件
     * @param eventEn
     * @param message
     */
    private static void processEmitEvent(Topic eventEn, Object message, Boolean async) {
        log.info("Bus on event=[{}] message=[{}]", eventEn, message);
        List<EventBus.EventHandler> eventHandlers = EVENT_HANDLER_MAP.get(eventEn);
        if (null == eventHandlers) {
            log.warn("emit [{}] event, but not handler.", eventEn);
            return;
        }

        eventHandlers.forEach(eventHandler -> {
            if (async) {
                executorService.submit(() -> {
                    try {
                        eventHandler.onMessage(message);
                    } catch (Exception e) {
                        log.error("handler [{}] async process event [{}] error.", eventHandler.getClass(), eventEn, e);
                    }
                });
            } else {
                try {
                    eventHandler.onMessage(message);
                } catch (Exception e) {
                    log.error("handler [{}] sync process event [{}] error.", eventHandler.getClass(), eventEn, e);
                }
            }
        });
    }

    /**
     * 监听事件
     * @param eventEn
     * @param eventHandler
     */
    private static synchronized void on(Topic eventEn, EventBus.EventHandler eventHandler) {
        List<EventBus.EventHandler> eventHandlers = EVENT_HANDLER_MAP.get(eventEn);

        if (eventHandlers == null) {
            eventHandlers = new ArrayList<>();
        }

        eventHandlers.add(eventHandler);
        EVENT_HANDLER_MAP.put(eventEn, eventHandlers);
    }

    public static abstract class EventHandler<T> implements InitializingBean {

        public abstract Topic topic();

        public abstract void onMessage(T message);

        @Override
        public void afterPropertiesSet() throws Exception {
            EventBus.on(topic(), this);
        }
    }
}
