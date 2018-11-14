package com.jetec.nordic_googleplay.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
                "Name" + " TEXT, " +
                "PV1" + " TEXT, " +
                "PV2" + " TEXT, " +
                "EH1" + " TEXT, " +
                "EL1" + " TEXT, " +
                "EH2" + " TEXT, " +
                "EL2" + " TEXT, " +
                "CR1" + " TEXT, " +
                "CR2" + " TEXT, " +
                "ADR" + " TEXT " +
                /*"DPP" + " TEXT" +*/ ")";
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

    public long insert(List<String> DataSave, String num)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("num", num);
        cv.put("Name", DataSave.get(0));
        cv.put("PV1", DataSave.get(1));
        cv.put("PV2", DataSave.get(2));
        cv.put("EH1", DataSave.get(3));
        cv.put("EL1", DataSave.get(4));
        cv.put("EH2", DataSave.get(5));
        cv.put("EL2", DataSave.get(6));
        cv.put("CR1", DataSave.get(7));
        cv.put("CR2", DataSave.get(8));
        cv.put("ADR", DataSave.get(9));
        //cv.put("DPP", DataSave.get(10));


        long new_insert = db.insert(table_name, num, cv);
        Log.e("Log","SQL = " + new_insert);
        return new_insert;
    }

    public ArrayList<HashMap<String, String>> fillList(){

        ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor=db.rawQuery("SELECT * FROM " + table_name, null);
        cursor.moveToFirst();
        do {
            String id = String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")));
            String num = cursor.getString(cursor.getColumnIndex("num"));
            String Name = cursor.getString(cursor.getColumnIndex("Name"));
            String PV1 = cursor.getString(cursor.getColumnIndex("PV1"));
            String PV2 = cursor.getString(cursor.getColumnIndex("PV2"));
            String EH1 = cursor.getString(cursor.getColumnIndex("EH1"));
            String EL1 = cursor.getString(cursor.getColumnIndex("EL1"));
            String EH2 = cursor.getString(cursor.getColumnIndex("EH2"));
            String EL2 = cursor.getString(cursor.getColumnIndex("EL2"));
            String CR1 = cursor.getString(cursor.getColumnIndex("CR1"));
            String CR2 = cursor.getString(cursor.getColumnIndex("CR2"));
            String ADR = cursor.getString(cursor.getColumnIndex("ADR"));
            //String DPP = cursor.getString(cursor.getColumnIndex("DPP"));

            HashMap<String, String> map = new HashMap<String, String>();
            Log.e("putmap", "id" + id);
            map.put("id", id);
            map.put("num", num);
            map.put("Name", Name);
            map.put("PV1", PV1);
            map.put("PV2", PV2);
            map.put("EH1", EH1);
            map.put("EL1", EL1);
            map.put("EH2", EH2);
            map.put("EL2", EL2);
            map.put("CR1", CR1);
            map.put("CR2", CR2);
            map.put("ADR", ADR);
            //map.put("DPP", DPP);

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
        String Name = cursor.getString(cursor.getColumnIndex("Name"));
        String PV1 = cursor.getString(cursor.getColumnIndex("PV1"));
        String PV2 = cursor.getString(cursor.getColumnIndex("PV2"));
        String EH1 = cursor.getString(cursor.getColumnIndex("EH1"));
        String EL1 = cursor.getString(cursor.getColumnIndex("EL1"));
        String EH2 = cursor.getString(cursor.getColumnIndex("EH2"));
        String EL2 = cursor.getString(cursor.getColumnIndex("EL2"));
        String CR1 = cursor.getString(cursor.getColumnIndex("CR1"));
        String CR2 = cursor.getString(cursor.getColumnIndex("CR2"));
        String ADR = cursor.getString(cursor.getColumnIndex("ADR"));
        //String DPP = cursor.getString(cursor.getColumnIndex("DPP"));

        dataList.add(Name);
        dataList.add(PV1);
        dataList.add(PV2);
        dataList.add(EH1);
        dataList.add(EL1);
        dataList.add(EH2);
        dataList.add(EL2);
        dataList.add(CR1);
        dataList.add(CR2);
        dataList.add(ADR);
        //dataList.add(DPP);
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

    public int getCount(String num){
        int count;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM " + table_name + " WHERE num=?", new String[]{num});

        count = cursor.getCount();
        Log.e("myLog","count =" + count);
        return count;
    }

}
