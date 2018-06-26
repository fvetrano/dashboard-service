package it.tim.dashboard.web.interceptor;

import feign.RequestTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class HeaderForwardInterceptorTest {

    @Mock
    ApplicationContext applicationContext;

    @InjectMocks
    HeaderForwardInterceptor interceptor;

    @Test
    public void apply() {
        when(applicationContext.getBean(TIMContext.class)).thenReturn(new TIMContext());
        interceptor.apply(new RequestTemplate());
    }
}