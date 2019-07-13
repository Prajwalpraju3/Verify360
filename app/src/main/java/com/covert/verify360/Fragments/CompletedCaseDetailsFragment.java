package com.covert.verify360.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.covert.verify360.BeanClasses.CompCasesUser;
import com.covert.verify360.BeanClasses.NewCasesBean;
import com.covert.verify360.MainActivity;
import com.covert.verify360.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.Objects;

import Services.DownloadService;
import Services.FileDownloader;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedCaseDetailsFragment extends Fragment {

    View v;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    TextView case_id, activity_id, client, pre_post, product_type, activity_type, company_name,
            applicant_name, father_name, mobile_number, email, address, est_complete_date, remarks,
            Prior, ComplinTAT, CompleteDate;
    Button buttonDownload, buttonAccept, buttonReject, viewButton;
    CompCasesUser compCasesUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.completedcases_details_fragment,container,false);
        sharedPreferences = getActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Case Details");
        progressBar = v.findViewById(R.id.progressBar);
        try {
            Bundle bundle= getArguments();
            compCasesUser = (CompCasesUser) bundle.getSerializable("xyz");
        }catch (Exception e){
            e.printStackTrace();
        }

        case_id = v.findViewById(R.id.case_id);
        activity_id = v.findViewById(R.id.activity_id);
        client = v.findViewById(R.id.client);
        pre_post = v.findViewById(R.id.pre_post);
        product_type = v.findViewById(R.id.product_type);
        activity_type = v.findViewById(R.id.activity_type);
        company_name = v.findViewById(R.id.company_name);
        applicant_name = v.findViewById(R.id.applicant_name);
        father_name = v.findViewById(R.id.father_name);
        mobile_number = v.findViewById(R.id.mobile_number);
        email = v.findViewById(R.id.email);
        address = v.findViewById(R.id.address);
        est_complete_date = v.findViewById(R.id.est_complete_date);
        CompleteDate= v.findViewById(R.id.complete_date);
        remarks = v.findViewById(R.id.remarks);
        Prior=v.findViewById(R.id.tv_priority);
        ComplinTAT= v.findViewById(R.id.completed_in_tat);
        buttonAccept = v.findViewById(R.id.acceptButton);
        buttonReject = v.findViewById(R.id.rejectButton);
        buttonDownload = v.findViewById(R.id.downloadButton);
        viewButton = v.findViewById(R.id.viewButton);

        case_id.setText(compCasesUser.getCase_id());
        activity_id.setText(compCasesUser.getCase_detail_id());
        client.setText(compCasesUser.getClient_name());
        pre_post.setText(compCasesUser.getPre_post());
        product_type.setText(compCasesUser.getProduct_type());
        activity_type.setText(compCasesUser.getActivity_type());
        company_name.setText(compCasesUser.getCompany_name());
        applicant_name.setText(compCasesUser.getApplicant_first_name().trim()
                + " " + compCasesUser.getApplicant_last_name().trim());
        father_name.setText(compCasesUser.getFather_name().trim());
        mobile_number.setText(compCasesUser.getPrimary_contact().trim());
        email.setText(compCasesUser.getEmail().trim());
        address.setText(compCasesUser.getDoor_number().trim() + ", "
                + compCasesUser.getStreet_address().trim() + ", "
                + compCasesUser.getLandmark().trim() + "\n"
                + compCasesUser.getLocation().trim() + ", "
                + compCasesUser.getPincode().trim() + "\n" + compCasesUser.getRegion_name().trim());
        CompleteDate.setText(compCasesUser.getCompleted_date().trim());
        est_complete_date.setText(compCasesUser.getEst_completed_date().trim());
        remarks.setText(compCasesUser.getRemarks());
        Prior.setText(compCasesUser.getPriority());
        ComplinTAT.setText(compCasesUser.getCompleted_in_tat());

        buttonDownload.setOnClickListener(view -> {
            if(null == compCasesUser.getFile_url() || compCasesUser.getFile_url().trim().equals("")){
                Snackbar.make(v,"File not available!", 2000).show();
                return;
            }
            String fileUrl = compCasesUser.getFile_url();
            String fileName = compCasesUser.getFile_url().substring(compCasesUser.getFile_url().lastIndexOf("_") + 1);
            DownloadTask downloadTask = new DownloadTask(fileUrl, fileName);
            if(!MainActivity.isIsConnected()){
                Snackbar.make(v, "No internet connection!", 2000).show();
                return;
            }
            downloadTask.execute();
        });
        viewButton.setOnClickListener(view -> {
            if(null == compCasesUser.getFile_url() || compCasesUser.getFile_url().trim().equals("")){
                Snackbar.make(v,"File not available!", 2000).show();
                return;
            }
            String fileName = compCasesUser.getFile_url().substring(compCasesUser.getFile_url().lastIndexOf("_") + 1);
            File file = new File(Environment.getExternalStorageDirectory() + "/Verify360/" + fileName);
            if (file.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.fromFile(file);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenintent.setDataAndType(uri, "*/*");
                try {
                    startActivity(pdfOpenintent);
                }
                catch (ActivityNotFoundException e) {
                    Snackbar.make(getView(), "PDF viewer not found!", 2000).show();
                }
            } else {
                Snackbar.make(v, "Please download file first!", 2000).show();
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Completed Case Details");
    }

    private class DownloadTask extends AsyncTask<Void, Void, Void> {
        private String url;
        private String fileName;

        public DownloadTask(String url, String fileName) {
            this.url = url;
            this.fileName = fileName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Snackbar.make(v, "Starting Download!", 2000).show();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            downloadFile(url, fileName);
            return null;
        }

        private void downloadFile(String fileUrl, String fileName) {
            FileDownloader downloadService = DownloadService.createService(FileDownloader.class);
            Call<ResponseBody> call = downloadService.downloadFile(fileUrl);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        writeFileToStorage(response.body(), fileName);
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getActivity(), "Error in downlaoding file", Toast.LENGTH_SHORT).show());
                }
            });

        }

        private void writeFileToStorage(ResponseBody response, String fileName) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory() + "/Verify360/");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    byte[] buffer = new byte[4096];

                    long filesize = response.contentLength();
                    long fileSizeDownloaded = 0;
                    inputStream = response.byteStream();
                    File filename = new File(dir, fileName);
                    if (filename.exists()) {
                        Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getActivity(), "File already present", Toast.LENGTH_SHORT).show());
                        return;
                    }
                    outputStream = new FileOutputStream(filename);
                    while (true) {
                        int read = inputStream.read(buffer);
                        if (read == -1) {
                            break;
                        }
                        outputStream.write(buffer, 0, read);
                        Log.d("Status", "file download: " + fileSizeDownloaded + " of " + filesize);
                    }
                    outputStream.flush();
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Snackbar.make(v, "Download Completed!", 2000).show());


                } catch (FileNotFoundException e) {
                    Log.d("Status", "" + e.getMessage());
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Snackbar.make(v, "File not found!", 2000).show());
                } catch (IOException e) {
                    Log.d("Status", "" + e.getMessage());
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Snackbar.make(v, "Error in file download!", 2000).show());
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } catch (IOException e) {
                Log.d("Status", "" + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
        }
    }
}
