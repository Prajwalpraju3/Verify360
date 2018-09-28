package com.covert.verify360.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.covert.verify360.BeanClasses.PendingCasesBean;
import com.covert.verify360.BeanClasses.PendingCasesResponse;
import com.covert.verify360.CaseBusinessVerificationActivity;
import com.covert.verify360.CasePaySlipVerificationActivity;
import com.covert.verify360.CaseResidentVerificationActivity;
import com.covert.verify360.GlobalConstants;
import com.covert.verify360.MainActivity;
import com.covert.verify360.R;

import java.util.ArrayList;
import java.util.List;

import Services.FactoryService;
import Services.IPendingCases;
import database_utils.DatabaseHandler;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class PendingCasesFragment extends Fragment {
    String working_by;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private List<PendingCasesBean> pendingCasesBeanList;
    private ProgressBar progressBar;
    private DatabaseHandler db_instance;
    private PendingCasesAdapter pendingCasesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        pendingCasesBeanList = new ArrayList<>();
        db_instance = DatabaseHandler.getDatabaseInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pending_cases, container, false);
        sharedPreferences = getActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        recyclerView = v.findViewById(R.id.pendingCasesRview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        working_by = null;
        if (sharedPreferences != null && sharedPreferences.contains("EMP_ID")) {
            working_by = sharedPreferences.getString("EMP_ID", "");
        }
        setPendingData(working_by);

        db_instance.pendingCasesDao().getCasesFromDB().observe(getActivity(), pendingcases -> {
            pendingCasesBeanList = pendingcases;
            pendingCasesAdapter = new PendingCasesAdapter(pendingCasesBeanList,
                    PendingCasesFragment.this.getActivity());
            recyclerView.setAdapter(pendingCasesAdapter);
        });
        return v;
    }

    private void setPendingData(String working_by) {
        if (!MainActivity.isIsConnected()) {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;

        }
        if (pendingCasesBeanList != null) {
            pendingCasesBeanList.clear();
        }
        progressBar.setVisibility(View.VISIBLE);
        IPendingCases iPendingCases = FactoryService.createService(IPendingCases.class);
        iPendingCases.getCasesFromNetwork(working_by)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PendingCasesResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final PendingCasesResponse pendingCasesResponse) {
                        if (pendingCasesResponse.getError().equals("false")) {
                            progressBar.setVisibility(View.GONE);
                            AsyncTask.execute(() -> db_instance.pendingCasesDao().insertCases(pendingCasesResponse.getNewCasesList()));
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

    private class PendingCasesAdapter extends RecyclerView.Adapter<PendingCasesAdapter.ViewHolder> {
        private List<PendingCasesBean> listPendingCases;
        private Context context;

        public PendingCasesAdapter(List<PendingCasesBean> listPendingCases, Context context) {
            this.listPendingCases = listPendingCases;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewHolder view = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.pending_cases_row, parent, false));
            return view;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.case_id.setText("Case ID : " + listPendingCases.get(position).getCase_id());
            holder.activity_id.setText("Activity ID : " + listPendingCases.get(position).getCase_detail_id());
            holder.activity_type.setText("Activity type : " + listPendingCases.get(position).getActivity_type());
            holder.name.setText("Name : " + listPendingCases.get(position).getApplicant_first_name() + " "
                    + listPendingCases.get(position).getApplicant_last_name());
            holder.mobile.setText("Mobile : " + listPendingCases.get(position).getPrimary_contact());
            holder.address.setText("Address : \n" + listPendingCases.get(position).getDoor_number() + ", "
                    + listPendingCases.get(position).getStreet_address() + ", "
                    + listPendingCases.get(position).getLandmark() + "\n"
                    + listPendingCases.get(position).getLocation() + ", "
                    + listPendingCases.get(position).getPincode() + "\n" + listPendingCases.get(position).getRegion_name());

            holder.buttonView.setOnClickListener(v -> {
                final String activityType = listPendingCases.get(position).getActivity_type();
                if (activityType.toLowerCase().equals(GlobalConstants.RESIDENT_VERIFICATION)) {
                    Intent intent = new Intent(getActivity(), CaseResidentVerificationActivity.class);
                    intent.putExtra("CASE_ID", listPendingCases.get(position).getCase_id());
                    intent.putExtra("case_detailed_id", listPendingCases.get(position).getCase_detail_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (activityType.toLowerCase().equals(GlobalConstants.PAY_SLIP_VERIFICATION)) {
                    Intent intent = new Intent(getActivity(), CasePaySlipVerificationActivity.class);
                    intent.putExtra("CASE_ID", listPendingCases.get(position).getCase_id());
                    intent.putExtra("case_detailed_id", listPendingCases.get(position).getCase_detail_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (activityType.toLowerCase().equals(GlobalConstants.BUSINESS_VERIFICATION)) {
                    Intent intent = new Intent(getActivity(), CaseBusinessVerificationActivity.class);
                    intent.putExtra("CASE_ID", listPendingCases.get(position).getCase_id());
                    intent.putExtra("case_detailed_id", listPendingCases.get(position).getCase_detail_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listPendingCases.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView case_id, activity_id, activity_type, name, mobile, address;
            private Button buttonView;

            public ViewHolder(View itemView) {
                super(itemView);
                case_id = itemView.findViewById(R.id.pCase_id);
                activity_id = itemView.findViewById(R.id.pActivity_id);
                activity_type = itemView.findViewById(R.id.pActivity_type);
                name = itemView.findViewById(R.id.pCustomer_name);
                mobile = itemView.findViewById(R.id.pMobile_Number);
                address = itemView.findViewById(R.id.pAddress);
                buttonView = itemView.findViewById(R.id.buttonView);
            }
        }
    }
}
