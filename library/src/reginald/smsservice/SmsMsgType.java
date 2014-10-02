package reginald.smsservice;

/**
 * Created by liu on 2014/8/10.
 */
public enum SmsMsgType {

    UNKNOWN(-1),
    RECEIVED(1),
    SENT(2),;

    private final int value;

    private SmsMsgType(int value) {
        this.value = value;
    }

    /**
     * Create a new SmsType using the sms type value represented with integers in the Sms content provider.
     *
     * @param value used to translate into SmsType
     * @return new SmsType associated to the value passed as parameter
     */
    public static SmsMsgType fromValue(int value) {
        for (SmsMsgType smsType : values()) {
            if (smsType.value == value) {
                return smsType;
            }
        }
        throw new IllegalArgumentException("Invalid sms type: " + value);
    }

}
