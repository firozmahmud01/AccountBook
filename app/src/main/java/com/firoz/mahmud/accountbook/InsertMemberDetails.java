package com.firoz.mahmud.accountbook;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;


public class InsertMemberDetails extends Fragment {
    ImageView shop_iv,member_iv;
    EditText member_name,member_address,
    security,monthly_payment,
    member_phone_number,
    shop_address;
    ProgressDialog pd;
    Context context;
    int shop_code=10,member_code=20;
    Bitmap shop_bit=null,member_bit=null;
    FloatingActionButton fab;
    MainActivity ma;
    TextView date;
    Button done;
    public InsertMemberDetails(Context context,FloatingActionButton fab,final MainActivity ma){
        this.ma=ma;
        this.context=context;
        this.fab=fab;
        BackWork bw=new BackWork(){
            @Override
            public void customMethode()
            {
                ma.getAllMember();
            }
        };
        ma.bw=bw;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_insert_member_details, container, false);
        inisialize(view);
        pd=new ProgressDialog(context);
        pd.setMessage("Loading......");
        pd.setCancelable(false);
        shop_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(photoPickerIntent,"Pick shop picture"),shop_code);
            }
        });
        member_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(photoPickerIntent,"Pick face picture"),member_code);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker,int year,int moth,int day) {
                        date.setText(day+"/"+(moth+1)+"/"+year);
                    }
                },Integer.valueOf(date.getText().toString().split("/")[2])
                ,Integer.valueOf(date.getText().toString().split("/")[1])-1,Integer.valueOf(date.getText().toString().split("/")[0]));
                dp.show();
            }

        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String membername = member_name.getText().toString(),
                        memberaddress = member_address.getText().toString(),
                        phonenumber = member_phone_number.getText().toString(),
                        shopaddress = shop_address.getText().toString(),
                        mothlypayment = monthly_payment.getText().toString(),
                        securitys = security.getText().toString();
                if (member_bit == null ||
                        membername.isEmpty() ||
                        memberaddress.isEmpty() ||
                        phonenumber.isEmpty() ||
                        shopaddress.isEmpty() ||
                        mothlypayment.isEmpty() ||
                        securitys.isEmpty()) {
                    Toast.makeText(context, "Something you have forgotten", Toast.LENGTH_SHORT).show();
                    return;
                }else if (shop_bit==null){
                    Toast.makeText(context, "You have to add shop picture.\nTo do that click on the backgraund picture.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String timemiles = String.valueOf(System.currentTimeMillis());
                    Bitmap half = (new ImageGenarator()).cropRightSite(member_bit, 20);
                    File halffile = new File(AllData.picture.halfcirclepath(timemiles,context));
                    FileOutputStream halfout = new FileOutputStream(halffile);
                    half.compress(Bitmap.CompressFormat.PNG,50,halfout);
                    halfout.flush();
                    halfout.close();
                    Bitmap full=member_bit;
                    File fullfile=new File(AllData.picture.circlepath(timemiles,context));
                    FileOutputStream fullout=new FileOutputStream(fullfile);
                    full.compress(Bitmap.CompressFormat.PNG,50,fullout);
                    fullout.flush();
                    fullout.close();
                    File shopfile=new File(AllData.picture.shopPath(timemiles,context));
                    FileOutputStream shopout=new FileOutputStream(shopfile);
                    shop_bit.compress(Bitmap.CompressFormat.PNG,100,shopout);
                    shopout.flush();
                    shopout.close();
                    MemberDetailsDB mddb = new MemberDetailsDB(context, AllData.sqlDataBase.databasename);
                    mddb.addMember(membername,
                            memberaddress, phonenumber
                            ,fullfile.getAbsolutePath()
                    ,halffile.getAbsolutePath()
                    ,timemiles
                            ,shopfile.getAbsolutePath()
                            ,shopaddress,Integer.valueOf(securitys),Integer.valueOf(mothlypayment),date.getText().toString());

                    ma.getAllMember();
                }catch (Exception e){
                    Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    public void inisialize(View v){
        date=v.findViewById(R.id.insert_member_details_date_textview);
        date.setText(DateFormat.format("dd/MM/yyyy",new Date()));
        shop_iv=v.findViewById(R.id.insert_member_details_shop_pic_imageview);
        member_iv=v.findViewById(R.id.insert_member_details_profile_imageview);
        member_name=v.findViewById(R.id.insert_member_details_person_name_edittext);
        member_address=v.findViewById(R.id.insert_member_details_member_address_edittext);
        security=v.findViewById(R.id.insert_member_details_security_edittext);
        monthly_payment=v.findViewById(R.id.insert_member_details_monthly_payment_edittext);
        member_phone_number=v.findViewById(R.id.insert_member_details_phone_number_edittext);
        shop_address=v.findViewById(R.id.insert_member_details_shop_address_edittext);
        done=v.findViewById(R.id.insert_member_details_done_bar_button);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            if (requestCode==shop_code){
                try {
                    shop_bit =resize(MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData()));
                    shop_iv.setImageBitmap(shop_bit);
                }catch (Exception e){}
            }else if (requestCode==member_code){
                pd.show();
                    Thread th=new Thread() {
                        @Override
                        public void run() {
                                member_iv.getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            File file = new File(context.getCacheDir(), "tmp");
                                            if (file.exists()) {
                                                file.delete();
                                            }
                                            Bitmap tmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                                            FileOutputStream outputStream = new FileOutputStream(file);
                                            tmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                            outputStream.flush();
                                            outputStream.close();
                                            ImageGenarator ig = new ImageGenarator();
                                            if (file.exists()) {
                                                member_bit = ig.getFace(file.getAbsolutePath());
                                                if (member_bit == null) {
                                                    Toast.makeText(context, "No face found", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    member_bit = ig.circleBitmap(member_bit);
                                                    member_iv.setImageBitmap(member_bit);
                                                    Toast.makeText(context, "Image succesfully choosen", Toast.LENGTH_SHORT).show();
                                                }
                                                pd.dismiss();
                                            } else {
                                                pd.dismiss();
                                                Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (final Exception e) {
                                            pd.dismiss();
                                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                                        }
                                    }});
                        }
                    };
                    th.start();
            }
        }
    }
    public Bitmap resize(Bitmap bit){
        return Bitmap.createScaledBitmap(bit,600,(int)((600.00/bit.getWidth())*bit.getHeight()),false);
    }
}
