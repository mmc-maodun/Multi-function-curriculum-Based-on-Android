package temp;
import zyb.org.androidschedule.MainActivity;
import zyb.org.androidschedule.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Adapter;
public class MyAdapter {

	private Context context;
	private MainActivity main;
	private Cursor[] cursor=new Cursor[7];
	private SimpleCursorAdapter[] adapter;
	
	private SharedPreferences preferences;
	
	public MyAdapter(Context context){
		this.context=context;
		main=(MainActivity) context;
	}
	public void test(){
	
	
			
	}
	
}
