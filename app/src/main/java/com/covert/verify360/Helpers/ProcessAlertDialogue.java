package com.covert.verify360.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.covert.verify360.R;

public class ProcessAlertDialogue {
    private Context context;
    private AlertDialog.Builder dialog;
    private AlertDialog alert;
    public ProcessAlertDialogue(Activity activity){
        this.context= activity;
        dialog = new AlertDialog.Builder(this.context);
    }

    public void ShowDialogue(){
        dialog.setCancelable(false);
        dialog.setView(R.layout.process_alert);
        alert = dialog.create();
        alert.show();
        alert.getWindow().setLayout(300, 300);
    }

    public void CloseDialogue(){
        alert.dismiss();
    }
}
