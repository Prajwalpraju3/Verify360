package com.covert.verify360;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.covert.verify360.MainActivity;

public class NetworkChangleReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            if(isConnected(context)){
                MainActivity.NetworkCheck(true);
            }else{
                MainActivity.NetworkCheck(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private boolean isConnected(Context context){
        try{
            ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                return (networkInfo!=null && networkInfo.isConnected());
            }
        }catch (NullPointerException e){
            return false;
        }
        return false;
    }
}
