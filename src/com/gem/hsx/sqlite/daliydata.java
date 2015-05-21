package com.gem.hsx.sqlite; 
import android.content.ContentValues; 
import android.content.Context; 
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase; 
import android.database.sqlite.SQLiteOpenHelper;
public class daliydata extends SQLiteOpenHelper 
{
	private final static String DATABASE_NAME = ".db"; 
	private final static int DATABASE_VERSION = 1; 
	private final static String TABLE_NAME = "data";
	public final static String DATE = "date";
	public final static String HEALTH_VALUE = "health_value";
	public final static String BOOK_AUTHOR = "map_y"; 
	public final static String BOOK_DESC = "map_desc"; 
	public daliydata(Context context,String username) { 
		// TODO Auto-generated constructor stub
		super(context, username+DATABASE_NAME, null, DATABASE_VERSION);
		} 
	//����table @Override
	public void onCreate(SQLiteDatabase db)
	{ 
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + DATE + " text, " + HEALTH_VALUE + " INTEGER);";
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
	//���Ӳ���
	public long insert(String date,int value) 
	{
		SQLiteDatabase db = this.getWritableDatabase(); 
	/* ContentValues */
		ContentValues cv = new ContentValues(); 
		
	
	
		cv.put(date, value); 
		long row = db.insert(TABLE_NAME, null, cv); return row; } 
	
	
	//ɾ������
	public void delete(String date) 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String where = DATE + " = ?";
		String[] whereValue ={ date}; 
		db.delete(TABLE_NAME, where, whereValue);
	} 
	//�޸Ĳ��� 
	public void update(int id, String value)
	{
		SQLiteDatabase db = this.getWritableDatabase(); 
		String where = DATE + " = ?";
		String[] whereValue = { value};
		ContentValues cv = new ContentValues(); 
		
		cv.put(BOOK_DESC, value); 
		db.update(TABLE_NAME, cv, where, whereValue); 
	}
	

}

