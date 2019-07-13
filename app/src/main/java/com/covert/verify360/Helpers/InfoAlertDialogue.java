package com.covert.verify360.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class InfoAlertDialogue {
    private Context context;
    public InfoAlertDialogue(Activity activity){
        this.context= activity;
    }

    public void ShowDialogue(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog alert = dialog.create();
        alert.show();
    }
}
