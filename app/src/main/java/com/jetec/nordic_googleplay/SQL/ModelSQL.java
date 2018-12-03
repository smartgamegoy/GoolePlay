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

public class ModelSQL extends SQLiteOpenHelper {

    private final static String table_name = "abc"; //資料表名
    private final static String db_name = "modelsql.db";    //資料庫名
    private static final int VERSION = 2;

    public ModelSQL(Context context) {
        super(context, db_name, null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // TODO 每次成功打開數據庫後首先被執行
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + table_name + "(" +
                "_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL," +
                "modelNAME" + " TEXT, " +
                "modelJSON" + " TEXT" + ")";
        db.execSQL(DATABASE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
        //db.execSQL("DROP TABLE IF EXISTS " + table_name); //刪除舊有的資料表
        onCreate(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    public int getCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_name, null);
        return cursor.getCount();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + table_name);
    }

    public void insert(String modelNAME, JSONArray modelJSON)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("modelNAME", modelNAME);
        cv.put("modelJSON", modelJSON.toString());

        Log.e("TAG",cv.toString());

        db.insert(table_name, modelNAME, cv);
    }

    public JSONArray getJSON(String modelNAME){
        JSONArray modelJSON = null;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor=db.rawQuery("SELECT * FROM " + table_name + " WHERE modelNAME = ?", new String[]{modelNAME});
        cursor.moveToFirst();
        String modellist = cursor.getString(cursor.getColumnIndex("modelJSON"));
        try {
            modelJSON = new JSONArray(modellist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return modelJSON;
    }
}
