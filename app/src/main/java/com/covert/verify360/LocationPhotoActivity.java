package com.covert.verify360;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.covert.verify360.AdapterClasses.MyAdapter;
import com.covert.verify360.BeanClasses.ResponseMessage;
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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Services.FactoryService;
import Services.UploadImage;
import Utils.Constants;
import Utils.CustomProgressDialogTwo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 0 -> Resident
 * 1 -> Business
 * 2 -> Payslip
 */

public class LocationPhotoActivity extends AppCompatActivity {

    @BindView(R.id.image_list)
    RecyclerView image_list;
    @BindView(R.id.add_image)
    Button add_image;
    @BindView(R.id.upload_data)
    Button upload_data;

    private CustomProgressDialogTwo progressDialog;

    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private String imageFilePath;

    private MyAdapter imageAdapter;
    private ArrayList<String> imageList;

    private int selector = -1;
    private String caseId, caseDetailId, workingBy;
    private double lat, lon;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_loc_photo);
        ButterKnife.bind(this);
        progressDialog = new CustomProgressDialogTwo(this, R.drawable.progress);

        Intent intent = getIntent();
        selector = intent.getIntExtra(Constants.KEY_SELCTOR, -1);
        caseId = intent.getStringExtra(Constants.KEY_CASE_ID);
        caseDetailId = intent.getStringExtra(Constants.KEY_CASE_DETAIL_ID);
        workingBy = intent.getStringExtra(Constants.KEY_WORKING_BY);

        // ImageAdapter
        image_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false));
        imageList = new ArrayList<>();
        imageAdapter = new MyAdapter(this, imageList);
        image_list.setAdapter(imageAdapter);

    }

    @OnClick(R.id.add_image)
    public void openCameraIntent() {
        if (checkPermissions()) {
            Intent pictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                //Create a file to store the image
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.covert.verify360.provider", photoFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            photoURI);
                    startActivityForResult(pictureIntent,
                            REQUEST_CAPTURE_IMAGE);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermissions() {
        boolean isAllAllowed = false;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
            isAllAllowed = false;
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, REQUEST_CAPTURE_IMAGE);
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
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                if (mCurrentLocation != null){
                    lat = mCurrentLocation.getLatitude();
                    lon = mCurrentLocation.getLongitude();
                    System.out.println("Mayur: mLat: "+lat);
                    System.out.println("Mayur: mLon: "+lon);
                    Toast.makeText(LocationPhotoActivity.this, "Location fetched", Toast.LENGTH_SHORT).show();
                    uploadImages();
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
                showProgressDialog();
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
//                        Log.i(TAG, "All location settings are satisfied.");
                        dismissDialog();
                        Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
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
                        dismissDialog();
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                /*Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");*/
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(LocationPhotoActivity.this,
                                            REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
//                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
//                                Log.e(TAG, errorMessage);

//                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        if (mCurrentLocation != null){
                            lat = mCurrentLocation.getLatitude();
                            lon = mCurrentLocation.getLongitude();
                        }
                    }
                });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    /**
     * For creating file that will store the captured image
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @OnClick(R.id.upload_data)
    public void uploadData() {
        if (checkLocPermission()){
            initLocation();
        }

        // TODO: 9/29/2018 Get location here and once you get location, set lat-lon in lat, lon variables
        // and then call uploadImages();
    }

    private void uploadImages() {
        if (imageList.size() <= 0) {
            Toast.makeText(this, "Choose image first", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < imageList.size(); i++) {
            uploadImage(new File(imageList.get(i)), "doc for text", lat, lon);
        }
    }

    private void uploadImage(File file, String docFor, double lat, double lon) {
        showProgressDialog();
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload",
                file.getName(),
                reqFile);

        UploadImage submissionService = FactoryService.createFileService(UploadImage.class);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
        RequestBody xCaseID = RequestBody.create(MediaType.parse("text/plain"), caseId);
        RequestBody xCaseDetailsId = RequestBody.create(MediaType.parse("text/plain"), caseDetailId);
        RequestBody xDocFor = RequestBody.create(MediaType.parse("text/plain"), docFor);
        RequestBody xWorkingBy = RequestBody.create(MediaType.parse("text/plain"), workingBy);
        RequestBody xLat = RequestBody.create(MediaType.parse("text/plain"), "" + lat);
        RequestBody xLon = RequestBody.create(MediaType.parse("text/plain"), "" + lon);
        System.out.println("Mayur: lat: " + lat);
        System.out.println("Mayur: lon: " + lon);
        Call<ResponseMessage> call = submissionService.uploadImage(body, name,
                xCaseID, xCaseDetailsId,
                xDocFor, xWorkingBy, xLat, xLon);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        dismissDialog();
                        Toast.makeText(LocationPhotoActivity.this,
                                "Form details submitted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                t.printStackTrace();
                dismissDialog();
                Toast.makeText(LocationPhotoActivity.this,
                        "We found some glitch", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == RESULT_OK) {
//                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imageList.add(imageFilePath);
                imageAdapter.notifyDataSetChanged();
                imageFilePath = "";
            } else {
                imageFilePath = "";
            }
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialogTwo(this, R.drawable.progress);
        }
        if (!isFinishing() && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissDialog() {
        if (progressDialog != null && !isFinishing()) {
            if (progressDialog.isShowing()) {
                progressDialog.hide();
            }
        }
    }

    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }
}
