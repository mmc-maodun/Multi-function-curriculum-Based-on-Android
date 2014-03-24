package temp;

import java.util.Calendar;
import zyb.org.androidschedule.MainActivity;
import zyb.org.androidschedule.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MyDialog {
	
	private EditText course_name;
	private EditText course_address;
	private EditText course_teacher;
	private EditText course_week;
//	private EditText course_time1,course_time2;
	private EditText course_count;
	

	private View view;
	private Context context;
	private LayoutInflater inflater;
	private Builder builder;
	MyAdapter adapter;
	MainActivity main;
	String s1="",s2="",s3="",s4="",s5="",s6="",s7="";
	
	public MyDialog(Context context){
		this.context=context;
		main=(MainActivity) context;
		adapter=new MyAdapter(context);		
	}
	 /*
	 * 点击未编辑的课程列表跳出”添加课程“对话框
	 */
	public void add(final int day,final int n){
		//填装对话框的view
		inflater=LayoutInflater.from(context);
		view=inflater.inflate(R.layout.edit_shedule,null);
		findWidgetes();//取部件
		final Button course_time1=(Button)view.findViewById(R.id.time1);
		final Button course_time2=(Button)view.findViewById(R.id.time2);
		//为两个输入时间的按钮绑定监听器
		course_time1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TimeSet_Dialog(course_time1);
			}
		});		
		course_time2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TimeSet_Dialog(course_time2);
			}
		});
		
		builder=new AlertDialog.Builder(context)
		.setIcon(R.drawable.ic_launcher)
		.setTitle("编辑课程信息")
		.setView(view)
		.setPositiveButton("确认",new OnClickListener(){
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(!(s1=course_name.getText().toString()).equals("")) s1="课程: "+s1;
				if(!(s2=course_address.getText().toString()).equals("")) s2="地点: "+s2;
				if(!(s3=course_teacher.getText().toString()).equals("")) s3="老师: "+s3;
				if(!(s4=course_week.getText().toString()).equals("")) s4="周数: "+s4;
				if(!(s6=course_time1.getText().toString()).equals("")) s6="时间: "+s6;
				if(!(s7=course_time2.getText().toString()).equals("")) ;
				
				if((s5=course_count.getText().toString()).equals("")||s1.equals("")) {
					Toast.makeText(context, "请正确输入课程及节数！", 3000).show();
					return;
				}
				else {
					int i=Integer.parseInt(s5.trim());//i为节数
					for(int m=0;m<i;m++){
						MainActivity.db.update(day,n+m+1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
					}
						
				}
				
				main.cursor[day].requery();
				main.list[day].invalidate();			
			}
			
		})
		.setNegativeButton("取消", new OnClickListener(){
	
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				
			}
			
		});
		builder.create().show();
		
	}
	
	 /*
	 * 点击已编辑的课程列表跳出”修改课程信息或删除课程信息“对话框
	 */
	public void modify(final int day,final int n){
		//填装对话框的view
		inflater=LayoutInflater.from(context);
		view=inflater.inflate(R.layout.edit_shedule,null);
		findWidgetes();//取部件
		final Button course_time1=(Button)view.findViewById(R.id.time1);
		final Button course_time2=(Button)view.findViewById(R.id.time2);
		//为两个输入时间的按钮绑定监听器
		course_time1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TimeSet_Dialog(course_time1);
			}
		});		
		course_time2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TimeSet_Dialog(course_time2);
			}
		});
		
		//从数据库取出旧数据
		main.cursor[day].moveToPosition(n);
		String [] temp=new String[8];
		for(int i=0;i<8;i++) {temp[i]=main.cursor[day].getString(i+1);}
		//将旧数据显示在编辑对话框
		if(!temp[0].equals("")) course_name.setText(temp[0].substring(temp[0].indexOf(":")+2));
		if(!temp[1].equals("")) course_address.setText(temp[1].substring(temp[1].indexOf(":")+2));
		if(!temp[2].equals("")) course_teacher.setText(temp[2].substring(temp[2].indexOf(":")+2));
		if(!temp[3].equals("")) course_week.setText(temp[3].substring(temp[3].indexOf(":")+2));
		if(!temp[4].equals("")) course_time1.setText(temp[4].substring(temp[4].indexOf(":")+2));
		course_time2.setText(temp[5]);
		course_count.setText(temp[6]);
		view.invalidate();
		
		
		builder=new AlertDialog.Builder(context)
		.setIcon(R.drawable.ic_launcher)
		.setTitle("修改课程信息")
		.setView(view)
		.setPositiveButton("确认",new OnClickListener(){
	
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(!(s1=course_name.getText().toString()).equals("")) s1="课程: "+s1;
				if(!(s2=course_address.getText().toString()).equals("")) s2="地点: "+s2;
				if(!(s3=course_teacher.getText().toString()).equals("")) s3="老师: "+s3;
				if(!(s4=course_week.getText().toString()).equals("")) s4="周数: "+s4;
				if(!(s6=course_time1.getText().toString()).equals(""))s6="时间: "+s6;
				if(!(s7=course_time2.getText().toString()).equals(""));
				s5=course_count.getText().toString();
				main.cursor[day].moveToPosition(n);
				int n1=Integer.parseInt(main.cursor[day].getString(7).trim());//课程的总节数
				int n2=Integer.parseInt(main.cursor[day].getString(8).trim());//选中的为该课程的第几节
				Log.i("kkk",main.cursor[day].getString(7));
				//如果没有再次输入节数或节数没有变化，根据选中的为第几节更新前后节的数据即可
				if(s5.equals("")||n1==Integer.parseInt(s5.trim())) {
					switch(n2){
						case 0:
							for(int m=0;m<n1;m++){
								MainActivity.db.update(day,n+m+1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
								}
							break;
							
						case 1:
							MainActivity.db.update(day,n,s1,s2,s3,s4,s5,s6,s7,"0");
							for(int m=1;m<n1;m++){
								MainActivity.db.update(day,n+m,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
								}
							break;		
						case 2:
							MainActivity.db.update(day,n-1,s1,s2,s3,s4,s5,s6,s7,"0");
							MainActivity.db.update(day,n,s1,s2,s3,s4,s5,s6,s7,"1");
							for(int m=2;m<n1;m++){
								MainActivity.db.update(day,n+m-1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
								}
							break;
						case 3:
							for(int m=n2;m>=0;m--){
								MainActivity.db.update(day,n-m+1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(n2-m));
								}
							break;
					}
				
				}
				//若节数有变化，先确定新节数并赋予旧的数据再更新数据
				else{
					int n3=Integer.parseInt(s5.trim());
					//扩充节数
					if(n3>n1){
						
						switch(n2){//更新数据
						case 0:
							for(int m=0;m<n3;m++){
								MainActivity.db.update(day,n+m+1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
								}
							break;
							
						case 1:
							MainActivity.db.update(day,n,s1,s2,s3,s4,s5,s6,s7,"0");
							for(int m=1;m<n3;m++){
								MainActivity.db.update(day,n+m,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
								}
							break;		
						case 2:
							MainActivity.db.update(day,n-1,s1,s2,s3,s4,s5,s6,s7,"0");
							MainActivity.db.update(day,n,s1,s2,s3,s4,s5,s6,s7,"1");
							for(int m=2;m<n3;m++){
								MainActivity.db.update(day,n+m-1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
								}
							break;
						case 3:
							for(int m=n2;m>=0;m--){
								MainActivity.db.update(day,n-m+1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(n2-m));
								}
							break;
							}
					
					}
					//缩减节数：删除旧数据再根据新的节数赋予旧数据最后更新新数据
					if(n3<n1){
						switch(n2){//删除
							case 0:	
								for(int m=0;m<n1;m++){
									MainActivity.db.deleteData(day,n+m+1);
									}
								break;
	
							case 1:
								MainActivity.db.deleteData(day,n);
								for(int m=1;m<n1;m++){
									MainActivity.db.deleteData(day,n+m);
									}
								break;		
							case 2:
								MainActivity.db.deleteData(day,n-1);
								MainActivity.db.deleteData(day,n);
								for(int m=2;m<n1;m++){
									MainActivity.db.deleteData(day,n+m-1);
									}
									break;
							case 3:
								for(int m=n2;m>=0;m--){
									MainActivity.db.deleteData(day,n-m+1);
									}
								break;
							default:
								Toast.makeText(context, "error", 3000).show();
								break;
						}
						
					
						switch(n2){//更新数据
							case 0:
								for(int m=0;m<n3;m++){
									MainActivity.db.update(day,n+m+1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
									}
								break;
								
							case 1:
								MainActivity.db.update(day,n,s1,s2,s3,s4,s5,s6,s7,"0");
								for(int m=1;m<n3;m++){
									MainActivity.db.update(day,n+m,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
									}
								break;		
							case 2:
								MainActivity.db.update(day,n-1,s1,s2,s3,s4,s5,s6,s7,"0");
								MainActivity.db.update(day,n,s1,s2,s3,s4,s5,s6,s7,"1");
								for(int m=2;m<n3;m++){
									MainActivity.db.update(day,n+m-1,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
									}
								break;
							case 3:
								for(int m=0;m<n3;m++){
									MainActivity.db.update(day,n+m-2,s1,s2,s3,s4,s5,s6,s7,Integer.toString(m));
									}
								break;
						}
							
					}
				}
				main.cursor[day].requery();
				main.list[day].invalidate();
			
			}
			
		})
		.setNegativeButton("取消", new OnClickListener(){
	
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				
			}
			
		});
		builder.create().show();
		
	}
	
	private void findWidgetes(){
		course_name=(EditText)view.findViewById(R.id.editText1);
		course_address=(EditText)view.findViewById(R.id.editText2);
		course_teacher=(EditText)view.findViewById(R.id.editText3);
		course_week=(EditText)view.findViewById(R.id.editText4);
//		course_time1=(EditText)view.findViewById(R.id.time1);
//		course_time2=(EditText)view.findViewById(R.id.time2);
		course_count=(EditText)view.findViewById(R.id.jieshu);		
	}
	
	public void TimeSet_Dialog(final TextView text){
		Calendar c = Calendar.getInstance();
		// 创建一个TimePickerDialog实例，并把它显示出来。
		new TimePickerDialog(main,
			// 绑定监听器
			new TimePickerDialog.OnTimeSetListener()
			{
				@Override
				public void onTimeSet(TimePicker tp, int hourOfDay,int minute){
					//获取完整的时间，在只有一位的数字前面加0
					StringBuffer s_hour = new StringBuffer();
					StringBuffer s_minute = new StringBuffer();
					s_hour.append(hourOfDay);
					s_minute.append(minute);
					if(hourOfDay<10){
						s_hour.insert(0,"0");
					}
					if(minute<10){
						s_minute.insert(0,"0");
					}
					//将结果显示在edit中
					text.setText(s_hour.toString() + ":" + s_minute.toString());
				}
			}
		//设置初始时间
		, c.get(Calendar.HOUR_OF_DAY)
		, c.get(Calendar.MINUTE)
		//true表示采用24小时制
		, true).show();
	}
}
