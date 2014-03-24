package temp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper{
	
	private final static String DB_NAME="myBase";
	private final static String[] TB_NAME={"Mon","Tue","Wed","Thur","Fri","Sat","Sun"};
	public  final static String ID="_id";
	public final static String CLASS="classes";
	public final static String LOCA="location";
	public final static String TEACHER="teacher";
	public final static String ZHOUSHU="zhoushu";
	public final static String JIESHU="jieshu";
	public final static String TIME1="time1";
	public final static String TIME2="time2";
	public final static String WHICH="which";
	
	public DataBase(Context context){
		super(context,DB_NAME,null,1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		for(int i=0;i<7;i++){
		String sql="CREATE TABLE "+TB_NAME[i]+" (_id INTEGER primary key autoincrement,classes varchar(70),location varchar(70)," +
				"teacher varchar(70),zhoushu varchar(70),time1 varchar(70),time2 varchar(70),jieshu varchar(70),which varchar(70))";
		db.execSQL(sql);	
		}
	
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oleVersion, int newVersion) {
		for(int i=0;i<7;i++){
			String sql="DROP TABLE IF EXISTS "+TB_NAME[i];
			db.execSQL(sql);
		}
		onCreate(db);
		
	}
	public Cursor select(int i){
		
		SQLiteDatabase db=DataBase.this.getReadableDatabase();
		Cursor cursor=db.query(TB_NAME[i],null,null,null,null,null,null);
		return cursor;
	}
	public  long insert(int i,String cla,String loca,String tea,String zhou,String jie,String time1,String time2,String which){
		SQLiteDatabase db=DataBase.this.getWritableDatabase();
		ContentValues cv=new ContentValues();		
		cv.put(CLASS,cla);
		cv.put(LOCA, loca);
		cv.put(TEACHER,tea);
		cv.put(ZHOUSHU,zhou);
		cv.put(JIESHU,jie);
		cv.put(TIME1,time1);
		cv.put(TIME2,time2);
		cv.put(WHICH,which);
		long row=db.insert(TB_NAME[i],null,cv);
		return row;
	}
	public void update(int i,int _id,String cla,String loca,String tea,String zhou,String jie,String time1,String time2,String which){
		SQLiteDatabase db=DataBase.this.getWritableDatabase();
		String where="_id = ?";
		String[] whereValues={Integer.toString(_id)};
		ContentValues cv=new ContentValues();
		if(!cla.equals("")) cv.put(CLASS,cla);
		if(!loca.equals("")) cv.put(LOCA, loca);
		if(!tea.equals("")) cv.put(TEACHER,tea);
		if(!zhou.equals("")) cv.put(ZHOUSHU,zhou);
		if(!jie.equals("")) cv.put(JIESHU,jie);
		if(!time1.equals("")) cv.put(TIME1,time1);
		if(!time2.equals("")) cv.put(TIME2,time2);
		if(!which.equals("")) cv.put(WHICH,which);
		db.update(TB_NAME[i], cv, where, whereValues);
	}
	public void deleteData(int i,int _id){
		SQLiteDatabase db=DataBase.this.getWritableDatabase();
		String where="_id = ?";
		String[] whereValues={Integer.toString(_id)};
		ContentValues cv=new ContentValues();
		cv.put("classes","");
		cv.put("location","");
		cv.put("teacher","");
		cv.put("zhoushu","");
		cv.put("jieshu","");
		cv.put("time1","");
		cv.put("time2","");
		cv.put("which","");
		db.update(TB_NAME[i], cv, where, whereValues);
	}
	public void delete(int i,int _id){
		SQLiteDatabase db=this.getWritableDatabase();
		String where="_id = ?";
		String[] whereValues={Integer.toString(_id)};
		db.delete(TB_NAME[i], where, whereValues);
	}
	public  void createTable(int j){

				for(int i=1;i<=12;i++)
					insert(j,"", "", "","","","","","");
	
	}
	
}
