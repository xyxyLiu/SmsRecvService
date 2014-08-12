package com.example.SmsDemo;
import android.content.SharedPreferences;
/**
 * Created by tony.lxy on 2014/8/12.
 */
public class SmsSharedPreference {

		private static final String SMSSERVICECLASS = "smsServiceClass";
		private static final String ISBOOT = "smsIsBoot";
		private SharedPreferences preferences;

		SmsSharedPreference(SharedPreferences preferences) {
			if (preferences == null) {
				throw new IllegalArgumentException("SharedPreferences param can't be null");
			}
			this.preferences = preferences;
		}


		public void setServiceClass(Class<? extends  BaseSmsMsgService> serviceClass) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString(SMSSERVICECLASS,serviceClass.getName());
			editor.commit();
		}

		public Class<? extends  BaseSmsMsgService> getServiceClass() {
			 String className = preferences.getString(SMSSERVICECLASS, "");
			 Class<? extends  BaseSmsMsgService> serviceClass = null;
			 if(!className.equals(""))
			 {
				 try{
					 serviceClass = (Class<? extends  BaseSmsMsgService>)Class.forName(className);
				 }catch(Exception e){
					 e.printStackTrace();
					 return null;
				 }
				 return serviceClass;
			 }
			 else
			 {
				return null;
			 }
		}


		public boolean getIsBoot()
		{
			return preferences.getBoolean(ISBOOT, false);
		}

		public void setIsBoot(Boolean isBoot)
		{
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean(ISBOOT,isBoot);
			editor.commit();
		}


}
