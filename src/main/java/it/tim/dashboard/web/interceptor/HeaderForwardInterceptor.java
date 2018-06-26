package it.tim.dashboard.web.interceptor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HeaderForwardInterceptor implements RequestInterceptor{

    @Autowired
    private ApplicationContext context;

    @Override
    public void apply(RequestTemplate template) {

    	
    	template.header("Accept-Encoding", "identity");
    	
        TIMContext timContext = context.getBean(TIMContext.class);
        Map<String,String> headersMap = timContext.getMapValues();

        for (Map.Entry<String, String> entry : headersMap.entrySet()) {
            template.header(entry.getKey(),entry.getValue());
        }

    }
}
