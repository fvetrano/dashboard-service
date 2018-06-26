package it.tim.dashboard.web.interceptor;

import it.tim.dashboard.web.TIMHeaders;
import lombok.Data;
import org.springframework.security.jwt.Jwt;

import java.util.HashMap;
import java.util.Map;

@Data
public class TIMContext {

    private String sessionId;
    private String businessId;
    private String messageId;
    private String transactionId;
    private String sourceSystem;
    private String channel;
    private String interactionDate;
    private String interactionTime;
    private String sessionJWT;
    private Jwt jwt;

    private String cfPiva;


    public Map getMapValues(){

        HashMap map = new HashMap();

        map.put(TIMHeaders.CHANNEL.name(), channel);
        map.put(TIMHeaders.BUSINESS_ID.name(), businessId);
        map.put(TIMHeaders.INTERACTION_DATE.name(), interactionDate);
        map.put(TIMHeaders.INTERACTION_TIME.name(), interactionTime);
        map.put(TIMHeaders.MESSAGE_ID.name(), messageId);
        map.put(TIMHeaders.TRANSACTION_ID.name(), transactionId);
        map.put(TIMHeaders.SESSION_ID.name(), sessionId);
        map.put(TIMHeaders.SOURCE_SYSTEM.name(), "CBE");
        map.put(TIMHeaders.SESSION_JWT.name(), sessionJWT);

        return map;

    }


}
