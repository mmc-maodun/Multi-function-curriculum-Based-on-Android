package zyb.org.version;

import temp.MyApplication;
import zyb.org.androidschedule.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class VersionActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version);
		
		//将该activity加入到MyApplication对象实例容器中
		MyApplication.getInstance().addActivity(this);
		
		TextView backButton = (TextView)findViewById(R.id.backtoSetButton);
		//为返回按钮绑定监听器
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
//				Intent intent = new Intent(AboutUs.this,MainActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//刷新
//				startActivity(intent);
			}
		});
	}
}
