package temp;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

//MyApplication类用来存储每一个activity，并实现关闭所有activity的操作
public class MyApplication extends Application {
	
//定义容activity容器
	private List<Activity> activityList = new LinkedList<Activity>();
	private static MyApplication instance;
	
	private MyApplication(){}
	//单例设计模式中取得唯一的MyApplication实例
	public static MyApplication getInstance(){
		if(instance == null)
			instance = new MyApplication();
		return instance;
	}
	//添加activity到容器中
	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	//遍历所有的activity并finish
	public void exitApp(){
		for(Activity activity : activityList){
			if(activity != null)
				activity.finish();
		}
		System.exit(0);
	}
	//清空缓存
	@Override
    public void onLowMemory() { 
        super.onLowMemory();     
        System.gc(); 
    }  
	
}
