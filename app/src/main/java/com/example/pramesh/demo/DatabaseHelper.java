package com.example.pramesh.demo;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static android.R.attr.id;

/**
 * Created by Pramesh Bajracharya on11/11/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "student";
    public static final String TABLE_NAME = "student";
    public static final String COL_1 = "id";
    public static final String COL_2 = "name";
    public static final String COL_3 = "surname";
    public static final String COL_4 = "marks";
    public static final int DATABASE_VERSION = 8;
    public static final String CREATE_TABLE ="CREATE TABLE "+TABLE_NAME+" ("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_2+" TEXT,"+COL_3+" TEXT,"+COL_4+" INTEGER(10));";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);                       //  This creates the database ...
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE "+TABLE_NAME+" IF EXISTS "+TABLE_NAME+";");
    }
    public boolean insertData(String name,String surname,String marks){
        SQLiteDatabase db = this.getWritableDatabase();               //  database instance
        ContentValues contentValues  = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2,name);
        contentValues.put(DatabaseHelper.COL_3,surname);
        contentValues.put(DatabaseHelper.COL_4,marks);
        long result = db.insert(DatabaseHelper.TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();               //  data base instance ...
         Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" ;",null);
         return res;
    }

    public Integer deletedata(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

    public boolean updateData(String id, String name, String surname, String marks) {
        SQLiteDatabase db = this.getWritableDatabase();               //  data base instance ...
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, marks);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }
}
