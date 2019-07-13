package com.covert.verify360;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.covert.verify360.BeanClasses.LoginResponse;
import com.covert.verify360.BeanClasses.User;
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

import java.util.List;

import Services.FactoryService;
import Services.Login;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.username_text)
    EditText textUsername;
    @BindView(R.id.password_text)
    EditText textPassword;
    @BindView(R.id.rememberme_sw)
    Switch rememberMeSw;
    @BindView(R.id.button_login)
    ImageButton login;
    @BindView(R.id.progressBar)ProgressBar progressBar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProcessAlertDialogue processAlertDialogue;

    public static final int PERMISSION_ALL = 1;
    final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA};

    private String usernameText, passwordText;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    Double lat=0.0, lon=0.0;

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
                        Toast.makeText(LoginActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modified_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        }
        processAlertDialogue= new ProcessAlertDialogue(this);
        ButterKnife.bind(this);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sharedPreferences=getSharedPreferences("USER_DETAILS",MODE_PRIVATE);
        initLocation();
    }

    int cnt=0;
    @OnClick(R.id.button_login)
    void login() {
        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = null;
            if (manager != null) {
                info = manager.getActiveNetworkInfo();
            }
            if (((info != null) && info.isConnected())) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                if (TextUtils.isEmpty(textUsername.getText())) {
                    textUsername.requestFocus();
                    textUsername.setError("Please Enter Username");
                } else if (TextUtils.isEmpty(textPassword.getText())) {
                    textPassword.requestFocus();
                    textPassword.setError("Please Enter Password");
                } else {
                    if (!checkLocPermission()) {
                        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                        return;
                    }
                    if (0.0 == lat || 0.0 == lon) {
                        if (cnt >= 2) {
                            cnt = 0;
                            new InfoAlertDialogue(LoginActivity.this).ShowDialogue(getString(R.string.information), "Unable to fetch location, Please on GPS and try again.");
                        }
                        processAlertDialogue.ShowDialogue();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                processAlertDialogue.CloseDialogue();
                                cnt++;
                                login();
                            }
                        }, 7000);
                    }
                    processAlertDialogue.ShowDialogue();
                    usernameText = textUsername.getText().toString().trim();
                    passwordText = textPassword.getText().toString().trim();
                    Login login = FactoryService.createService(Login.class);
                    Call<LoginResponse> loginCall = login.login(usernameText, passwordText, lat, lon);
                    loginCall.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                final String msg = response.body().getError();
                                if (msg.equals("false")) {
                                    List<User> user = response.body().getUser();
                                    User userDetails = user.get(0);
                                    if (userDetails.getUserLoginID().equals(usernameText)) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        editor = sharedPreferences.edit();
                                        editor.putString("USER_LOGIN_ID", userDetails.getUserLoginID());
                                        editor.putString("EMPLOYEE_NAME", userDetails.getEmployeeName());
                                        editor.putString("EMP_ID", userDetails.getEmpId());
                                        editor.putString("user_id", userDetails.getUserId());
                                        editor.putString("ROLE_NAME", userDetails.getRoleName());
                                        editor.putBoolean("REMEMBER_ME", rememberMeSw.isChecked());
                                        editor.apply();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                processAlertDialogue.CloseDialogue();
                                                startActivity(intent);
                                                finish();
                                            }
                                        }, 3000);
                                    }
                                } else {
                                    processAlertDialogue.CloseDialogue();
                                    new InfoAlertDialogue(LoginActivity.this).ShowDialogue(getString(R.string.information), "User not found!");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            processAlertDialogue.CloseDialogue();
                            System.out.println("Error in Login:" + t.getMessage());
                            t.printStackTrace();
                            new InfoAlertDialogue(LoginActivity.this).ShowDialogue(getString(R.string.information), getString(R.string.unable_to_process));
                        }
                    });
                }
            } else {
                new InfoAlertDialogue(LoginActivity.this).ShowDialogue(getString(R.string.information), "Internet connection lost! Please try again.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
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
                new InfoAlertDialogue(LoginActivity.this).ShowDialogue(getString(R.string.information),"Please go to application settings & provide permissions");
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
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(LoginActivity.this,
                                            REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
//
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
