package com.covert.verify360.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.covert.verify360.BeanClasses.CancelledCasesResponse;
import com.covert.verify360.BeanClasses.CanclledCasesUser;
import com.covert.verify360.BeanClasses.Dashboard;
import com.covert.verify360.BeanClasses.DashboardBean;
import com.covert.verify360.Helpers.ProcessAlertDialogue;
import com.covert.verify360.MainActivity;
import com.covert.verify360.R;

import java.util.ArrayList;
import java.util.List;

import Services.CanclledCasesService;
import Services.DashboardService;
import Services.FactoryService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 3/27/2018.
 */

public class FragmentDashboard extends Fragment {
    private CardView newcasesCardView,pendingCasesCardView,completedCasesCardView,cancelledCasesCardView;
    private List<DashboardBean> dashboardItems;
    //ProgressBar progressBar;
    ProcessAlertDialogue processAlertDialogue;
    TextView OpenCases, AcceptedCases, RejectedCases, CompleteCases, CancelCases, FECompleted;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Dashboard");
        //progressBar= v.findViewById(R.id.progress_bar);
        processAlertDialogue= new ProcessAlertDialogue(getActivity());
        newcasesCardView = v.findViewById(R.id.newcases_layout);
        pendingCasesCardView = v.findViewById(R.id.pendingCases_layout);
        completedCasesCardView = v.findViewById(R.id.completedCases_layout);
        cancelledCasesCardView = v.findViewById(R.id.cancelledCases_layout);
        OpenCases= v.findViewById(R.id.tv_opencases);
        AcceptedCases= v.findViewById(R.id.tv_acceptcases);
        RejectedCases= v.findViewById(R.id.tv_rejectedcases);
        CancelCases= v.findViewById(R.id.tv_cancelcases);
        FECompleted= v.findViewById(R.id.tv_fecompletecases);
        CompleteCases= v.findViewById(R.id.tv_completedcases);

        newcasesCardView.setOnClickListener(v1 -> getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment,new ModifiedNewCasesFragment(),"new cases")
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit());

        pendingCasesCardView.setOnClickListener(vl ->
                getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment,new PendingCasesFragment(),"Pending cases")
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit());

        completedCasesCardView.setOnClickListener(vl ->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment,new CompletedCasesFragment(),"Pending cases")
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit());


        cancelledCasesCardView.setOnClickListener(vl ->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment,new CancelledCasesFragment(),"Pending cases")
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit());
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String empid = null;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        if (sharedPreferences != null && sharedPreferences.contains("EMP_ID")) {
            empid = sharedPreferences.getString("EMP_ID", "");
        }
        setData(empid);
    }

    private void setData(String empid) {
        if(!MainActivity.isIsConnected()){
            Snackbar.make(getView(), "Please connect to internet!",2000).show();
            return;
        }
        if (dashboardItems != null) {
            dashboardItems.clear();
        }
        processAlertDialogue.ShowDialogue();
        //progressBar.setVisibility(View.VISIBLE);
        DashboardService iAssignedCases = FactoryService.createService(DashboardService.class);
        Call<Dashboard> call = null;
        call = iAssignedCases.getDashboardData(empid);


        call.enqueue(new Callback<Dashboard>() {
            @Override
            public void onResponse(Call<Dashboard> call, Response<Dashboard> response) {

                if(response.code() == 200 && response.body().getError().equals("false")){
                    //progressBar.setVisibility(View.GONE);
                    Dashboard oneDash= response.body();
                    dashboardItems= new ArrayList<>();
                    dashboardItems=oneDash.getNewCasesList();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SetDashboardData();
                        }
                    });
                }else{
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Dashboard> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                processAlertDialogue.CloseDialogue();
                Toast.makeText(getActivity(), "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetDashboardData(){
        if(dashboardItems!=null){
            for(DashboardBean oneItem: dashboardItems){
                if(oneItem.getStatus_name().equals("Open")){
                    OpenCases.setText(String.valueOf(oneItem.getNo_of_Cases()));
                }else if(oneItem.getStatus_name().equals("Accept")){
                    AcceptedCases.setText(String.valueOf(oneItem.getNo_of_Cases()));
                }else if(oneItem.getStatus_name().equals("Reject")){
                    RejectedCases.setText(String.valueOf(oneItem.getNo_of_Cases()));
                }else if(oneItem.getStatus_name().equals("Cancelled")){
                    CancelCases .setText(String.valueOf(oneItem.getNo_of_Cases()));
                }else if(oneItem.getStatus_name().equals("FE-Complete")){
                    FECompleted .setText(String.valueOf(oneItem.getNo_of_Cases()));
                }else if(oneItem.getStatus_name().equals("Completed")){
                    CompleteCases .setText(String.valueOf(oneItem.getNo_of_Cases()));
                }
            }
        }
        processAlertDialogue.CloseDialogue();
    }
}
