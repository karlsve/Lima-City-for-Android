package limaCity.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseConnectionCache
{
	private static final String DB_NAME = "cache_db";
	private static final int DB_VERSION = 1;
	
	private Context context = null;
	private DatabaseHelper dbhelper = null;
	private SQLiteDatabase db = null;
	
	private ArrayList<String> columns = new ArrayList<String>();
	
	private String db_table = "";
	
	private String db_create = "";
	
	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) 
		{
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d("DB", "Database created");
			db.execSQL(db_create);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			db.execSQL("DROP TABLE IF EXISTS " + db_table);
			onCreate(db);
		}

	}
	
	public DatabaseConnectionCache(Context context, String table, HashMap<String, String> columns)
	{
		this.db_table = table;
		this.context = context;
		this.db_create = "create table " + table + " ( ID integer primary key autoincrement,";
		Iterator<String> keys = columns.keySet().iterator();
		while(keys.hasNext())
		{
			String current_key = keys.next();
			this.db_create += ", " + current_key + " " + columns.get(current_key);
		}
		this.db_create += " );";
	}
	
	public DatabaseConnectionCache open() throws SQLException
	{
		dbhelper = new DatabaseHelper(context);
		db = dbhelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbhelper.close();
	}
	
	public long addRow(HashMap<String, String> data)
	{
		ContentValues values = new ContentValues();
		Iterator<String> keys = data.keySet().iterator();
		while(keys.hasNext())
		{
			String current_key = keys.next();
			if(columns.contains(current_key))
			{
				values.put(current_key, data.get(current_key));
			}
		}
		if(values.size() > 0)
		{
			return db.insert(db_table, null, values);
		}
		else
		{
			return -1;
		}
	}
	
	public boolean deleteRow(long rowId)
	{
		return db.delete(db_table, "ID="+rowId , null) > 0;
	}
	
	public Cursor fetchAll()
	{
		return db.query(db_table, (String[])columns.toArray(), null, null, null, null, null);
	}
	
	public Cursor fetchRow(long rowId)
	{
		Cursor cursor = db.query(true, db_table, (String[])columns.toArray(), "ID="+rowId, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public boolean updateRow(long rowId, HashMap<String, String> data)
	{
		ContentValues values = new ContentValues();
		Iterator<String> keys = data.keySet().iterator();
		while(keys.hasNext())
		{
			String current_key = keys.next();
			if(columns.contains(current_key))
			{
				values.put(current_key, data.get(current_key));
			}
		}
		if(values.size() > 0)
		{
			return db.update(db_table, values, "ID="+rowId, null) > 0;
		}
		else
		{
			return false;
		}
	}
}
