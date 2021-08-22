package com.firoz.mahmud.accountbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Date;

public class PaidDb extends SQLiteOpenHelper {
    private String amount = "Amount";
    private String day = "Present_Day";
    private String details = "Amount_Details";

    /* renamed from: id */
    private String f63id = "Sirial_No";
    private String month = "Present_Month";
    private String table_name = "PayedDetails";
    private String timemiles = "SystemMiles";
    private String year = "Present_Year";

    public static class MyData {
        private int amount;
        private int day;
        private String details;

        /* renamed from: id */
        private int f64id;
        private int month;
        private String timemiles;
        private int year;

        public MyData(int i, int i2, int i3, int i4, int i5, String str, String str2) {
            this.f64id = i;
            this.amount = i2;
            this.day = i3;
            this.month = i4;
            this.year = i5;
            this.details = str;
            this.timemiles = str2;
        }

        public int getId() {
            return this.f64id;
        }

        public void setId(int i) {
            this.f64id = i;
        }

        public int getAmount() {
            return this.amount;
        }

        public void setAmount(int i) {
            this.amount = i;
        }

        public int getDay() {
            return this.day;
        }

        public void setDay(int i) {
            this.day = i;
        }

        public int getMonth() {
            return this.month;
        }

        public void setMonth(int i) {
            this.month = i;
        }

        public int getYear() {
            return this.year;
        }

        public void setYear(int i) {
            this.year = i;
        }

        public String getDetails() {
            return this.details;
        }

        public void setDetails(String str) {
            this.details = str;
        }

        public String getTimemiles() {
            return this.timemiles;
        }

        public void setTimemiles(String str) {
            this.timemiles = str;
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public PaidDb(Context context, String str) {
        super(context, str, null, 115);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ");
        sb.append(this.table_name);
        sb.append("(");
        sb.append(this.f63id);
        sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append(this.amount);
        sb.append(" INTEGER,");
        sb.append(this.details);
        sb.append(" TEXT,");
        sb.append(this.timemiles);
        sb.append(" TEXT,");
        sb.append(this.day);
        sb.append(" INTEGER,");
        sb.append(this.month);
        sb.append(" INTEGER,");
        sb.append(this.year);
        sb.append(" INTEGER)");
        sQLiteDatabase.execSQL(sb.toString());
    }

    public void addAmount(int amount, String details,int day,int month,int year, String timemiles) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.amount, amount);
        contentValues.put(this.details, details);
        contentValues.put(this.day, day);
        contentValues.put(this.month, month);
        contentValues.put(this.year, year);
        contentValues.put(this.timemiles, timemiles);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        onCreate(writableDatabase);
        writableDatabase.insert(this.table_name, null, contentValues);
        writableDatabase.close();
    }

