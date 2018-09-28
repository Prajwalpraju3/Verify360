package com.covert.verify360;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.covert.verify360.AdapterClasses.MyAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationPhotoActivity extends AppCompatActivity {

    @BindView(R.id.image_list)
    RecyclerView image_list;
    @BindView(R.id.add_image)
    Button add_image;
    @BindView(R.id.upload_image)
    Button upload_image;

    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private String imageFilePath;

    private MyAdapter imageAdapter;
    private ArrayList<String> imageList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_loc_photo);
        ButterKnife.bind(this);

        // ImageAdapter
        image_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false));
        imageList = new ArrayList<>();
        imageAdapter = new MyAdapter(this, imageList);
        image_list.setAdapter(imageAdapter);
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
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @OnClick(R.id.add_image)
    public void openCameraIntent() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
//                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    if (data.getExtras().get("data") != null) {
                        imageList.add(imageFilePath);
                        imageAdapter.notifyDataSetChanged();
                        imageFilePath = "";
                        System.out.println("Mayur: " + imageList.size());
                        for (int i = 0; i < imageList.size(); i++) {
                            System.out.println("Mayur: path " + i + " " + imageList.get(i));
                        }
                    } else {
                        Toast.makeText(this, "Could not get image", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                imageFilePath = "";
            }
        }
    }
}
