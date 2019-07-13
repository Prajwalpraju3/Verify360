package com.covert.verify360.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.covert.verify360.AdapterClasses.CompletedCasesAdapter;
import com.covert.verify360.BeanClasses.CompCasesUser;
import com.covert.verify360.BeanClasses.CompletedCasesResponse;
import com.covert.verify360.BeanClasses.NewCasesBean;
import com.covert.verify360.Helpers.InfoAlertDialogue;
import com.covert.verify360.Helpers.ProcessAlertDialogue;
import com.covert.verify360.MainActivity;
import com.covert.verify360.R;

import java.util.ArrayList;
import java.util.List;

import Services.CompletedCaseDetails;
import Services.FactoryService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompletedCasesFragment extends Fragment implements CompletedCasesAdapter.CaseSelected{
    SharedPreferences sharedPreferences;
    View v;
    View NoData;
    private ProcessAlertDialogue processAlertDialogue;
    private ArrayList<CompCasesUser> list;
    RecyclerView completedCasesList;
    CompletedCasesAdapter completedCasesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        list = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.modified_newcases_fragment, container, false);
        NoData= v.findViewById(R.id.no_data);
        sharedPreferences = getActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Completed Cases");
        processAlertDialogue=new ProcessAlertDialogue(getActivity());
        completedCasesList = v.findViewById(R.id.newcases_list);
        completedCasesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String empid = null;
        if (sharedPreferences != null && sharedPreferences.contains("EMP_ID")) {
            empid = sharedPreferences.getString("EMP_ID", "");
        }
        setData(empid);
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
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Completed Cases");
    }


    private void setData(String empid) {
        if(!MainActivity.isIsConnected()){
            Snackbar.make(getView(), "Please connect to internet!",2000).show();
            return;
        }
        if (list != null) {
            list.clear();
        }
        processAlertDialogue.ShowDialogue();
        CompletedCaseDetails iAssignedCases = FactoryService.createService(CompletedCaseDetails.class);
        Call<CompletedCasesResponse> call = null;
        call = iAssignedCases.getCompletedCases(empid);
        call.enqueue(new Callback<CompletedCasesResponse>() {
            @Override
            public void onResponse(Call<CompletedCasesResponse> call, Response<CompletedCasesResponse> response) {
                if(response.code() == 200 && response.body().getError().equals("false")){
                    list = new ArrayList<>(response.body().getUser());
                   setAdapter(list);
                }else{
                    new InfoAlertDialogue(getActivity()).ShowDialogue(getString(R.string.information),"No data available at moment!");
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(list!=null && list.size()==0){
                            NoData.setVisibility(View.VISIBLE);
                        }else{
                            NoData.setVisibility(View.GONE);
                        }
                    }
                });
                processAlertDialogue.CloseDialogue();
            }

            @Override
            public void onFailure(Call<CompletedCasesResponse> call, Throwable t) {
                processAlertDialogue.CloseDialogue();
                new InfoAlertDialogue(getActivity()).ShowDialogue(getString(R.string.information),getString(R.string.unable_to_process));
            }
        });


    }

    @Override
    public void selectedNewCase(int position, CompCasesUser newCasesBean) {
        CompletedCaseDetailsFragment fragment = new CompletedCaseDetailsFragment();
        FragmentManager fragmentManager = new MainActivity().getFmanager();
        Bundle bundle= new Bundle();
        bundle.putSerializable("xyz", newCasesBean);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.content_layout, fragment).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

    }

    private void setAdapter(ArrayList<CompCasesUser> compCasesUsers){
        completedCasesAdapter = new CompletedCasesAdapter(getActivity(), compCasesUsers,this);
        completedCasesList.setAdapter(completedCasesAdapter);
    }
}
