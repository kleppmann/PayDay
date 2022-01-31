package az.ibar.payday.ms.stock.config;

import az.ibar.payday.ms.stock.model.HeaderKeys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Component
public class InterceptorConfig extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestId = request.getHeader(HeaderKeys.HEADER_REQUEST_ID);
        MDC.put(HeaderKeys.HEADER_REQUEST_ID, requestId != null ? requestId : UUID.randomUUID().toString());

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        MDC.clear();

        super.postHandle(request, response, handler, modelAndView);
    }
}