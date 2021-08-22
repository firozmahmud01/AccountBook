package com.firoz.mahmud.accountbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    boolean allmember;
    Context context=this;
    MemberDetailsDB mddb;
    MainActivity ma=this;
    StorageReference sr;
    BackWork bw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        sr=FirebaseStorage.getInstance().getReference().child("backup.zip");
        bw=new BackWork();
        allmember=true;
        mddb=new MemberDetailsDB(this,AllData.sqlDataBase.databasename);
        fab=findViewById(R.id.main_activity_floating_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allmember){
                    v.setVisibility(View.GONE);
                    NavigationView nv=findViewById(R.id.main_activity_nav_view);
                    nv.getMenu().getItem(1).setChecked(true);
                    InsertMemberDetails insertMemberDetails=new InsertMemberDetails(context,fab,ma);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_activity_frame, insertMemberDetails).commit();
                }else{
                    bw.fabwork();
                }
            }
        });
        if (savedInstanceState==null) {
            AllMember am=new AllMember(context,mddb,fab,this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_activity_frame,am).commit();
        }
        navigationBar();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout dl=findViewById(R.id.main_activity_drawer);
        if (dl.isDrawerOpen(GravityCompat.START)){
            dl.closeDrawer(GravityCompat.START);
        }else {
            bw.customMethode();
        }
    }

    private void navigationBar(){
        Toolbar toolbar=findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout dl=findViewById(R.id.main_activity_drawer);
        ActionBarDrawerToggle abdt=new ActionBarDrawerToggle(MainActivity.this,dl,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        NavigationView nv=findViewById(R.id.main_activity_nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_all_member:
                        getAllMember();
                        break;
                    case R.id.nav_add_member:
                        fab.setVisibility(View.GONE);
                        InsertMemberDetails insertMemberDetails=new InsertMemberDetails(context,fab,ma);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_activity_frame,insertMemberDetails).commit();
                        break;
                    case R.id.nav_dashboard:
                        fab.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_activity_frame,new DashBoard()).commit();
                        break;
                    case R.id.nav_upload:
                        DetailsChangerDialog dcd=new DetailsChangerDialog(context, InputType.TYPE_CLASS_TEXT) {
                            @Override
                            public void getChangedData(String data) {
                                final ProgressDialog pd=new ProgressDialog(context);
                                pd.setCancelable(false);
                                pd.setMessage("Uploading....");
                                pd.show();
                                Thread th=new Thread(){
                                    @Override
                                    public void run(){
                                        try {
                                            File zipfile=new File(context.getCacheDir(), "backup.zip");
                                            final File[] files = (new File(context.getFilesDir(), "Pictures")).listFiles();
                                            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipfile));
                                            for (File file : files) {
                                                ZipEntry ze = new ZipEntry("Pictures/" + file.getName());
                                                zos.putNextEntry(ze);
                                                FileInputStream dis =new FileInputStream(file);
                                                byte b[]=new byte[1024*10];
                                                int length;
                                                while ((length=dis.read(b))!=-1){
                                                    zos.write(b,0,length);
                                                }
                                            }
                                            File database=context.getDatabasePath(AllData.sqlDataBase.databasename);
                                            ZipEntry ze=new ZipEntry(database.getName());
                                            zos.putNextEntry(ze);
                                            FileInputStream fis=new FileInputStream(database);
                                            byte b[]=new byte[1024*10];
                                            int length;
                                            while ((length=fis.read(b))!=-1){
                                                zos.write(b,0,length);
                                            }
                                            zos.close();
                                            sr.putFile(Uri.fromFile(zipfile)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    pd.dismiss();
                                                    Toast.makeText(getBaseContext(), "Successfully uploaded", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    pd.dismiss();
                                                    Toast.makeText(getBaseContext(), "Failed to upload.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }catch (Exception e){
                                            pd.dismiss();
                                        }
                                    }};
                                th.start();
                            }
                        };
                        dcd.showDialog("Upload","Type confirm to upload");
                        dcd.setInputDesable();
                        break;
                    case R.id.nav_download:
                        DetailsChangerDialog dcd1=new DetailsChangerDialog(context, InputType.TYPE_CLASS_TEXT) {
                            @Override
                            public void getChangedData(String data) {
                                final ProgressDialog pdd=new ProgressDialog(context);
                                pdd.setCancelable(false);
                                pdd.setMessage("Downloading....");
                                pdd.show();
                                File tmp=new File(context.getCacheDir(),"backup.zip");
                                if (tmp.exists()){
                                    tmp.delete();
                                }
                                sr.getFile(tmp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        try {
                                            ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(context.getCacheDir(),"backup.zip")));
                                            ZipEntry ze;
                                            File f=new File(context.getFilesDir(),"Pictures");
                                            if (!f.exists()){
                                                f.mkdirs();
                                            }
                                            while((ze=zis.getNextEntry())!=null) {
                                                if (ze.isDirectory()) {
                                                    File file = new File(context.getFilesDir(), ze.getName());
                                                    if (!file.exists()) {
                                                        file.mkdirs();
                                                    }
                                                    continue;
                                                }
                                                    if (ze.getName().endsWith(".db")) {
                                                        FileOutputStream fos=new FileOutputStream(context.getDatabasePath(AllData.sqlDataBase.databasename));
                                                        byte[] b=new byte[1024*10];
                                                        int lenght;
                                                        while ((lenght=zis.read(b))!=-1){
                                                            fos.write(b,0,lenght);
                                                        }
                                                        fos.flush();
                                                        fos.close();
                                                    } else {
                                                        FileOutputStream fos=new FileOutputStream(new File(context.getFilesDir(),ze.getName()));
                                                        byte[] b=new byte[1024*10];
                                                        int lenght;
                                                        while ((lenght=zis.read(b))!=-1){
                                                            fos.write(b,0,lenght);
                                                        }
                                                        fos.flush();
                                                        fos.close();
                                                    }
                                                }
                                            zis.close();
                                            pdd.dismiss();
                                            finish();
                                            startActivity(new Intent(MainActivity.this,MainActivity.class));
                                        }catch (Exception e){
                                            pdd.dismiss();
                                            Toast.makeText(getBaseContext(), "Failed to download.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pdd.dismiss();
                                        Toast.makeText(getBaseContext(), "Failed to download.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        };
                        dcd1.showDialog("Download","Type confirm to download");
                        dcd1.setInputDesable();
                        break;
                }
                if (dl.isDrawerOpen(GravityCompat.START)) {
                    dl.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });
    }
    @SuppressLint("RestrictedApi")
    public void getAllMember(){
        fab.setVisibility(View.VISIBLE);
        AllMember am=new AllMember(context,mddb,fab,ma);
        allmember=true;
        NavigationView nv=findViewById(R.id.main_activity_nav_view);
        nv.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_activity_frame,am).commit();
    }
}
