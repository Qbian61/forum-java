package pub.developers.forum.common.support;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Qiangqiang.Bian
 * @create 2020/10/22
 * @desc
 **/
@Slf4j
public class ExecutorFactory {

    public static ExecutorService getExecutorService(Class<?> cls, int fixedThreads) {
        return Executors.newFixedThreadPool(fixedThreads
                , new ThreadFactoryBuilder()
                        .setNameFormat(cls.getSimpleName() + "-%d")
                        .setUncaughtExceptionHandler(ExecutorFactory.getCommonHandler())
                        .setDaemon(false)
                        .build())
                ;
    }

    private static Thread.UncaughtExceptionHandler getCommonHandler() {
        return (t, ex) -> {
            log.error("GroupName:[{}], ThreadName:[{}]. "
                    , t.getThreadGroup().getName()
                    , t.getName());
            if (ex != null) {
                log.error("Cause:[{}], Message:[{}]. "
                        , ex.getCause()
                        , ex.getMessage()
                        , ex);
            }
        };
    }

}

