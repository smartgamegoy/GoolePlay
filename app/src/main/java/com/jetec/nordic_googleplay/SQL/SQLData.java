package com.jetec.nordic_googleplay.SQL;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLData extends SQLiteOpenHelper {

    Context context;
    private final static String table_name = "newdata"; //資料表名
    private final static String db_name = "datasql.db";    //資料庫名
    private static final int VERSION = 2;
    public SQLData(Context context) {
        super(context, db_name, null, VERSION);
    }

    public Cursor select()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table_name, null, null, null, null,
                null, null, null);
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {   //ok
        String DATABASE_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + table_name + "(" +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL," +
                "num" + " TEXT, " +
                "savelist" + " TEXT," +
                "model" + " TEXT" + ")";
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

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, "_id=" + id ,null);
    }

    public long insert(JSONArray DataSave, String num, String model)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("num", num);
        cv.put("savelist", DataSave.toString());
        cv.put("model", model);

        long new_insert = db.insert(table_name, num, cv);
        Log.e("Log","SQL = " + new_insert);
        return new_insert;
    }

    public ArrayList<HashMap<String, String>> fillList(String model){

        ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor=db.rawQuery("SELECT * FROM " + table_name+ " WHERE model=?", new String[]{model});
        Log.e("cursor","cursor.getcount = " + cursor.getCount());
        cursor.moveToFirst();
        do {
            String id = String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")));
            String num = cursor.getString(cursor.getColumnIndex("num"));
            String savelist = cursor.getString(cursor.getColumnIndex("savelist"));
            String savemodel = cursor.getString(cursor.getColumnIndex("model"));

            HashMap<String, String> map = new HashMap<String, String>();
            Log.e("putmap", "id" + id);
            map.put("id", id);
            map.put("num", num);
            map.put("savelist", savelist);

            dataList.add(map);
        }while(cursor.moveToNext());
        return dataList;
    }

    public List<String> getlist(String num){
        List<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor=db.rawQuery("SELECT * FROM " + table_name + " WHERE num=?", new String[]{num});
        Log.e("myLog","cursor =" + cursor);
        cursor.moveToFirst();
        String savelist = cursor.getString(cursor.getColumnIndex("savelist"));

        dataList.add(savelist);

        //Log.e("myLog","dataList =" + dataList);
        return dataList;
    }

    public boolean update(int id, String num){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("num", num);

        long set_update = db.update(table_name, cv, "_id=" + id, null);
        return set_update > 0;
    }

    public int getCount(String num, String model){
        int count;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM " + table_name + " WHERE num=? AND model=?", new String[]{num, model});

        count = cursor.getCount();
        Log.e("myLog","count =" + count);
        return count;
    }

    public JSONArray getJSON(String saveNAME){
        JSONArray savelJSON = null;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor=db.rawQuery("SELECT * FROM " + table_name + " WHERE num = ?", new String[]{saveNAME});
        cursor.moveToFirst();
        String savelist = cursor.getString(cursor.getColumnIndex("savelist"));
        try {
            savelJSON = new JSONArray(savelist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return savelJSON;
    }

    public int modelsearch(String model){
        int count;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM " + table_name + " WHERE model=?", new String[]{model});

        count = cursor.getCount();
        Log.e("myLog","count =" + count);
        return count;
    }
}
