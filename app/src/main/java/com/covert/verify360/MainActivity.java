package com.covert.verify360;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.covert.verify360.BeanClasses.LoginResponse;
import com.covert.verify360.BeanClasses.LogoutResponse;
import com.covert.verify360.BeanClasses.LogoutUser;
import com.covert.verify360.BeanClasses.PendingCaseDetails;
import com.covert.verify360.BeanClasses.User;
import com.covert.verify360.Fragments.CancelledCasesDetailsFragment;
import com.covert.verify360.Fragments.CancelledCasesFragment;
import com.covert.verify360.Fragments.CompletedCaseDetailsFragment;
import com.covert.verify360.Fragments.CompletedCasesFragment;
import com.covert.verify360.Fragments.EarningsFragment;
import com.covert.verify360.Fragments.FragmentDashboard;
import com.covert.verify360.Fragments.ModifiedNewCasesFragment;
import com.covert.verify360.Fragments.NewCasesFragment;
import com.covert.verify360.Fragments.PendingCasesFragment;
import com.covert.verify360.Fragments.ProfileSettingsFragment;
import com.covert.verify360.Fragments.ReAssignedCasesFragment;
import com.covert.verify360.Fragments.ReOpenedCasesFragment;
import com.covert.verify360.Fragments.ReportsFragment;
import com.covert.verify360.Helpers.InfoAlertDialogue;
import com.covert.verify360.Helpers.ProcessAlertDialogue;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.net.InetAddress;
import java.util.List;
import java.util.Objects;

