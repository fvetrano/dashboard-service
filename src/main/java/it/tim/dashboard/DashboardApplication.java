package it.tim.dashboard;

import it.tim.dashboard.web.interceptor.TIMContext;
import it.tim.dashboard.web.interceptor.TIMHeadersInputInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@EnableHystrixDashboard
@EnableAspectJAutoProxy
public class DashboardApplication extends WebMvcConfigurerAdapter {


    public static void main(String[] args) {
        SpringApplication.run(DashboardApplication.class, args);
    }

    private final TIMHeadersInputInterceptor timHeadersInputInterceptor;

    @Autowired
    public DashboardApplication(TIMHeadersInputInterceptor timHeadersInputInterceptor) {
        this.timHeadersInputInterceptor = timHeadersInputInterceptor;
    }

    @Bean
    @SuppressWarnings("unused")
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.timHeadersInputInterceptor).addPathPatterns("/**/api/**/");
    }



    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public TIMContext produceTIMHeadersContext(){
        return new TIMContext();
    }

}
