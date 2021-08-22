package com.firoz.mahmud.accountbook;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Member;
import java.util.Date;


public class MemberDetails extends Fragment implements View.OnClickListener {

    ImageView shop_iv,member_iv;
    TextView member_name,member_address,member_number
            ,shop_address,security,monthly_payment,tdue,tpaid;
    MemberDetailsDB.MyData md;
    MainActivity ma;
    ProgressDialog pd;
    Context context;
    Bitmap member_bit,shop_bit;
    int member_code=1230,shop_code=1012;
    TextView date;
    public MemberDetails(MemberDetailsDB.MyData my,Context context,final MainActivity ma){
        this.context=context;
        this.md=my;
        this.ma=ma;
        BackWork bw=new BackWork(){
            @Override
            public void customMethode(){
               ma.getAllMember();
            }
        };
        ma.bw=bw;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_member_details, container, false);
        pd=new ProgressDialog(context);
        pd.setMessage("Loading....");
        pd.setCancelable(false);
        inisialize(view);
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_details_profile_imageview:
                Intent photoPickerIntent1 = new Intent(Intent.ACTION_PICK);
                photoPickerIntent1.setType("image/*");
                startActivityForResult(Intent.createChooser(photoPickerIntent1,"Pick face picture"),member_code);
                break;
            case R.id.member_details_shop_imageview:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(photoPickerIntent,"Pick shop picture"),shop_code);
                break;
            case R.id.member_details_person_name_textview:
                DetailsChangerDialog dcd = new DetailsChangerDialog(context, InputType.TYPE_CLASS_TEXT) {
                    @Override
                    public void getChangedData(String data) {
                        MemberDetailsDB mddb = new MemberDetailsDB(context, AllData.sqlDataBase.databasename);
                        md.setMember_name(data);
                        mddb.updateData(md);
                        reLoad();
                    }
                };
                dcd.showDialog("Member Name",member_name.getText().toString());
                break;
            case R.id.member_details_phone_number_textview:
                DetailsChangerDialog dcd1 = new DetailsChangerDialog(context, InputType.TYPE_CLASS_PHONE) {
                    @Override
                    public void getChangedData(String data) {
                        MemberDetailsDB mddb = new MemberDetailsDB(context, AllData.sqlDataBase.databasename);
                        md.setPhone_number(data);
                        mddb.updateData(md);
                        reLoad();
                    }
                };
                dcd1.showDialog("Phone Number",member_number.getText().toString());
                break;
            case R.id.member_details_security_textview:
                DetailsChangerDialog dcd2 = new DetailsChangerDialog(context, InputType.TYPE_CLASS_NUMBER) {
                    @Override
                    public void getChangedData(String data) {
                        MemberDetailsDB mddb = new MemberDetailsDB(context, AllData.sqlDataBase.databasename);
                        md.setSecurity(Integer.valueOf(data));
                        mddb.updateData(md);
                        reLoad();
                    }
                };
                dcd2.showDialog("Security",security.getText().toString());
                break;
            case R.id.member_details_monthly_payment_textview:
                DetailsChangerDialog dcd3 = new DetailsChangerDialog(context, InputType.TYPE_CLASS_NUMBER) {
                    @Override
                    public void getChangedData(String data) {
                        MemberDetailsDB mddb = new MemberDetailsDB(context, AllData.sqlDataBase.databasename);
                        md.setMonthly_payment(Integer.valueOf(data));
                        mddb.updateData(md);
                        reLoad();
                    }
                };
                dcd3.showDialog("Monthly Payment",monthly_payment.getText().toString());
                break;
            case R.id.member_details_member_address_textview:
                DetailsChangerDialog dcd4 = new DetailsChangerDialog(context, InputType.TYPE_CLASS_TEXT) {
                    @Override
                    public void getChangedData(String data) {
                        MemberDetailsDB mddb = new MemberDetailsDB(context, AllData.sqlDataBase.databasename);
                        md.setAddress(data);
                        mddb.updateData(md);
                        reLoad();
                    }
                };
                dcd4.showDialog("Home Address",member_address.getText().toString());
                break;
            case R.id.member_details_shop_address_textview:
                DetailsChangerDialog dcd5 = new DetailsChangerDialog(context, InputType.TYPE_CLASS_TEXT) {
                    @Override
                    public void getChangedData(String data) {
                        MemberDetailsDB mddb = new MemberDetailsDB(context, AllData.sqlDataBase.databasename);
                        md.setShop_address(data);
                        mddb.updateData(md);
                        reLoad();
                    }
                };
                dcd5.showDialog("Shop Address",shop_address.getText().toString());
                break;
            case R.id.member_details_date_textview:
                DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int moth, int day) {
                        DetailsChangerDialog dcd=new DetailsChangerDialog(context,InputType.TYPE_CLASS_TEXT) {
                            @Override
                            public void getChangedData(String data) {
                                MemberDetailsDB mddb=new MemberDetailsDB(context,AllData.sqlDataBase.databasename);
                                md.setDate(data);
                                mddb.updateData(md);
                                reLoad();
                            }
                        };
                        dcd.showDialog("Date",day+"/"+(moth+1)+"/"+year);
                        dcd.setInputDesable();
                    }
                }, Integer.valueOf(date.getText().toString().split("/")[2])
                        , Integer.valueOf(date.getText().toString().split("/")[1]) - 1, Integer.valueOf(date.getText().toString().split("/")[0]));
                dpd.show();
                break;
            case R.id.member_details_total_due_textview:
                MoneyDetails moneyDetails = new MoneyDetails(ma.fab,md,ma,context);
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_frame, moneyDetails).commit();
                break;
            case R.id.member_details_total_payed_textview:
                MoneyDetails moneyDetails1 = new MoneyDetails(ma.fab,md,ma,context);
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_frame, moneyDetails1).commit();
                break;
        }
    }
    private void reLoad(){
        MemberDetails memberDetails=new MemberDetails(md,context,ma);
        getFragmentManager().beginTransaction()
                .replace(R.id.main_activity_frame,memberDetails).commit();
    }
    private void inisialize(View v){
        shop_iv=v.findViewById(R.id.member_details_shop_imageview);
        shop_iv.setOnClickListener(this);
        shop_iv.setImageURI(Uri.parse(md.getShop_pic_path()));
        member_iv=v.findViewById(R.id.member_details_profile_imageview);
        member_iv.setOnClickListener(this);
        date=v.findViewById(R.id.member_details_date_textview);
        date.setOnClickListener(this);
        date.setText(md.getDate());
        member_iv.setImageURI(Uri.parse(md.getFull_circle_pic_path()));
        member_name=v.findViewById(R.id.member_details_person_name_textview);
        member_name.setOnClickListener(this);
        member_name.setText(md.getMember_name());
        member_address=v.findViewById(R.id.member_details_member_address_textview);
        member_address.setOnClickListener(this);
        member_address.setText(md.getAddress());
        member_number=v.findViewById(R.id.member_details_phone_number_textview);
        member_number.setOnClickListener(this);
        member_number.setText(md.getPhone_number());
        shop_address=v.findViewById(R.id.member_details_shop_address_textview);
        shop_address.setOnClickListener(this);
        shop_address.setText(md.getShop_address());
        security=v.findViewById(R.id.member_details_security_textview);
        security.setOnClickListener(this);
        security.setText(""+md.getSecurity());
        monthly_payment=v.findViewById(R.id.member_details_monthly_payment_textview);
        monthly_payment.setOnClickListener(this);
        monthly_payment.setText(""+md.getMonthly_payment());
        tdue=v.findViewById(R.id.member_details_total_due_textview);
        DueDb dueDb=new DueDb(context,AllData.sqlDataBase.databasename);
        tdue.setText(dueDb.getTotalAmount(md.getTimemiles())+"");
        tdue.setOnClickListener(this);
        tpaid=v.findViewById(R.id.member_details_total_payed_textview);
        PaidDb paidDb=new PaidDb(context,AllData.sqlDataBase.databasename);
        tpaid.setText(paidDb.getTotalAmount(md.getTimemiles())+"");
        tpaid.setOnClickListener(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            if (requestCode==shop_code){
                try {
                    shop_bit =resize(MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData()));
                    DetailsChangerDialog dcd=new DetailsChangerDialog(context,InputType.TYPE_CLASS_TEXT) {
                        @Override
                        public void getChangedData(String data) {
                            try {
                            File shopf = new File(AllData.picture.shopPath(md.getTimemiles(), context));
                            if (shopf.exists()) {
                                shopf.delete();
                            }
                            shop_bit.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(shopf));
                            md.setShop_pic_path(shopf.getAbsolutePath());
                            MemberDetailsDB mddb = new MemberDetailsDB(context, AllData.sqlDataBase.databasename);
                            mddb.updateData(md);
                            reLoad();
                        }catch (Exception e){
                            Toast.makeText(context, "Failed due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        }};
                    dcd.showDialog("Shop Picture","Are you sure?");
                    dcd.setInputDesable();
                }catch (Exception e){
                    Toast.makeText(context, "Failed due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }else if (requestCode==member_code){
                pd.show();
                Thread th=new Thread(){
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
                                    pd.dismiss();
                                    DetailsChangerDialog dcd = new DetailsChangerDialog(context, InputType.TYPE_CLASS_TEXT) {
                                        @Override
                                        public void getChangedData(String data) {
                                            File member_full = new File(AllData.picture.circlepath(md.getTimemiles(), context));
                                            if (member_full.exists()) {
                                                member_full.delete();
                                            }
                                            try {
                                                member_bit.compress(Bitmap.CompressFormat.PNG, 50, new FileOutputStream(member_full));
                                                MemberDetailsDB mddb = new MemberDetailsDB(context, AllData.sqlDataBase.databasename);
                                                md.setFull_circle_pic_path(member_full.getAbsolutePath());
                                                Bitmap half = (new ImageGenarator()).cropRightSite(member_bit, 20);
                                                File halff = new File(AllData.picture.halfcirclepath(md.getTimemiles(), context));
                                                if (halff.exists()) {
                                                    halff.delete();
                                                }
                                                half.compress(Bitmap.CompressFormat.PNG, 50, new FileOutputStream(halff));
                                                md.setHalf_circle_pic_path(halff.getAbsolutePath());
                                                mddb.updateData(md);
                                                reLoad();
                                            } catch (Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(context, "Failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    };
                                    dcd.showDialog("Member Picture", "Are you sure?");
                                    dcd.setInputDesable();
                                    Toast.makeText(context, "Image succesfully choosen", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                        }

                            }
                        });
                    }};
                th.start();
            }
        }
    }
    public Bitmap resize(Bitmap bit){
        return Bitmap.createScaledBitmap(bit,600,(int)((600.00/bit.getWidth())*bit.getHeight()),false);
    }
}
