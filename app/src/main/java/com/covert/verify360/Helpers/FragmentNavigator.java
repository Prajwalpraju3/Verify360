package com.covert.verify360.Helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.covert.verify360.MainActivity;
import com.covert.verify360.R;

public class FragmentNavigator {
    public static void Navigate(FragmentManager fragmentManager, Fragment fragment){
        if(fragmentManager==null){
            fragmentManager= new MainActivity().getFmanager();
        }
        fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
}
