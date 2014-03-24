package zyb.org.editschedule;

import temp.MyApplication;
import zyb.org.about.AboutUsActivity;
import zyb.org.androidschedule.R;
import zyb.org.service.RemindReceiver;
import zyb.org.version.VersionActivity;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SetActivity extends Activity {
	
	//声明一个SharedPreferences对象，用来保存switch组件的开关信息
	private SharedPreferences preferences = null;
	//editor对象用来向preferences中写入数据
	private SharedPreferences.Editor editor = null;	
	
	//声明一个SharedPreferences对象，用来保存time_choice的值
	private SharedPreferences pre = null;
	//pre_editor对象用来向pre中写入数据
	private SharedPreferences.Editor pre_editor = null;	
	
	//声明一个AlarmManager对象，用来开启课前提醒服务
	private AlarmManager alarmManager = null;
	//声明一个PendingIntent对象，用来指定alarmManager要启动的组件
	private PendingIntent pi = null;
	private Intent alarm_receiver = null;
	 
	//定义单选列表对话狂的id，该对话框用于显示课前提醒时间的可选项
	final int SINGLE_DIALOG = 0x113;
	//定义选中的时间
	private int time_choice = 0;
	
	private Switch switch_quietButton;
	private Switch switch_remindButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		//将该activity加入到MyApplication对象实例容器中
		MyApplication.getInstance().addActivity(this);
		
		//声明一个获取系统音频服务的类的对象
		final AudioManager audioManager = (AudioManager)getSystemService(Service.AUDIO_SERVICE);
		//从MainAcivity中获取原始设置的铃声模式
		Intent intent = getIntent();
		final int orgRingerMode = intent.getIntExtra("mode_ringer", AudioManager.RINGER_MODE_NORMAL);
		//获取系统的闹钟定时服务
		alarmManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
		
		//指定alarmManager要启动的组件
		alarm_receiver = new Intent(SetActivity.this,RemindReceiver.class);
//		alarm_receiver.putExtra("anvance_remindtime", time_choice);
		pi = PendingIntent.getBroadcast(SetActivity.this, 0, alarm_receiver, 0);
		
		//取出各组件
		TextView backButton = (TextView)findViewById(R.id.backtoMainButton);
		switch_quietButton = (Switch)findViewById(R.id.switch_quiet);
		switch_remindButton = (Switch)findViewById(R.id.switch_remind);
		
		//这里模式一定要设置为MODE_MULTI_PROCESS，否则即使相应的xml文件中数据有更新，RemindReceiver中也不能获取更新后的数据，而是一直获取上次的数据， 除非清空缓存
		this.pre = SetActivity.this.getSharedPreferences("time", Context.MODE_MULTI_PROCESS);
		this.pre_editor = pre.edit();	
		
		//指定该SharedPreferences数据可以跨进称调用
		this.preferences = SetActivity.this.getSharedPreferences("switch", Context.MODE_MULTI_PROCESS);
		this.editor = preferences.edit();		
		//每次创建该activity时，从preferences中读取switch_quietButton和switch_remindButton的开关信息的数据
		Boolean quiet_status = preferences.getBoolean("switch_quiet", false);
		Boolean remind_status = preferences.getBoolean("switch_remind", false);
		switch_quietButton.setChecked(quiet_status);
		switch_remindButton.setChecked(remind_status);		
		
		//为返回按钮绑定监听器
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
//				Intent intent = new Intent(Set.this,MainActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//刷新
//				startActivity(intent);
			}
		});
		
		//为自动静音开关按钮绑定监听器
		switch_quietButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//启动自动静音的service
				Intent intent = new Intent();
				intent.setAction("zyb.org.service.QUIET_SERVICE");
				
				if(isChecked){
					if(startService(intent) != null)
						Toast.makeText(SetActivity.this, "成功开启，上课期间的来电将自动转为振动模式", 3000).show();
					else{
						Toast.makeText(SetActivity.this, "未能成功开启，请重新尝试", 3000).show();
						switch_quietButton.setChecked(false);
					}
				}
				else{
					if(stopService(intent))
						Toast.makeText(SetActivity.this, "成功关闭，恢复到原来的响铃模式", 3000).show();
					else{
						Toast.makeText(SetActivity.this, "未能成功关闭，请重新尝试", 3000).show();
						switch_quietButton.setChecked(true);
					}
					audioManager.setRingerMode(orgRingerMode);
				}
				//将开关信息数据保存进preferences中
				SetActivity.this.editor.putBoolean("switch_quiet", isChecked);
				editor.commit();
			}
		});
		
		//为课前提醒开关按钮绑定监听器
		switch_remindButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					showDialog(SINGLE_DIALOG);
				}
				else{
					alarmManager.cancel(pi);
				}
				//将开关信息数据保存进preferences中
				SetActivity.this.editor.putBoolean("switch_remind", isChecked);
				editor.commit();
			}
		});
		
	}
				
	@Override
	//该方法返回的Dialog将被showDialog()方法回调
	protected Dialog onCreateDialog(int id, Bundle args) {
		//判断生成何种类型的对话框
		if(id == SINGLE_DIALOG){
			Builder b = new AlertDialog.Builder(this);
			// 设置对话框的标题
			b.setTitle("选择课前提醒时间");
			// 为对话框设置多个列表，参数-1表示默认不选中任何选项
			b.setSingleChoiceItems(R.array.set_remind, -1, new DialogInterface.OnClickListener(){
				
				@Override
				public void onClick(DialogInterface dialog,
					int which){
					switch (which){
						case 0:
							time_choice = 5;
							break;
						case 1:						
							time_choice = 10;
							break;
						case 2:
							time_choice = 20;
							break;
						case 3:
							time_choice = 30;
							break;
						case 4:
							time_choice = 40;
							break;
						case 5:
							time_choice = 50;
							break;
						case 6:
							time_choice = 60;
							break;	
					}
				}
			});
			// 添加一个“确定”按钮，用于关闭该对话框
			b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					System.out.println("SetActivity:" + time_choice);
					if(time_choice == 0){
						Toast.makeText(SetActivity.this, "请选择课前提醒的时间", 3000).show();
						switch_remindButton.setChecked(false);
					}else{
						SetActivity.this.pre_editor.putInt("time_choice", time_choice);
						pre_editor.commit();
						//从当前时间开始，每隔一分钟启动一次pi指定的组件，即发送一次广播
						alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pi);
						Toast.makeText(SetActivity.this, "设置成功，系统将在课前" + time_choice + "分钟提醒您", Toast.LENGTH_LONG).show();
					}
				}
			});
			//添加一个“取消”按钮
			b.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch_remindButton.setChecked(false);	
				}
			});
			// 创建对话框
			return b.create();
	 	}
		else
			return null;
	}



	//如果点击“关于我们”的TextView，则跳转
	public void click_us(View v){
		Intent intent = new Intent(SetActivity.this, AboutUsActivity.class);
		startActivity(intent);
	}
	//如果点击“版本支持”的TextView，则跳转
	public void click_version(View v){
		Intent intent = new Intent(SetActivity.this, VersionActivity.class);
		startActivity(intent);
	}
	public void click_revision(View v){
		Log.i("MyDebug", "revision");
	} 
}