    public void updateData(MyData myData) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(this.amount, Integer.valueOf(myData.getAmount()));
            contentValues.put(this.details, myData.getDetails());
            contentValues.put(this.day, Integer.valueOf(myData.getDay()));
            contentValues.put(this.month, Integer.valueOf(myData.getMonth()));
            contentValues.put(this.year, Integer.valueOf(myData.getYear()));
            contentValues.put(this.timemiles, myData.getTimemiles());
            String str = this.table_name;
            StringBuilder sb = new StringBuilder();
            sb.append(this.f63id);
            sb.append("=");
            sb.append(myData.getId());
            writableDatabase.update(str, contentValues, sb.toString(), null);
            writableDatabase.close();
        } catch (Exception unused) {
        }
    }

    public ArrayList<MyData> getAllData() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList<MyData> arrayList = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ");
            sb.append(this.table_name);
            Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), null);
            if (rawQuery != null && rawQuery.moveToFirst() && rawQuery.getCount() > 0) {
                for (int i = 0; i < rawQuery.getCount(); i++) {
                    MyData myData = new MyData(rawQuery.getInt(rawQuery.getColumnIndex(this.f63id)), rawQuery.getInt(rawQuery.getColumnIndex(this.amount)), rawQuery.getInt(rawQuery.getColumnIndex(this.day)), rawQuery.getInt(rawQuery.getColumnIndex(this.month)), rawQuery.getInt(rawQuery.getColumnIndex(this.year)), rawQuery.getString(rawQuery.getColumnIndex(this.details)), rawQuery.getString(rawQuery.getColumnIndex(this.timemiles)));
                    arrayList.add(myData);
                    rawQuery.moveToNext();
                }
            }
        } catch (Exception unused) {
        }
        readableDatabase.close();
        return arrayList;
    }

    public ArrayList<MyData> getAllData(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList<MyData> arrayList = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ");
            sb.append(this.table_name);
            sb.append(" WHERE ");
            sb.append(this.timemiles);
            sb.append(" ='");
            sb.append(str);
            sb.append("' ORDER BY "+this.f63id+" DESC;");
            Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), null);
            if (rawQuery != null && rawQuery.moveToFirst() && rawQuery.getCount() > 0) {
                for (int i = 0; i < rawQuery.getCount(); i++) {
                    MyData myData = new MyData(rawQuery.getInt(rawQuery.getColumnIndex(this.f63id)), rawQuery.getInt(rawQuery.getColumnIndex(this.amount)), rawQuery.getInt(rawQuery.getColumnIndex(this.day)), rawQuery.getInt(rawQuery.getColumnIndex(this.month)), rawQuery.getInt(rawQuery.getColumnIndex(this.year)), rawQuery.getString(rawQuery.getColumnIndex(this.details)), rawQuery.getString(rawQuery.getColumnIndex(this.timemiles)));
                    arrayList.add(myData);
                    rawQuery.moveToNext();
                }
            }
        } catch (Exception unused) {
        }
        readableDatabase.close();
        return arrayList;
    }

    public ArrayList<MyData> getAllDataByYear(String str, String str2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList<MyData> arrayList = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ");
            sb.append(this.table_name);
            sb.append(" WHERE ");
            sb.append(this.timemiles);
            sb.append(" ='");
            sb.append(str);
            sb.append("' and ");
            sb.append(this.year);
            sb.append("=");
            sb.append(str2);
            Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), null);
            if (rawQuery != null && rawQuery.moveToFirst() && rawQuery.getCount() > 0) {
                for (int i = 0; i < rawQuery.getCount(); i++) {
                    MyData myData = new MyData(rawQuery.getInt(rawQuery.getColumnIndex(this.f63id)), rawQuery.getInt(rawQuery.getColumnIndex(this.amount)), rawQuery.getInt(rawQuery.getColumnIndex(this.day)), rawQuery.getInt(rawQuery.getColumnIndex(this.month)), rawQuery.getInt(rawQuery.getColumnIndex(this.year)), rawQuery.getString(rawQuery.getColumnIndex(this.details)), rawQuery.getString(rawQuery.getColumnIndex(this.timemiles)));
                    arrayList.add(myData);
                    rawQuery.moveToNext();
                }
            }
        } catch (Exception unused) {
        }
        readableDatabase.close();
        return arrayList;
    }

    public ArrayList<MyData> getAllDataByYear(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList<MyData> arrayList = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ");
            sb.append(this.table_name);
            sb.append(" WHERE ");
            sb.append(this.year);
            sb.append("=");
            sb.append(str);
            Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), null);
            if (rawQuery != null && rawQuery.moveToFirst() && rawQuery.getCount() > 0) {
                for (int i = 0; i < rawQuery.getCount(); i++) {
                    MyData myData = new MyData(rawQuery.getInt(rawQuery.getColumnIndex(this.f63id)), rawQuery.getInt(rawQuery.getColumnIndex(this.amount)), rawQuery.getInt(rawQuery.getColumnIndex(this.day)), rawQuery.getInt(rawQuery.getColumnIndex(this.month)), rawQuery.getInt(rawQuery.getColumnIndex(this.year)), rawQuery.getString(rawQuery.getColumnIndex(this.details)), rawQuery.getString(rawQuery.getColumnIndex(this.timemiles)));
                    arrayList.add(myData);
                    rawQuery.moveToNext();
                }
            }
        } catch (Exception unused) {
        }
        readableDatabase.close();
        return arrayList;
    }

    public ArrayList<MyData> getAllDataByMonth(String str, String str2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList<MyData> arrayList = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ");
            sb.append(this.table_name);
            sb.append(" WHERE ");
            sb.append(this.timemiles);
            sb.append(" ='");
            sb.append(str);
            sb.append("' and ");
            sb.append(this.month);
            sb.append("=");
            sb.append(str2);
            Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), null);
            if (rawQuery != null && rawQuery.moveToFirst() && rawQuery.getCount() > 0) {
                for (int i = 0; i < rawQuery.getCount(); i++) {
                    MyData myData = new MyData(rawQuery.getInt(rawQuery.getColumnIndex(this.f63id)), rawQuery.getInt(rawQuery.getColumnIndex(this.amount)), rawQuery.getInt(rawQuery.getColumnIndex(this.day)), rawQuery.getInt(rawQuery.getColumnIndex(this.month)), rawQuery.getInt(rawQuery.getColumnIndex(this.year)), rawQuery.getString(rawQuery.getColumnIndex(this.details)), rawQuery.getString(rawQuery.getColumnIndex(this.timemiles)));
                    arrayList.add(myData);
                    rawQuery.moveToNext();
                }
            }
        } catch (Exception unused) {
        }
        readableDatabase.close();
        return arrayList;
    }

    public ArrayList<MyData> getAllDataByMonthandYear(String str, String str2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList<MyData> arrayList = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ");
            sb.append(this.table_name);
            sb.append(" WHERE ");
            sb.append(this.month);
            sb.append("=");
            sb.append(str);
            sb.append(" AND ");
            sb.append(this.year);
            sb.append("=");
            sb.append(str2);
            Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), null);
            if (rawQuery != null && rawQuery.moveToFirst() && rawQuery.getCount() > 0) {
                for (int i = 0; i < rawQuery.getCount(); i++) {
                    MyData myData = new MyData(rawQuery.getInt(rawQuery.getColumnIndex(this.f63id)), rawQuery.getInt(rawQuery.getColumnIndex(this.amount)), rawQuery.getInt(rawQuery.getColumnIndex(this.day)), rawQuery.getInt(rawQuery.getColumnIndex(this.month)), rawQuery.getInt(rawQuery.getColumnIndex(this.year)), rawQuery.getString(rawQuery.getColumnIndex(this.details)), rawQuery.getString(rawQuery.getColumnIndex(this.timemiles)));
                    arrayList.add(myData);
                    rawQuery.moveToNext();
                }
            }
        } catch (Exception unused) {
        }
        readableDatabase.close();
        return arrayList;
    }

    public ArrayList<MyData> getAllDataByDay(String str, String str2) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList<MyData> arrayList = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ");
            sb.append(this.table_name);
            sb.append(" WHERE ");
            sb.append(this.timemiles);
            sb.append(" ='");
            sb.append(str);
            sb.append("' and ");
            sb.append(this.day);
            sb.append("=");
            sb.append(this.day);
            Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), null);
            if (rawQuery != null && rawQuery.moveToFirst() && rawQuery.getCount() > 0) {
                for (int i = 0; i < rawQuery.getCount(); i++) {
                    MyData myData = new MyData(rawQuery.getInt(rawQuery.getColumnIndex(this.f63id)), rawQuery.getInt(rawQuery.getColumnIndex(this.amount)), rawQuery.getInt(rawQuery.getColumnIndex(this.day)), rawQuery.getInt(rawQuery.getColumnIndex(this.month)), rawQuery.getInt(rawQuery.getColumnIndex(this.year)), rawQuery.getString(rawQuery.getColumnIndex(this.details)), rawQuery.getString(rawQuery.getColumnIndex(this.timemiles)));
                    arrayList.add(myData);
                    rawQuery.moveToNext();
                }
            }
        } catch (Exception unused) {
        }
        readableDatabase.close();
        return arrayList;
    }

    public void deleteById(int i) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            String str = this.table_name;
            StringBuilder sb = new StringBuilder();
            sb.append(this.f63id);
            sb.append("=");
            sb.append(i);
            writableDatabase.delete(str, sb.toString(), null);
            writableDatabase.close();
        } catch (Exception unused) {
        }
    }

    public void deleteByTimeMiles(String str) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            String str2 = this.table_name;
            StringBuilder sb = new StringBuilder();
            sb.append(this.timemiles);
            sb.append("='");
            sb.append(str);
            sb.append("'");
            writableDatabase.delete(str2, sb.toString(), null);
            writableDatabase.close();
        } catch (Exception unused) {
        }
    }
    public int getTotalAmount(String timemiles) {
        SQLiteDatabase sld = this.getReadableDatabase();
        try {
            String command = "SELECT SUM(" + this.amount + ") FROM " + this.table_name +
                    " WHERE " + this.timemiles + "='" + timemiles + "';";
            Cursor c = sld.rawQuery(command, null);
            c.moveToFirst();
            int sum=c.getInt(0);
            sld.close();
            return sum;
        }catch (Exception e){
            sld.close();
            return 0;
        }
    }
}