import Services.FactoryService;
import Services.Login;
import Services.Logout;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private ProcessAlertDialogue processAlertDialogue;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    Double lat=0.0, lon=0.0;
    public static FragmentManager fManager;
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
    ImageView logOutIv;


    public FragmentManager getFmanager(){
        if(MainActivity.fManager==null){
            MainActivity.fManager= getSupportFragmentManager();
        }
        return fManager;
    }
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
        Runnable run= new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress ipAddr = InetAddress.getByName("www.google.com");
                    System.out.println("##IP ADD"+ipAddr);
                    isConnected= !ipAddr.equals("");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.fillInStackTrace());
                    isConnected=false;
                }
            }
        };
        Thread t=new Thread(run);
        t.start();
        while(t.isAlive()){}
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
                        //Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        new InfoAlertDialogue(MainActivity.this).ShowDialogue(getString(R.string.information), "Please grant permissions to use application.");
                    }
                }

                break;
        }
    }


    @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            fManager=getSupportFragmentManager();
            view = findViewById(android.R.id.content);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!checkPermissions(this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                }
            }
            processAlertDialogue= new ProcessAlertDialogue(MainActivity.this);
            initialize();
            receiver = new NetworkChangleReceiver();
            manager = getSupportFragmentManager();
            if(manager.getBackStackEntryCount()==0){
                fragmentDashboard = new FragmentDashboard();
                manager.beginTransaction().add(R.id.container_fragment, fragmentDashboard, "DashBoard")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
            initLocation();
            this.getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        Fragment current = getCurrentFragment();
                        if (current instanceof FragmentDashboard) {
                            navigationView.setCheckedItem(R.id.dashboard);
                        } else if(current instanceof NewCasesFragment || current instanceof ModifiedNewCasesFragment) {
                            navigationView.setCheckedItem(R.id.newCasesFragment);
                        } else if(current instanceof PendingCasesFragment) {
                            navigationView.setCheckedItem(R.id.pendingCasesFragment);
                        } else if(current instanceof CompletedCasesFragment || current instanceof CompletedCaseDetailsFragment) {
                            navigationView.setCheckedItem(R.id.completedCasesFragment);
                        } else if(current instanceof CancelledCasesFragment || current instanceof CancelledCasesDetailsFragment) {
                            navigationView.setCheckedItem(R.id.cancelledCasesFragment);
                        } else if(current instanceof ReOpenedCasesFragment) {
                            navigationView.setCheckedItem(R.id.reOpenedCasesFragment);
                        } else if(current instanceof ReportsFragment) {
                            navigationView.setCheckedItem(R.id.reportsFragment);
                        } else if(current instanceof EarningsFragment) {
                            navigationView.setCheckedItem(R.id.earningsFragment);
                        }
                    }
                });
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
            logOutIv = navigationView.findViewById(R.id.logout_imageview);
            nav_header = navigationView.inflateHeaderView(R.layout.nav_header);
            navHeaderText = nav_header.findViewById(R.id.textHeader);
            sharedPreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            if (sharedPreferences != null && sharedPreferences.contains("EMPLOYEE_NAME")) {
                navHeaderText.setText(sharedPreferences.getString("EMPLOYEE_NAME", "NA"));
            }
            networkStatus = findViewById(R.id.networkStatus);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setElevation(0);
            actionBarDrawerToggle = setDrawerToggle();
            drawerNavigationSetup(navigationView);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            logOutIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckLogoutStatus();
                }
            });
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

                case R.id.dashboard:
                    fragmentClass = FragmentDashboard.class;
                    break;

                case R.id.newCasesFragment:
                    //fragmentClass = NewCasesFragment.class;
                    fragmentClass = ModifiedNewCasesFragment.class;
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
                /*case R.id.reAssignedCasesFragment:
                    fragmentClass = ReAssignedCasesFragment.class;
                    break;*/
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
               /* case R.id.logout:
                    editor.clear();
                    editor.commit();
                    finish();
                    break;*/
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

        /*@Override
        public void onBackPressed () {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else{
                super.onBackPressed();
            }
        }*/

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

        public Fragment getCurrentFragment() {
            return this.getSupportFragmentManager().getPrimaryNavigationFragment();
        }

        int cnt=0;
        private int CheckLogoutStatus() {
            initLocation();
            if(!checkLocPermission()){
                Toast.makeText(MainActivity.this, "Set GPS permission.", Toast.LENGTH_SHORT).show();
                return -2;
            }
            if (0.0 == lat || 0.0 == lon) {
                if(cnt>=2){
                    cnt=0;
                    new InfoAlertDialogue(MainActivity.this).ShowDialogue(getString(R.string.information), "Unable to get location, please try again later.");
                    return -5;
                }
                processAlertDialogue.ShowDialogue();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cnt++;
                        processAlertDialogue.CloseDialogue();
                        CheckLogoutStatus();
                    }
                },6000);
                return -1;
            }else{
                stopLocationUpdates();
            }
            String empid = null;
            if (sharedPreferences != null && sharedPreferences.contains("EMP_ID")) {
                empid = sharedPreferences.getString("EMP_ID", "");
            }
            processAlertDialogue.ShowDialogue();
            Logout logout = FactoryService.createService(Logout.class);
            Call<LogoutResponse> loginCall = logout.logout(empid, lat, lon);
            loginCall.enqueue(new Callback<LogoutResponse>() {
                @Override
                public void onResponse(Call<LogoutResponse> call, retrofit2.Response<LogoutResponse> response) {
                    if (response.isSuccessful()) {
                        final String msg = response.body().getError();
                        if (msg.equals("false")) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    processAlertDialogue.CloseDialogue();
                                    List<LogoutUser> user = response.body().getUser();
                                    LogoutUser userDetails = user.get(0);
                                    int flag=userDetails.getFlag();
                                    if(flag==0){
                                        new InfoAlertDialogue(MainActivity.this).ShowDialogue(getString(R.string.information), "Please finish urgent cases and logout.");
                                    }else {
                                        editor.clear();
                                        editor.commit();
                                        finish();
                                    }
                                }
                            },3000);
                        } else {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "User Not Found  ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LogoutResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
            return 1;
        }



    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkLocPermission() {
        boolean isAllAllowed = false;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            isAllAllowed = false;
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        59);
            } else {
                Toast.makeText(this,
                        "Please go to application settings & provide permissions",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            isAllAllowed = true;
        }
        return isAllAllowed;
    }

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    private void initLocation(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                if (mCurrentLocation != null){
                    lat = mCurrentLocation.getLatitude();
                    lon = mCurrentLocation.getLongitude();
                    stopLocationUpdates();
                }
            }
        };
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startLocUpdate();
            }
        }, 1000);
    }

    private void startLocUpdate(){
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        if (mCurrentLocation != null) {
                            lat = mCurrentLocation.getLatitude();
                            lon = mCurrentLocation.getLongitude();
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //dismissDialog();
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this,
                                            REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
//                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        }

                        if (mCurrentLocation != null){
                            lat = mCurrentLocation.getLatitude();
                            lon = mCurrentLocation.getLongitude();
                        }
                    }
                });
    }

    public void stopLocationUpdates() {
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
    }
