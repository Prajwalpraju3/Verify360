package com.covert.verify360;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.covert.verify360.Fragments.CancelledCasesFragment;
import com.covert.verify360.Fragments.CompletedCasesFragment;
import com.covert.verify360.Fragments.EarningsFragment;
import com.covert.verify360.Fragments.FragmentDashboard;
import com.covert.verify360.Fragments.NewCasesFragment;
import com.covert.verify360.Fragments.PendingCasesFragment;
import com.covert.verify360.Fragments.ProfileSettingsFragment;
import com.covert.verify360.Fragments.ReAssignedCasesFragment;
import com.covert.verify360.Fragments.ReOpenedCasesFragment;
import com.covert.verify360.Fragments.ReportsFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_ALL = 1;
    public static TextView networkStatus;
    private static boolean isConnected;
    final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA};
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private View nav_header;
    private FragmentDashboard fragmentDashboard;
    private FragmentManager manager;
    private View view;
    private TextView navHeaderText;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private BroadcastReceiver receiver;

    public static void NetworkCheck(boolean connection) {
        if (connection) {
            isConnected = true;
            networkStatus.setText("Internet Available");
            networkStatus.setBackgroundColor(Color.parseColor("#17A832"));
            networkStatus.setTextColor(Color.WHITE);
            Handler handler = new Handler();
            Runnable delayrunnable = () -> networkStatus.setVisibility(View.GONE);
            handler.postDelayed(delayrunnable, 1000);
        } else {
            isConnected = false;
            networkStatus.setVisibility(View.VISIBLE);
            networkStatus.setText("Could not Connect to internet");
            networkStatus.setBackgroundColor(Color.RED);
            networkStatus.setTextColor(Color.WHITE);
        }
    }

    public static boolean isIsConnected() {
        return isConnected;
    }

    public static boolean checkPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String s : permissions) {
                if (ActivityCompat.checkSelfPermission(context, s) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_ALL:

                if (grantResults.length > 0) {

                    boolean External_Storage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean Fine_Location = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean Camera = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (External_Storage && Fine_Location && Camera) {

                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }


    @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            view = findViewById(android.R.id.content);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!checkPermissions(this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                }

            }


            initialize();
            receiver = new NetworkChangleReceiver();

            manager = getSupportFragmentManager();
            fragmentDashboard = new FragmentDashboard();
            manager.beginTransaction().add(R.id.container_fragment, fragmentDashboard, "DashBoard")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }

        private void registerBroadcast () {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }
        }

        @Override
        protected void onResume () {
            super.onResume();
            registerBroadcast();
        }

        @Override
        protected void onPause () {
            super.onPause();
            unregisterBroadcast();
        }


        private void initialize () {
            toolbar = findViewById(R.id.toolbar);
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.navigationView);
            nav_header = navigationView.inflateHeaderView(R.layout.nav_header);
            navHeaderText = nav_header.findViewById(R.id.textHeader);

            sharedPreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);
            editor = sharedPreferences.edit();

            if (sharedPreferences != null && sharedPreferences.contains("EMPLOYEE_NAME")) {
                navHeaderText.setText(sharedPreferences.getString("EMPLOYEE_NAME", "NA"));
            }
            networkStatus = findViewById(R.id.networkStatus);

            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setElevation(0);
            actionBarDrawerToggle = setDrawerToggle();
            drawerNavigationSetup(navigationView);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }


        private void drawerNavigationSetup ( final NavigationView navigationView){
            navigationView.setNavigationItemSelectedListener(item -> {
                selectDrawerItem(item);
                return true;
            });
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
            switch (item.getItemId()) {
                case android.R.id.home:
                    drawerLayout.openDrawer(GravityCompat.START);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void selectDrawerItem (MenuItem item){
            Fragment fragment = null;
            Class fragmentClass = null;
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.newCasesFragment:
                    fragmentClass = NewCasesFragment.class;
                    break;
                case R.id.pendingCasesFragment:
                    fragmentClass = PendingCasesFragment.class;
                    break;
                case R.id.completedCasesFragment:
                    fragmentClass = CompletedCasesFragment.class;
                    break;
                case R.id.cancelledCasesFragment:
                    fragmentClass = CancelledCasesFragment.class;
                    break;
                case R.id.reAssignedCasesFragment:
                    fragmentClass = ReAssignedCasesFragment.class;
                    break;
                case R.id.reOpenedCasesFragment:
                    fragmentClass = ReOpenedCasesFragment.class;
                    break;
                case R.id.reportsFragment:
                    fragmentClass = ReportsFragment.class;
                    break;
                case R.id.earningsFragment:
                    fragmentClass = EarningsFragment.class;
                    break;
                case R.id.profileSettingsFragment:
                    fragmentClass = ProfileSettingsFragment.class;
                    break;
                case R.id.logout:
                    editor.clear();
                    editor.commit();
                    finish();
                    break;
                default:
                    fragmentClass = FragmentDashboard.class;
            }

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                item.setChecked(true);
            }
            drawerLayout.closeDrawers();
        }

        private ActionBarDrawerToggle setDrawerToggle () {
            return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        }

        @Override
        protected void onPostCreate (@Nullable Bundle savedInstanceState){
            super.onPostCreate(savedInstanceState);
            actionBarDrawerToggle.syncState();
        }

        @Override
        public void onConfigurationChanged (Configuration newConfig){
            super.onConfigurationChanged(newConfig);
            actionBarDrawerToggle.onConfigurationChanged(newConfig);
        }

        @Override
        public void onBackPressed () {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else /*if (count == 0)*/ {
                super.onBackPressed();
            } /*else {
                *//*Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_fragment);
                if (fragment.equals(fragmentDashboard)) {
                    super.onBackPressed();
                }*//*
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, fragmentDashboard).commit();
            }*/
        }

        @Override
        protected void onDestroy () {
            super.onDestroy();
            unregisterBroadcast();
//        editor.clear();
        }

        private void unregisterBroadcast () {
            try {
                unregisterReceiver(receiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
