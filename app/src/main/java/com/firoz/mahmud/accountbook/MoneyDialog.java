package com.firoz.mahmud.accountbook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Date;

public abstract class MoneyDialog {
    private Context context;
    private EditText amount,details,confirm;
    Button done,cancle;
    TextView date;
    public MoneyDialog(Context context){
        this.context=context;
    }
    public void showDialog(String what,String samount,String sdetails,String sdate){
        final Dialog d=new Dialog(context);
        d.setContentView(R.layout.money_dialog);
        d.setTitle(what);
        inisialize(d);
        amount.setText(samount);
        details.setText(sdetails);
        date.setText(sdate);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount.getText().toString().isEmpty()
                        ||details.getText().toString().isEmpty()
                        ||confirm.getText()
                        .toString().isEmpty()){
                    Toast.makeText(context, "Fill up all blanks", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!confirm.getText().toString().equals("cf")){
                    Toast.makeText(context, "Please type \"cf\" to make sure.", Toast.LENGTH_SHORT).show();
                    return;
                }
                onCompleate(amount.getText().toString(),details.getText().toString()
                        ,Integer.valueOf(date.getText().toString().split("/")[0]),
                        Integer.valueOf(date.getText().toString().split("/")[1]),
                        Integer.valueOf(date.getText().toString().split("/")[2]));
                amount.setText("");
                details.setText("");
                confirm.setText("");
                d.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.setText("");
                details.setText("");
                confirm.setText("");
                d.dismiss();
            }
        });
        d.show();
    }
    public abstract void onCompleate(String amount,String details,int day,int month,int year);
    private void inisialize(Dialog d){
        amount=d.findViewById(R.id.money_dialog_amount_edittext);
        details=d.findViewById(R.id.money_dialog_details_edittext);
        confirm=d.findViewById(R.id.money_dialog_confirm_edittext);
        date=d.findViewById(R.id.money_dialog_date_textview);
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
        done=d.findViewById(R.id.money_dialog_done_button);
        cancle=d.findViewById(R.id.money_dialog_cancle_button);
    }
}
