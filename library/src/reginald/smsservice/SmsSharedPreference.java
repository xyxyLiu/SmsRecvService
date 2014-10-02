package reginald.smsservice;

import android.content.SharedPreferences;

/**
 * Created by tony.lxy on 2014/8/12.
 */
public class SmsSharedPreference {

    protected static final String SMSSERVICECLASS = "smsServiceClass";
    protected static final String ISBOOT = "smsIsBoot";
    protected SharedPreferences preferences;
    protected SharedPreferences.Editor editor;

    public static SmsSharedPreference INSTANCE = null;

    public static SmsSharedPreference getInstance(SharedPreferences preferences) {
        if (INSTANCE == null) {
            if (preferences == null) {
                throw new IllegalArgumentException("SMS SharedPreferences param can't be null");
            }
            INSTANCE = new SmsSharedPreference(preferences);
        }
        return INSTANCE;
    }

    private SmsSharedPreference(SharedPreferences preferences) {
        this.preferences = preferences;
        editor = preferences.edit();
    }


    public void setServiceClass(Class<? extends SmsMsgBaseService> serviceClass) {
        editor.putString(SMSSERVICECLASS, serviceClass.getName());
        editor.commit();
    }

    public Class<? extends SmsMsgBaseService> getServiceClass() {
        String className = preferences.getString(SMSSERVICECLASS, "");
        Class<? extends SmsMsgBaseService> serviceClass = null;
        if (!className.equals("")) {
            try {
                serviceClass = (Class<? extends SmsMsgBaseService>) Class.forName(className);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return serviceClass;
        } else {
            return null;
        }
    }


    public boolean getIsBoot() {
        return preferences.getBoolean(ISBOOT, false);
    }

    public void setIsBoot(Boolean isBoot) {
        editor.putBoolean(ISBOOT, isBoot);
        editor.commit();
    }


}
