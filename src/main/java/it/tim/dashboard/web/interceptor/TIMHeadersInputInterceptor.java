package it.tim.dashboard.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.tim.dashboard.web.TIMHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

@Slf4j
@Component
public class TIMHeadersInputInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String CF_PIVA_CLAIM = "cf_piva";


    private final TIMContext ctx;

    @Autowired
    public TIMHeadersInputInterceptor(TIMContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ctx.setSessionId(request.getHeader(TIMHeaders.SESSION_ID.getValue()));
        ctx.setSessionJWT(request.getHeader(TIMHeaders.SESSION_JWT.getValue()));
        ctx.setBusinessId(request.getHeader(TIMHeaders.BUSINESS_ID.getValue()));
        ctx.setMessageId(request.getHeader(TIMHeaders.MESSAGE_ID.getValue()));
        ctx.setTransactionId(request.getHeader(TIMHeaders.TRANSACTION_ID.getValue()));
        ctx.setChannel(request.getHeader(TIMHeaders.CHANNEL.getValue()));
        ctx.setInteractionDate(request.getHeader(TIMHeaders.INTERACTION_DATE.getValue()));
        ctx.setInteractionTime(request.getHeader(TIMHeaders.INTERACTION_TIME.getValue()));

        String jwtHeader = request.getHeader(TIMHeaders.SESSION_JWT.getValue());

        if (isNotEmpty(jwtHeader)){
            try {
                Jwt decode = JwtHelper.decode(jwtHeader);
                Map claims = objectMapper.readValue(decode.getClaims(), Map.class);

                ctx.setJwt(decode);
                ctx.setCfPiva((String)claims.get(CF_PIVA_CLAIM));

            }catch(Exception e){
                log.error("Invalid JWT Token",e);
                return badInputParams(response);
            }
        }else{
            log.error("Missing SESSION_JWT header");
            return  badInputParams(response);
        }


        return true;
    }

    private boolean badInputParams(HttpServletResponse response) throws IOException{
        response.sendError(400,"Bad Input params");
        return false;
    }

}
