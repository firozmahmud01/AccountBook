package com.firoz.mahmud.accountbook;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public abstract class ConfirmRemoveDialog {
    EditText confirm;
    ImageView face;
    TextView name;
    Button yes,no;
    private Context context;
    public ConfirmRemoveDialog(Context context){
        this.context=context;
    }
    public void showDialog(String sname,String path){
        final Dialog d=new Dialog(context);
        d.setTitle("Remove "+sname);
        d.setContentView(R.layout.remove_member_dialog);
        inisialize(d);
        face.setImageURI(Uri.parse(path));
        name.setText(sname);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!confirm.getText().toString().equals("cf")){
                    Toast.makeText(context, "Type \"cf\" to make sure.", Toast.LENGTH_SHORT).show();
                    return;
                }
                onComplete();
                d.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
    }
    public abstract void onComplete();

    private void inisialize(Dialog d) {
        confirm=d.findViewById(R.id.remove_member_dialog_confirm_edittext);
        face=d.findViewById(R.id.remove_member_dialog_face_imageview);
        name=d.findViewById(R.id.remove_member_dialog_name_textview);
        yes=d.findViewById(R.id.remove_member_dialog_yes_button);
        no=d.findViewById(R.id.remove_member_dialog_no_button);
    }
}
