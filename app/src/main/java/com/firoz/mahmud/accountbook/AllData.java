package com.firoz.mahmud.accountbook;

import android.content.Context;
import android.os.Build;

import java.io.File;

import androidx.annotation.Nullable;

public class AllData {

    public static class picture{
        public static String halfcirclepath(String timemiles, final Context context){
            File file= new File(context.getFilesDir(),"Pictures");
            if (!file.exists()){
                file.mkdirs();
            }
            return file.getAbsolutePath()+"/memberhalfpic"+timemiles;
        }
        public static String circlepath(String timemiles,Context context){
            File file= new File(context.getFilesDir(),"Pictures");
            if (!file.exists()){
                file.mkdirs();
            }
            return file.getAbsolutePath()+"/memberpic"+timemiles;
        }
        public static String shopPath(String timemiles,Context context){
            File file= new File(context.getFilesDir(),"Pictures");
            if (!file.exists()){
                file.mkdirs();
            }
            return file.getAbsolutePath()+"/shoppic"+timemiles;
        }
    }

    public static class sharedPref{
        public static String dataBaseName="Settings";
        public static String sqlDatabasePath="SqlPath";
    }
    public static class sqlDataBase{
        public static String databasename="MainData.db";
    }
    public static class activity{
        public static String passId="PassId";
    }
}
