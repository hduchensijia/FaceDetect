package com.gem.hsx.sqlite; 
import android.content.ContentValues; 
import android.content.Context; 
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase; 
import android.database.sqlite.SQLiteOpenHelper;
public class daliydata extends SQLiteOpenHelper 
{
	private final static String DATABASE_NAME = "index_show.db"; 
	private final static int DATABASE_VERSION = 1; 
	private  static String TABLE_NAME = "data";
	public final static String DATE = "date";
	public final static String HEALTH_VALUE = "health_value";
	public final static String BOOK_AUTHOR = "map_y"; 
	public final static String BOOK_DESC = "map_desc"; 
	public daliydata(Context context,String username) { 
		// TODO Auto-generated constructor stub
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		TABLE_NAME=username;
		} 
	//创建table @Override
	public void onCreate(SQLiteDatabase db)
	{ 
		String sql = "CREATE TABLE if not exists " + TABLE_NAME + " (" + DATE + " text, " + HEALTH_VALUE + " INTEGER);";
		db.execSQL(sql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{ 
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME; 
		db.execSQL(sql); 
		onCreate(db);
	} 
	
	public Cursor select()
	{ 
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db .query(TABLE_NAME, null, null, null, null, null, null);
			return cursor; 
	} 
	//增加操作
	public long insert(String date,int value) 
	{
		SQLiteDatabase db = this.getWritableDatabase(); 
	/* ContentValues */
		ContentValues cv = new ContentValues(); 			
		cv.put(DATE, date);
		cv.put(HEALTH_VALUE, value);
		long row = db.insert(TABLE_NAME, null, cv); return row; } 
	
	
	//删除操作
	public void delete(String date) 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String where = DATE + " = ?";
		String[] whereValue ={ date}; 
		db.delete(TABLE_NAME, where, whereValue);
	} 
	//修改操作 
	public void update(int id, String value)
	{
		SQLiteDatabase db = this.getWritableDatabase(); 
		String where = DATE + " = ?";
		String[] whereValue = { value};
		ContentValues cv = new ContentValues(); 
		
		cv.put(BOOK_DESC, value); 
		db.update(TABLE_NAME, cv, where, whereValue); 
	}
	
    public void settablename(String name){
    	TABLE_NAME=name;
    }
    }

