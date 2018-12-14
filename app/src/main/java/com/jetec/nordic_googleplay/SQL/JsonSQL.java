package com.jetec.nordic_googleplay.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

public class JsonSQL extends SQLiteOpenHelper {

    private final static String table_name = "datalist"; //資料表名
    private final static String db_name = "jsonsql.db";    //資料庫名
    private static final int VERSION = 2;

    public JsonSQL(Context context) {
        super(context, db_name, null, VERSION);
    }

    public Cursor select()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(table_name, null, null, null, null,
                null, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {   //ok
        String DATABASE_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + table_name + "(" +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL," +
                "address" + " TEXT, " +
                "Charttime" + " TEXT, " +
                "Timelist" + " TEXT, " +
                "Firstlist" + " TEXT, " +
                "Secondlist" + " TEXT, " +
                "Thirdlist" + " TEXT" + ")";
        db.execSQL(DATABASE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
        //db.execSQL("DROP TABLE IF EXISTS " + table_name); //刪除舊有的資料表
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // TODO 每次成功打開數據庫後首先被執行
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    public int getCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_name, null);
        return cursor.getCount();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + table_name);
    }

    public void delete(String address){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] del = new String[] {address};
        db.delete(table_name, "address" + "=?",del);
    }

    public long insert(JSONArray charttime, JSONArray timelist, JSONArray Firstlist,
                       JSONArray Secondlist, JSONArray Thirdlist, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("address", address);
        cv.put("Charttime", charttime.toString());
        cv.put("Timelist", timelist.toString());
        cv.put("Firstlist", Firstlist.toString());
        cv.put("Secondlist", Secondlist.toString());
        cv.put("Thirdlist", Thirdlist.toString());

        Log.e("TAG",charttime.toString());

        long new_insert = db.insert(table_name, address, cv);
        return new_insert;
    }

    public ArrayList<String> getlist(String address){
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor=db.rawQuery("SELECT * FROM " + table_name + " WHERE address = ?", new String[] {address});
        Log.e("myLog","cursor =" + cursor.getCount());
        cursor.moveToFirst();
        Log.e("myLog","cursor.moveToFirst = " + cursor.moveToFirst());
        String Charttime = cursor.getString(cursor.getColumnIndex("Charttime"));
        String Timelist = cursor.getString(cursor.getColumnIndex("Timelist"));
        String Firstlist = cursor.getString(cursor.getColumnIndex("Firstlist"));
        String Secondlist = cursor.getString(cursor.getColumnIndex("Secondlist"));
        String Thirdlist = cursor.getString(cursor.getColumnIndex("Thirdlist"));

        dataList.add(Charttime);
        dataList.add(Timelist);
        dataList.add(Firstlist);
        dataList.add(Secondlist);
        dataList.add(Thirdlist);

        return dataList;
    }

    public Boolean searchJson(String address){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor=db.rawQuery("SELECT * FROM " + table_name + " WHERE address = ?", new String[] {address});
        if(cursor.getCount() != 0) {
            return true;
        }
        else{
            return false;
        }
    }
}
