package az.ibar.payday.ms.stock.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;

public class FeignTraceRequestInterceptor implements RequestInterceptor {

    private static final String HTTP_HEADER_FOR_LOG_PREFIX = "PD_LOG_";

    @Override
    public void apply(RequestTemplate template) {
        if (MDC.getCopyOfContextMap() != null) {
            MDC.getCopyOfContextMap()
                    .forEach((k, v) -> template.header(HTTP_HEADER_FOR_LOG_PREFIX + k, v));
        }
    }
}