package zyb.org.editschedule;
import zyb.org.androidschedule.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Vibrator;

public class RemindActivity extends Activity
{
	private Vibrator vibrator;
		
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		SharedPreferences pre = getSharedPreferences("time", Context.MODE_MULTI_PROCESS);
		int advance_time = pre.getInt("time_choice", 30);
				
		//获取系统的vibrator服务，并设置手机振动2秒
		vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);

		// 创建一个对话框
		final AlertDialog.Builder builder= new AlertDialog.Builder(RemindActivity.this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("温馨提示");
			builder.setMessage("童鞋，还有" + advance_time + "分钟就要上课了哦！");
			builder.setPositiveButton(
				"确定" ,
				new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog , int which)
					{
						// 结束该Activity
						RemindActivity.this.finish();
					}
				}
			)
			.show();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// 结束该Activity
		RemindActivity.this.finish();	
	}
	
	
}
