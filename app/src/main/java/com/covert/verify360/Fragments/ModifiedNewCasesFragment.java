package com.covert.verify360.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.covert.verify360.AdapterClasses.ModifiedNewCasesRecyclerAdapter;
import com.covert.verify360.BeanClasses.AssignedCasesResponse;
import com.covert.verify360.BeanClasses.NewCasesBean;
import com.covert.verify360.Helpers.ProcessAlertDialogue;
import com.covert.verify360.MainActivity;
import com.covert.verify360.R;

import java.util.ArrayList;
import java.util.List;

import Services.FactoryService;
import Services.IAssignedCases;
import database_utils.DatabaseHandler;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ModifiedNewCasesFragment extends Fragment implements ModifiedNewCasesRecyclerAdapter.CaseSelected {

    private ArrayList<NewCasesBean> list;
    private SharedPreferences sharedPreferences;
    private DatabaseHandler db_instance;
    private RecyclerView newCasesList;
    private ProcessAlertDialogue processAlertDialogue;
    ModifiedNewCasesRecyclerAdapter modifiedNewCasesRecyclerAdapter;
    View NoData;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        list = new ArrayList<>();
        db_instance = DatabaseHandler.getDatabaseInstance(getActivity());
        processAlertDialogue=new ProcessAlertDialogue(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.modified_newcases_fragment,container,false);
        NoData= v.findViewById(R.id.no_data);
        sharedPreferences = getActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("New Cases");
        newCasesList = v.findViewById(R.id.newcases_list);
        newCasesList.setLayoutManager(new LinearLayoutManager(getActivity()));
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

        db_instance.newCasesDao().getCasesFromDB().observe(this, (List<NewCasesBean> newcases) -> {
            list = new ArrayList<>(newcases);
            modifiedNewCasesRecyclerAdapter = new ModifiedNewCasesRecyclerAdapter(getActivity(), list,this);
            newCasesList.setAdapter(modifiedNewCasesRecyclerAdapter);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("New Cases");
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
                            AsyncTask.execute(() -> db_instance.newCasesDao().insertCases(assignedCasesResponse.getNewCasesList()));
                        } else {
                            Snackbar.make(getView(), "Data not found!",2000).show();
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        processAlertDialogue.CloseDialogue();
                        Snackbar.make(getView(), "Server error!",2000).show();
                    }

                    @Override
                    public void onComplete() {
                        processAlertDialogue.CloseDialogue();
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
    }


    @Override
    public void selectedNewCase(int position, NewCasesBean newCasesBean) {
        NewCasesFragment fragment = new NewCasesFragment();
        FragmentManager fragmentManager = new MainActivity().getFmanager();
        Bundle bundle= new Bundle();
        bundle.putSerializable("xyz", newCasesBean);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.content_layout, fragment).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }
}
