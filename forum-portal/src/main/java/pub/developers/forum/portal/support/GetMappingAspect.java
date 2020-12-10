package pub.developers.forum.portal.support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import pub.developers.forum.common.exception.BizException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Qiangqiang.Bian
 * @create 2020/11/2
 * @desc
 **/
@Component
@Aspect
public class GetMappingAspect {

    @Resource
    private HttpServletRequest request;

    @Around("execution(* pub.developers.forum.portal..*.*(..)) && @annotation(getMapping)")
    public Object process(ProceedingJoinPoint joinPoint, GetMapping getMapping) throws Throwable {
        String toastMessage;

        try {
            return joinPoint.proceed();
        } catch (ViewException viewException) {
            toastMessage = viewException.getMessage();
        } catch (BizException bizException) {
            toastMessage = bizException.getMessage();
        } catch (Exception e) {
            toastMessage = "未知异常";
        }

        request.setAttribute("toast", toastMessage);
        return "error";
    }

}
