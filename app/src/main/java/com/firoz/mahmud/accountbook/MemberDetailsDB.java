package com.firoz.mahmud.accountbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class MemberDetailsDB extends SQLiteOpenHelper {
    private String tablename="MemberDetails",
    id="Member_No";
    private String member_name="Member_Name",
    address="Member_Address",
    phone_number="Member_Phone_Number",
    full_circle_pic_path="Full_Circle_Pic_Path",
    half_circle_pic_path="Half_Circle_Pic_Path",
    timemiles="Time_Miles",
    shop_pic_path="Shop_Picture_Path",
    shop_address="Shop_Address",
    security="Shop_Security",
    monthly_payment="Monthly_Payment",
    date="JoinDate";
    public MemberDetailsDB(Context context, String path){
        super(context,path,null,115);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+tablename+
                "(" +
                id+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                member_name+" TEXT," +
                address+" TEXT," +
                phone_number+" TEXT," +
                full_circle_pic_path+" TEXT," +
                half_circle_pic_path+" TEXT," +
                timemiles+" TEXT," +
                shop_pic_path+" TEXT," +
                shop_address+" TEXT," +
                security+" INTEGER," +
                monthly_payment+" INTEGER," +
                date+" TEXT" +
                ")");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
public void addMember(String member_name,String
                      address,
                      String phone_number,
                      String full_circle_pic_path,
                      String half_circle_pic_path,
                      String timemiles,
                      String shop_pic_path,
                      String shop_address,
                      int security,
                      int monthly_payment,String date){
    ContentValues cv=new ContentValues();
    cv.put(this.member_name,member_name);
    cv.put(this.address,address);
    cv.put(this.phone_number,phone_number);
    cv.put(this.full_circle_pic_path,full_circle_pic_path);
    cv.put(this.half_circle_pic_path,half_circle_pic_path);
    cv.put(this.timemiles,timemiles);
    cv.put(this.shop_pic_path,shop_pic_path);
    cv.put(this.shop_address,shop_address);
    cv.put(this.security,security);
    cv.put(this.monthly_payment,monthly_payment);
    cv.put(this.date,date);
    SQLiteDatabase sdw=this.getWritableDatabase();
    onCreate(sdw);
    sdw.insert(tablename,null,cv);
    sdw.close();
}
public ArrayList<MyData> getAllMemberDetails(){
        SQLiteDatabase sdr=this.getReadableDatabase();
    Cursor c=sdr.rawQuery("SELECT * FROM "+tablename,null);
    ArrayList<MyData>result=new ArrayList<MyData>();
    if (c!=null&&c.getCount()>0&&c.moveToFirst()) {
        for (int x = 0; x < c.getCount(); x++) {
            MyData md=new MyData(c.getInt(c.getColumnIndex(this.id)),c.getString(c.getColumnIndex(this.member_name))
                    ,c.getString(c.getColumnIndex(this.address)),
                    c.getString(c.getColumnIndex(this.phone_number)),
                            c.getString(c.getColumnIndex(this.full_circle_pic_path)),
                                    c.getString(c.getColumnIndex(this.half_circle_pic_path)),
                                            c.getString(c.getColumnIndex(this.timemiles)),
                    c.getString(c.getColumnIndex(this.shop_pic_path)),
                    c.getString(c.getColumnIndex(this.shop_address)),
                    c.getInt(c.getColumnIndex(this.security)),
                    c.getInt(c.getColumnIndex(this.monthly_payment)),
                    c.getString(c.getColumnIndex(this.date)));
            result.add(md);
            c.moveToNext();
        }
    }
    sdr.close();
    return result;
    }
    public MyData getSingleMemberDetails(int id){
        SQLiteDatabase sdr=this.getReadableDatabase();
        Cursor c=sdr.rawQuery("SELECT * FROM "+tablename+" WHERE "+this.id+"="+id,null);
        if (c!=null&&c.getCount()>0&&c.moveToFirst()) {
                MyData md=new MyData(c.getInt(c.getColumnIndex(this.id)),c.getString(c.getColumnIndex(this.member_name))
                        ,c.getString(c.getColumnIndex(this.address)),
                        c.getString(c.getColumnIndex(this.phone_number)),
                        c.getString(c.getColumnIndex(this.full_circle_pic_path)),
                        c.getString(c.getColumnIndex(this.half_circle_pic_path)),
                        c.getString(c.getColumnIndex(this.timemiles)),
                        c.getString(c.getColumnIndex(this.shop_pic_path)),
                        c.getString(c.getColumnIndex(this.shop_address)),
                        c.getInt(c.getColumnIndex(this.security)),
                        c.getInt(c.getColumnIndex(this.monthly_payment)),
                        c.getString(c.getColumnIndex(this.date)));
                sdr.close();
                return md;

        }
        sdr.close();
        return null;
    }
    public void updateData(MyData md){
        SQLiteDatabase sdw=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(this.member_name,md.getMember_name());
        cv.put(this.address,md.getAddress());
        cv.put(this.phone_number,md.getPhone_number());
        cv.put(this.full_circle_pic_path,md.getFull_circle_pic_path());
        cv.put(this.half_circle_pic_path,md.getHalf_circle_pic_path());
        cv.put(this.timemiles,md.getTimemiles());
        cv.put(this.shop_pic_path,md.shop_pic_path);
        cv.put(this.shop_address,md.shop_address);
        cv.put(this.security,md.security);
        cv.put(this.date,md.date);
        cv.put(this.monthly_payment,md.monthly_payment);
        sdw.update(tablename,cv,this.id+"="+md.getId(),null);
        sdw.close();
    }
    public void removeMember(int id){
        SQLiteDatabase sdw=this.getWritableDatabase();
        sdw.delete(tablename,this.id+"="+id,null);
        sdw.close();
    }
public static class MyData{
        int id;
    private String member_name,
            address,
            phone_number,
            full_circle_pic_path,
            half_circle_pic_path,
            timemiles,shop_pic_path,shop_address,date;
int security,monthly_payment;

    public MyData(int id,
                  String member_name,
                  String address,
                  String phone_number,
                  String full_circle_pic_path,
                  String half_circle_pic_path,
                  String timemiles,
                  String shop_pic_path,
                  String shop_address,
                  int security,
                  int monthly_payment,String date) {
        this.id = id;
        this.member_name = member_name;
        this.address = address;
        this.phone_number = phone_number;
        this.full_circle_pic_path = full_circle_pic_path;
        this.half_circle_pic_path = half_circle_pic_path;
        this.timemiles = timemiles;
        this.shop_pic_path = shop_pic_path;
        this.shop_address = shop_address;
        this.security = security;
        this.monthly_payment = monthly_payment;
        this.date=date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShop_pic_path() {
        return shop_pic_path;
    }

    public void setShop_pic_path(String shop_pic_path) {
        this.shop_pic_path = shop_pic_path;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public int getSecurity() {
        return security;
    }

    public void setSecurity(int security) {
        this.security = security;
    }

    public int getMonthly_payment() {
        return monthly_payment;
    }

    public void setMonthly_payment(int monthly_payment) {
        this.monthly_payment = monthly_payment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFull_circle_pic_path() {
        return full_circle_pic_path;
    }

    public void setFull_circle_pic_path(String full_circle_pic_path) {
        this.full_circle_pic_path = full_circle_pic_path;
    }

    public String getHalf_circle_pic_path() {
        return half_circle_pic_path;
    }

    public void setHalf_circle_pic_path(String half_circle_pic_path) {
        this.half_circle_pic_path = half_circle_pic_path;
    }

    public String getTimemiles() {
        return timemiles;
    }

    public void setTimemiles(String timemiles) {
        this.timemiles = timemiles;
    }
}
}
