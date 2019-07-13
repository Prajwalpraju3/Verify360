package com.covert.verify360.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.covert.verify360.AdapterClasses.CancelledCasesAdapter;
import com.covert.verify360.AdapterClasses.CompletedCasesAdapter;
import com.covert.verify360.BeanClasses.CancelledCasesResponse;
import com.covert.verify360.BeanClasses.CanclledCasesUser;
import com.covert.verify360.BeanClasses.CompCasesUser;
import com.covert.verify360.BeanClasses.CompletedCasesResponse;
import com.covert.verify360.Helpers.ProcessAlertDialogue;
import com.covert.verify360.MainActivity;
import com.covert.verify360.R;

import java.util.ArrayList;
import java.util.List;

import Services.CanclledCasesService;
import Services.CompletedCaseDetails;
import Services.FactoryService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CancelledCasesFragment extends Fragment implements CancelledCasesAdapter.CaseSelected {

    SharedPreferences sharedPreferences;
    View v;
    private ArrayList<CanclledCasesUser> list;
    RecyclerView cancelledList;
    CancelledCasesAdapter cancelledCasesAdapter;
    private ProcessAlertDialogue processAlertDialogue;
    View NoData;

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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cancelled Cases");
        processAlertDialogue= new ProcessAlertDialogue(getActivity());
        cancelledList = v.findViewById(R.id.newcases_list);
        cancelledList.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cancelled Cases");
    }

    private void setData(String empid) {
        if(!MainActivity.isIsConnected()){
            Snackbar.make(v, "Please connect to internet!",2000).show();
            return;
        }
        if (list != null) {
            list.clear();
        }
        processAlertDialogue.ShowDialogue();
        CanclledCasesService iAssignedCases = FactoryService.createService(CanclledCasesService.class);
        Call<CancelledCasesResponse> call = null;
        call = iAssignedCases.getCancelledCases(empid);
        call.enqueue(new Callback<CancelledCasesResponse>() {
            @Override
            public void onResponse(Call<CancelledCasesResponse> call, Response<CancelledCasesResponse> response) {

                if(response.code() == 200 && response.body().getError().equals("false")){
                    list = new ArrayList<CanclledCasesUser>(response.body().getUser());
                    setAdapter(list);
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
                }else{
                    processAlertDialogue.CloseDialogue();
                    Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<CancelledCasesResponse> call, Throwable t) {
                processAlertDialogue.CloseDialogue();
                Toast.makeText(getActivity(), "Server Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void setAdapter(ArrayList<CanclledCasesUser> compCasesUsers){
        cancelledCasesAdapter = new CancelledCasesAdapter(getActivity(), compCasesUsers,this);
        cancelledList.setAdapter(cancelledCasesAdapter);
    }

    @Override
    public void selectedNewCase(int position, CanclledCasesUser canclledCasesUser) {
        CancelledCasesDetailsFragment fragment = new CancelledCasesDetailsFragment();
        FragmentManager fragmentManager = new MainActivity().getFmanager();
        Bundle bundle= new Bundle();
        bundle.putSerializable("xyz", canclledCasesUser);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.content_layout, fragment).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
}
