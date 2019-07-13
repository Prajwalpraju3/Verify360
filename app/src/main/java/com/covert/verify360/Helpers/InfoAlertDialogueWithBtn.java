package com.covert.verify360.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.covert.verify360.R;

public class InfoAlertDialogueWithBtn {
    private Context context;

    public InfoAlertDialogueWithBtn(Activity activity){
        this.context= activity;
    }
    public void ShowDialogue(String title, String message, OnAlertBtnClick onClik) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.context, R.style.AlertDialogTheme);
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClik.onClick(false);
                dialogInterface.dismiss();
            }
        });
        final AlertDialog alert = dialog.create();
        try{
            alert.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
