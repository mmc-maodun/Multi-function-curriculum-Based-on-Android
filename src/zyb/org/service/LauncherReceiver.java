package zyb.org.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class LauncherReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		//LauncherReceiver用来监听系统开机事件，如果之前已经设置了启动SetQuietService，即quiet_status为true，
		//而后来因为某种原因关机了，则开机启动SetQuietService
		SharedPreferences preferences = arg0.getSharedPreferences("switch", Context.MODE_MULTI_PROCESS);
		Boolean quiet_status = preferences.getBoolean("switch_quiet", false);
		Boolean remind_status = preferences.getBoolean("switch_remind", false);
		
		Intent quiet_intent = new Intent(arg0,SetQuietService.class);
		
		Intent remind_intent = new Intent(arg0,RemindReceiver.class);
		PendingIntent pi_remind = PendingIntent.getBroadcast(arg0, 0, remind_intent, 0);
		AlarmManager am = (AlarmManager)arg0.getSystemService(Service.ALARM_SERVICE);
		
		if(quiet_status){
			arg0.startService(quiet_intent);
		}
		
		if(remind_status){
			am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, pi_remind);
		}else{
			am.cancel(pi_remind);
		}
	}

}
