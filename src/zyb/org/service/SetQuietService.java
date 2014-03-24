package zyb.org.service;

import java.util.Timer;
import java.util.TimerTask;
import temp.DataBase;
import temp.ShareMethod;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.IBinder;

public class SetQuietService extends Service {
	
	//保存时间，temp[day][row][time]表示第day+1个tab选项卡中的第row+1个行中第time+1个表示时间的字符串
	String[][][] temp = new String[7][12][2];
	
	//取得数据库，并定义保存每张表数据的cursor集合
	DataBase db = new DataBase(SetQuietService.this);
	Cursor[] cursor = new Cursor[7];
		
	@Override
	public IBinder onBind(Intent arg0){
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		 //声明一个获取系统音频服务的类的对象
		 final AudioManager audioManager = (AudioManager)getSystemService(Service.AUDIO_SERVICE);
		 //获取手机之前设置好的铃声模式
		 final int orgRingerMode = audioManager.getRingerMode(); 
		
		//每隔一分钟从数据库中取以此数据，获得一次当前的时间，并与用用户输入的上下课时间比较，如果相等，则执行相应的静音或者恢复铃声操作
		new Timer().schedule(new TimerTask() {		
			@Override
			public void run() {
				
				 //取出数据库中每日的数据，保存在cursor数组中
				 for(int i=0;i<7;i++){
					cursor[i]=db.select(i);					
				}
				
				//从数据库取出用户输入的上课和下课时间，用来设置上课自动静音
				for(int day=0;day<7;day++){ 
					for(int row=0;row<12;row++){
						cursor[day].moveToPosition(row);
						for(int time=0;time<2;time++){
							temp[day][row][time] = cursor[day].getString(time+5);
						}
			 			if(!temp[day][row][0].equals(""))
							temp[day][row][0] = temp[day][row][0].substring(temp[day][row][0].indexOf(":")+2);
					}
				}
								
				//获取当前的是星期几
				int currentDay = ShareMethod.getWeekDay();
				for(int j=0;j<12;j++){
					//获取手机当前的铃声模式
					int currentRingerMode = audioManager.getRingerMode();  
					if(temp[currentDay][j][0].equals(ShareMethod.getTime()) && currentRingerMode!=AudioManager.RINGER_MODE_VIBRATE){
						audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//						System.out.println("class is on");
					}
					if(temp[currentDay][j][1].equals(ShareMethod.getTime()) && currentRingerMode==AudioManager.RINGER_MODE_VIBRATE){
						audioManager.setRingerMode(orgRingerMode);
//						System.out.println("class is over");
					}   
				}
				
			 }
		}, 0, 60000);		
		return super.onStartCommand(intent, flags, startId);
	}	
		
}
