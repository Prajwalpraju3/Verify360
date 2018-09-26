package com.covert.verify360.Fragments;

import android.app.AlertDialog;
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
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.covert.verify360.BeanClasses.AssignedCasesResponse;
import com.covert.verify360.BeanClasses.NewCasesBean;
import com.covert.verify360.GlobalConstants;
import com.covert.verify360.MainActivity;
import com.covert.verify360.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Services.AcceptService;
import Services.DownloadService;
import Services.FactoryService;
import Services.FileDownloader;
import Services.IAssignedCases;
import Services.RejectService;
import database_utils.DatabaseHandler;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewCasesFragment extends Fragment {
    private ViewPager viewPager;
    private NewCasesAdapter newCasesAdapter;
    private List<NewCasesBean> list;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private DatabaseHandler db_instance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        list = new ArrayList<>();
        db_instance = DatabaseHandler.getDatabaseInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_cases, container, false);
        sharedPreferences = getActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        viewPager = v.findViewById(R.id.viewPager);
        viewPager.setVisibility(View.VISIBLE);

        String empid = null;
        if (sharedPreferences != null && sharedPreferences.contains("EMP_ID")) {
            empid = sharedPreferences.getString("EMP_ID", "");
        }
        setData(empid);

        db_instance.newCasesDao().getCasesFromDB().observe(this, (List<NewCasesBean> newcases) -> {
            newCasesAdapter = new NewCasesAdapter(getActivity(), newcases);
            viewPager.setAdapter(newCasesAdapter);
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setData(String empid) {
        if(!MainActivity.isIsConnected()){
            Toast.makeText(getActivity(), "Please connect to internet.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (list != null) {
            list.clear();
        }
        progressBar.setVisibility(View.VISIBLE);
        IAssignedCases iAssignedCases = FactoryService.createService(IAssignedCases.class);
        iAssignedCases.getCasesFromNetwork(empid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AssignedCasesResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final AssignedCasesResponse assignedCasesResponse) {
                        if (assignedCasesResponse.getError().equals("false")) {
                            progressBar.setVisibility(View.GONE);
                            AsyncTask.execute(() -> db_instance.newCasesDao().insertCases(assignedCasesResponse.getNewCasesList()));
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Server Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                    }

                });
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
//        AsyncTask.execute(()->db_instance.newCasesDao().deleteAllCases());
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
            Toast.makeText(getActivity(), "Downlaoding file", Toast.LENGTH_SHORT).show();
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
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getActivity(), "File Downloaded", Toast.LENGTH_SHORT).show());


                } catch (FileNotFoundException e) {
                    Log.d("Status", "" + e.getMessage());
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getActivity(), "Cannot find file", Toast.LENGTH_SHORT).show());


                } catch (IOException e) {
                    Log.d("Status", "" + e.getMessage());
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getActivity(), "Error downloading file", Toast.LENGTH_SHORT).show());

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

    private class NewCasesAdapter extends PagerAdapter {

        private Context context;
        private List<NewCasesBean> listNewCases;

        public NewCasesAdapter(Context context, List<NewCasesBean> listNewCases) {
            this.context = context;
            this.listNewCases = listNewCases;
        }

        @Override
        public int getCount() {
            return listNewCases.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.new_cases_row, container, false);
            TextView case_id, activity_id, client, pre_post, product_type, activity_type, company_name,
                    applicant_name, father_name, mobile_number, email, address, est_complete_date, remarks;
            Button buttonDownload, buttonAccept, buttonReject, viewButton;

            case_id = viewGroup.findViewById(R.id.case_id);
            activity_id = viewGroup.findViewById(R.id.activity_id);
            client = viewGroup.findViewById(R.id.client);
            pre_post = viewGroup.findViewById(R.id.pre_post);
            product_type = viewGroup.findViewById(R.id.product_type);
            activity_type = viewGroup.findViewById(R.id.activity_type);
            company_name = viewGroup.findViewById(R.id.company_name);
            applicant_name = viewGroup.findViewById(R.id.applicant_name);
            father_name = viewGroup.findViewById(R.id.father_name);
            mobile_number = viewGroup.findViewById(R.id.mobile_number);
            email = viewGroup.findViewById(R.id.email);
            address = viewGroup.findViewById(R.id.address);
            est_complete_date = viewGroup.findViewById(R.id.est_complete_date);
            remarks = viewGroup.findViewById(R.id.remarks);

            buttonAccept = viewGroup.findViewById(R.id.acceptButton);
            buttonReject = viewGroup.findViewById(R.id.rejectButton);
            buttonDownload = viewGroup.findViewById(R.id.downloadButton);
            viewButton = viewGroup.findViewById(R.id.viewButton);

            case_id.setText("Case Id :" + " " + listNewCases.get(position).getCase_id());
            activity_id.setText("Activity Id :" + " " + listNewCases.get(position).getCase_detail_id());
            client.setText("Client :" + " " + listNewCases.get(position).getClient_name());
            pre_post.setText("Pre_Post :" + " " + listNewCases.get(position).getPre_post());
            product_type.setText("Product type :" + " " + listNewCases.get(position).getProduct_type());
            activity_type.setText("Activity type :" + " " + listNewCases.get(position).getActivity_type());
            company_name.setText("Company :" + " " + listNewCases.get(position).getCompany_name());
            applicant_name.setText("Full Name :" + " " + listNewCases.get(position).getApplicant_first_name()
                    + " " + listNewCases.get(position).getApplicant_last_name());
            father_name.setText("Father Name :" + " " + listNewCases.get(position).getFather_name());
            mobile_number.setText("Mobile :" + " " + listNewCases.get(position).getPrimary_contact());
            email.setText("Email :" + " " + listNewCases.get(position).getEmail());
            address.setText("Address :" + "\n" + listNewCases.get(position).getDoor_number() + ", "
                    + listNewCases.get(position).getStreet_address() + ", "
                    + listNewCases.get(position).getLandmark() + "\n"
                    + listNewCases.get(position).getLocation() + ", "
                    + listNewCases.get(position).getPincode() + "\n" + listNewCases.get(position).getRegion_name());
            est_complete_date.setText("est-compl-date :" + " " + listNewCases.get(position).getEst_completed_date());
            remarks.setText("Remarks :" + " " + listNewCases.get(position).getRemarks());


            buttonReject.setOnClickListener(v -> {
                if(!MainActivity.isIsConnected()){
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;

                }
                String working_by = null;
                if (sharedPreferences != null && sharedPreferences.contains("EMP_ID")) {
                    working_by = sharedPreferences.getString("EMP_ID", "");
                }
                RejectService rejectService = FactoryService.createService(RejectService.class);


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.layout_alert_dialog, null);
                final TextInputEditText reasonEtext = view.findViewById(R.id.reasonToReject);
                String finalWorking_by = working_by;
                builder.setView(view)
                        .setTitle("Verify360")
                        .setPositiveButton("Reject", (dialog, which) -> {
                            Call<ResponseBody> call = rejectService.reject(finalWorking_by, listNewCases.get(position).getCase_detail_id()
                                    , listNewCases.get(position).getCase_id(), reasonEtext.getText().toString()
                                    , Integer.toString(GlobalConstants.REJECT));
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, "Case Rejected", Toast.LENGTH_SHORT).show();
                                        AsyncTask.execute(() -> db_instance.newCasesDao().deleteAllCases());
                                        setData(finalWorking_by);
                                        dialog.cancel();
                                    } else {
                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                        }).setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            });
            buttonAccept.setOnClickListener(v -> {
                if(!MainActivity.isIsConnected()){
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;

                }
                String working_by = null;
                if (sharedPreferences != null && sharedPreferences.contains("EMP_ID")) {
                    working_by = sharedPreferences.getString("EMP_ID", "");
                }
                AcceptService acceptService = FactoryService.createService(AcceptService.class);
                Call<ResponseBody> call = acceptService.accept(working_by, listNewCases.get(position).getCase_detail_id()
                        , listNewCases.get(position).getCase_id(), Integer.toString(GlobalConstants.ACCEPT));
                String finalWorking_by = working_by;
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Case Accepted successfully", Toast.LENGTH_SHORT).show();
                            AsyncTask.execute(() -> db_instance.newCasesDao().deleteAllCases());
                            setData(finalWorking_by);
                        } else {
                            Toast.makeText(context, "Error ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
            buttonDownload.setOnClickListener(v -> {
                String fileUrl = listNewCases.get(position).getFile_url();
                String fileName = listNewCases.get(position).getFile_url().substring(listNewCases.get(position).getFile_url().lastIndexOf("_") + 1);
                DownloadTask downloadTask = new DownloadTask(fileUrl, fileName);
                if(!MainActivity.isIsConnected()){
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;

                }
                downloadTask.execute();
            });
            viewButton.setOnClickListener(v -> {
                String fileName = listNewCases.get(position).getFile_url().substring(listNewCases.get(position).getFile_url().lastIndexOf("_") + 1);
                File file = new File(Environment.getExternalStorageDirectory() + "/Verify360/" + fileName);
                if (file.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String absPath = file.getAbsolutePath();
                    Uri uri = Uri.parse("content://" + "com.covert.verify360" + "/" + absPath);
                    intent.setDataAndType(uri, "application/pdf");
//                    intent.setDataAndType(FileProvider.getUriForFile(context,"com.covert.verify360.fileprovider",file), "application/pdf");
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Intent choser = Intent.createChooser(intent, "Open File");
                    try {
                        context.startActivity(choser);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "Please download pdf viewer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(viewGroup);
            return viewGroup;
        }
    }
}
