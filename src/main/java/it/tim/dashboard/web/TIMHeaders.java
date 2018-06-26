package it.tim.dashboard.web;

public enum TIMHeaders{

    SESSION_ID("SessionID"),
    SESSION_JWT("sessionJWT"),
    BUSINESS_ID("BusinessID"),
    MESSAGE_ID("MessageId"),
    TRANSACTION_ID("TransactionID"),
    SOURCE_SYSTEM("SourceSystem"),
    CHANNEL("Channel"),
    INTERACTION_DATE("interactionDate-Date"),
    INTERACTION_TIME("interactionDate-Time");

    private final String value;

    public String getValue() {
        return value;
    }

    TIMHeaders(String value){
        this.value = value;
    }

}