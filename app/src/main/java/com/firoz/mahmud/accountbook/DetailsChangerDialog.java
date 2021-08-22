package com.firoz.mahmud.accountbook;

import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public abstract class DetailsChangerDialog {
    Context context;
    EditText change,confirm;
    Button done,cancle;
    int type;
    public DetailsChangerDialog(Context context,int type){
        this.context=context;
        this.type=type;
    }
    public void showDialog(String where,String text){
        final Dialog d=new Dialog(context);
        d.setContentView(R.layout.detailschangerdialog);
        d.setTitle(where);
        inisialize(d);
        change.setHint(where);
        change.setInputType(type);
        change.setText(text);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change.getText().toString().isEmpty()||confirm.getText()
                .toString().isEmpty()){
                    Toast.makeText(context, "Fill up all blanks", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!confirm.getText().toString().equals("cf")){
                    Toast.makeText(context, "Please type \"cf\" to make sure.", Toast.LENGTH_SHORT).show();
                    return;
                }
                getChangedData(change.getText().toString());
                change.setText("");
                confirm.setText("");
                d.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change.setText("");
                confirm.setText("");
                d.dismiss();
            }
        });
        d.show();
    }
    public abstract void getChangedData(String data);
public void inisialize(Dialog d){
        change=d.findViewById(R.id.deatails_changer_change_dialog_editText);
        confirm=d.findViewById(R.id.deatails_changer_confirm_dialog_editText);
        done=d.findViewById(R.id.details_changer_changer_done_dialog_button);
        cancle=d.findViewById(R.id.details_changer_changer_cancle_dialog_button);
}
public void setInputDesable(){
    change.setEnabled(false);
}
}
