package com.covert.verify360.Fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.covert.verify360.Helpers.InfoAlertDialogue;
import com.covert.verify360.Helpers.ProcessAlertDialogue;
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
    private List<NewCasesBean> list;
    private SharedPreferences sharedPreferences;
    private DatabaseHandler db_instance;
    private ProcessAlertDialogue processAlertDialogue;
    View v;


    NewCasesBean newCasesBean;
    TextView case_id, activity_id, client, pre_post, product_type, activity_type, company_name,
            applicant_name, father_name, mobile_number, email, address, est_complete_date, remarks;
    Button buttonDownload, buttonAccept, buttonReject, viewButton;


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
        v = inflater.inflate(R.layout.new_cases_row, container, false);
        sharedPreferences = getActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Case Details");
        try {
            Bundle bundle= getArguments();
            newCasesBean = (NewCasesBean) bundle.getSerializable("xyz");
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
        remarks = v.findViewById(R.id.remarks);

        buttonAccept = v.findViewById(R.id.acceptButton);
        buttonReject = v.findViewById(R.id.rejectButton);
        buttonDownload = v.findViewById(R.id.downloadButton);
        viewButton = v.findViewById(R.id.viewButton);

        case_id.setText(newCasesBean.getCase_id());
        activity_id.setText(newCasesBean.getCase_detail_id());
        client.setText(newCasesBean.getClient_name());
        pre_post.setText(newCasesBean.getPre_post());
        product_type.setText(newCasesBean.getProduct_type());
        activity_type.setText(newCasesBean.getActivity_type());
        company_name.setText(newCasesBean.getCompany_name());
        applicant_name.setText(newCasesBean.getApplicant_first_name()
                + " " + newCasesBean.getApplicant_last_name());
        father_name.setText(newCasesBean.getFather_name());
        mobile_number.setText(newCasesBean.getPrimary_contact());
        email.setText(newCasesBean.getEmail());
        address.setText(newCasesBean.getDoor_number() + ", "
                + newCasesBean.getStreet_address() + ", "
                + newCasesBean.getLandmark() + "\n"
                + newCasesBean.getLocation() + ", "
                + newCasesBean.getPincode() + "\n" + newCasesBean.getRegion_name());
        est_complete_date.setText(newCasesBean.getEst_completed_date());
        remarks.setText(newCasesBean.getRemarks());


        buttonReject.setOnClickListener(view -> {
            if(!MainActivity.isIsConnected()){
                Snackbar.make(getView(), "No internet connection!", 2000).show();
                return;

            }
            String working_by = null;
            if (sharedPreferences != null && sharedPreferences.contains("EMP_ID")) {
                working_by = sharedPreferences.getString("EMP_ID", "");
            }
            RejectService rejectService = FactoryService.createService(RejectService.class);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View viewAlert = LayoutInflater.from(getActivity()).inflate(R.layout.layout_alert_dialog, null);
            final TextInputEditText reasonEtext = viewAlert.findViewById(R.id.reasonToReject);
            String finalWorking_by = working_by;
            builder.setView(viewAlert)
                    .setTitle("Verify360")
                    .setPositiveButton("Reject", (dialog, which) -> {
                        Call<ResponseBody> call = rejectService.reject(finalWorking_by, newCasesBean.getCase_detail_id()
                                , newCasesBean.getCase_id(), reasonEtext.getText().toString()
                                , Integer.toString(GlobalConstants.REJECT));
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    AsyncTask.execute(() -> db_instance.newCasesDao().deleteAllCases());
                                    setData(finalWorking_by);
                                    dialog.cancel();
                                    new InfoAlertDialogue(getActivity()).ShowDialogue(getString(R.string.information), "Case is rejected.");
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.container_fragment, new  ModifiedNewCasesFragment()).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                                } else {
                                    dialog.cancel();
                                    new InfoAlertDialogue(getActivity()).ShowDialogue(getString(R.string.information), getString(R.string.unable_to_process));
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Snackbar.make(getView(), "Server error!", 2000).show();
                                dialog.cancel();
                            }
                        });
                    }).setNegativeButton("Cancel", (dialog, which) -> {
                dialog.cancel();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        buttonAccept.setOnClickListener(view -> {
            if(!MainActivity.isIsConnected()){
                Snackbar.make(getView(), "No internet connection!", 2000).show();
                return;

            }
            android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(getActivity(),  R.style.AlertDialogTheme);
            dialog.setCancelable(false);
            dialog.setTitle("Confirmation");
            dialog.setMessage("Are you sure about accepting case?");
            dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    String working_by = null;
                    if (sharedPreferences != null && sharedPreferences.contains("EMP_ID")) {
                        working_by = sharedPreferences.getString("EMP_ID", "");
                    }
                    AcceptService acceptService = FactoryService.createService(AcceptService.class);
                    Call<ResponseBody> call = acceptService.accept(working_by, newCasesBean.getCase_detail_id()
                            , newCasesBean.getCase_id(), Integer.toString(GlobalConstants.ACCEPT));
                    String finalWorking_by = working_by;
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                AsyncTask.execute(() -> db_instance.newCasesDao().deleteAllCases());
                                setData(finalWorking_by);
                                new InfoAlertDialogue(getActivity()).ShowDialogue(getString(R.string.information), "Case Accepted.");
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.container_fragment, new  ModifiedNewCasesFragment()).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                            } else {
                                new InfoAlertDialogue(getActivity()).ShowDialogue(getString(R.string.information), getString(R.string.unable_to_process));
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Snackbar.make(getView(), "Server error!", 2000).show();
                        }
                    });
                }
            });
            final android.support.v7.app.AlertDialog alert = dialog.create();
            alert.show();
        });

        buttonDownload.setOnClickListener(view -> {
            if(null == newCasesBean.getFile_url() || newCasesBean.getFile_url().trim().equals("")){
                Snackbar.make(getView(),"File not available!", 2000).show();
                return;
            }
            String fileUrl = newCasesBean.getFile_url();
            String fileName = newCasesBean.getFile_url().substring(newCasesBean.getFile_url().lastIndexOf("_") + 1);
            DownloadTask downloadTask = new DownloadTask(fileUrl, fileName);
            if(!MainActivity.isIsConnected()){
                Snackbar.make(getView(), "No internet connection!", 2000).show();
                return;

            }
            downloadTask.execute();
        });
        viewButton.setOnClickListener(view -> {
            if(null == newCasesBean.getFile_url() || newCasesBean.getFile_url().trim().equals("")){
                Snackbar.make(getView(),"File not available!", 2000).show();
                return;
            }
            String fileName = newCasesBean.getFile_url().substring(newCasesBean.getFile_url().lastIndexOf("_") + 1);
            File file = new File(Environment.getExternalStorageDirectory() + "/Verify360/" + fileName);
            if (file.exists()) {
                Uri uri = Uri.fromFile(file);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setDataAndType(uri, "application/pdf");
                try {
                    startActivityForResult(pdfOpenintent,0);
                }
                catch (ActivityNotFoundException e) {
                    Snackbar.make(getView(), "PDF viewer not found!", 2000).show();
                }
            } else {
                Snackbar.make(getView(), "Please download file first!", 2000).show();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("New Case Details");
    }

    private void setData(String empid) {
        if(!MainActivity.isIsConnected()){
            Snackbar.make(getView(), "No internet connection!", 2000).show();
            return;
        }
        if (list != null) {
            list.clear();
        }
        //progressBar.setVisibility(View.VISIBLE);
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
                            //progressBar.setVisibility(View.GONE);
                            AsyncTask.execute(() -> db_instance.newCasesDao().insertCases(assignedCasesResponse.getNewCasesList()));
                        } else {
                            //progressBar.setVisibility(View.GONE);
                            Snackbar.make(getView(), "Data not available!", 2000).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //progressBar.setVisibility(View.GONE);
                        Snackbar.make(getView(), "Server Error" + e.getMessage(), 2000).show();
                    }

                    @Override
                    public void onComplete() {
                        //progressBar.setVisibility(View.GONE);
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
            Snackbar.make(getView(), "starting Download!", 2000).show();
            //progressBar.setVisibility(View.VISIBLE);
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
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Snackbar.make(getView(), "Download error!", 2000).show());
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
                        Objects.requireNonNull(getActivity()).runOnUiThread(() -> Snackbar.make(getView(), "Already downloaded!", 2000).show());
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
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Snackbar.make(getView(), "Download complete!", 2000).show());


                } catch (FileNotFoundException e) {
                    Log.d("Status", "" + e.getMessage());
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Snackbar.make(getView(), "Cannot download file!", 2000).show());


                } catch (IOException e) {
                    Log.d("Status", "" + e.getMessage());
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Snackbar.make(getView(), "File download error!", 2000).show());

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
            //progressBar.setVisibility(View.GONE);
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

            container.addView(viewGroup);
            return viewGroup;
        }
    }

}
