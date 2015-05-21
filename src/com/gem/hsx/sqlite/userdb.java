package com.gem.hsx.sqlite; 
import android.content.ContentValues; 
import android.content.Context; 
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase; 
import android.database.sqlite.SQLiteOpenHelper;
public class userdb extends SQLiteOpenHelper 
{
	private  static String DATABASE_NAME = "data.db"; 
	private  static String TABLE_NAME = "user"; 
	private final static int DATABASE_VERSION = 1; 
	private final static String PASSWORD = "password";
	public final static String USER_NAME = "username";
	public final static String GENDER = "gender";
	public final static String AGE = "age"; 
	public final static String OTHERS = "others"; 
	public userdb(Context context) { 
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		} 
	//创建table @Override
	public void onCreate(SQLiteDatabase db)
	{ 
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + USER_NAME + " text PRIMARY KEY, " + PASSWORD + " text, "+ GENDER+" INTEGER, "+AGE +" INTEGER,"+OTHERS+" text);";
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
	public long insert(String username,String password,int gender,String others,int age) 
	{
		SQLiteDatabase db = this.getWritableDatabase(); 
	/* ContentValues */
		ContentValues cv = new ContentValues(); 
		//cv.put(BOOK_ID, id);
		cv.put(USER_NAME, username);
		cv.put(PASSWORD, password); 
		cv.put(GENDER, gender); 
		cv.put(AGE, age);
		cv.put(OTHERS, others);
		long row = db.insert(TABLE_NAME, null, cv); return row; } 
	//删除操作
	public void delete(String username) 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String where = USER_NAME + " = ?";
		String[] whereValue ={ username}; 
		db.delete(TABLE_NAME, where, whereValue);
	} 
	//修改操作 
	public void update(String username,String password,int gender,String others,int age)
	{
		SQLiteDatabase db = this.getWritableDatabase(); 
		String where = username + " = ?";
		String[] whereValue = { username};
		ContentValues cv = new ContentValues(); 
		cv.put(USER_NAME, username);
		cv.put(PASSWORD, password); 
		cv.put(GENDER, gender); 
		cv.put(AGE, age);
		cv.put(OTHERS, others);
		db.update(TABLE_NAME, cv, where, whereValue); 
	}
	

	public void setDbname(String name)
	{
		DATABASE_NAME=name;
	}


}

