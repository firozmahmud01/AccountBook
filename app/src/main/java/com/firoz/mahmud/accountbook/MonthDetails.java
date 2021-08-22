package com.firoz.mahmud.accountbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MonthDetails extends SQLiteOpenHelper {
    String tablename="MonthDeatails",timemiles="UnicTimesMile",year="DataYear",id="UnicId";
    public MonthDetails(Context context){
        super(context,AllData.sqlDataBase.databasename,null,115);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " +tablename+
                "(" +
                id+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                timemiles+" TEXT," +
                year+" TEXT" +
                ");");
    }
    public void addAllData(String timemiles,String year){
        try {
            ContentValues cv = new ContentValues();
            cv.put(this.timemiles, timemiles);
            cv.put(this.year, year);
            SQLiteDatabase swd = this.getWritableDatabase();
            onCreate(swd);
            swd.insert(this.tablename, null, cv);
            swd.close();
        }catch (Exception e){
            String se=e.getMessage();
        }
    }
    public ArrayList<String> getAllData(String timemiles){
        String cmd="SELECT * FROM "+this.tablename+" WHERE "+this.timemiles+"='"+timemiles+"';";
        SQLiteDatabase srd=this.getReadableDatabase();
        ArrayList<String> data=new ArrayList<>();
        try {
            Cursor c = srd.rawQuery(cmd, null);
            if (c != null && c.moveToFirst() && c.getCount() > 0) {
                do {
                    data.add(c.getString(c.getColumnIndex(this.year)));
                } while (c.moveToNext());
            }
        }catch (Exception e){
            String m=e.getMessage();
        }
        srd.close();
        return data;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
