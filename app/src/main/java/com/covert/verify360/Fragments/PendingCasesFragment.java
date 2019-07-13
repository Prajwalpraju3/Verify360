package com.covert.verify360.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.covert.verify360.BeanClasses.CancelledReasonsList;
import com.covert.verify360.BeanClasses.PendingCasesBean;
import com.covert.verify360.BeanClasses.PendingCasesResponse;
import com.covert.verify360.CancelSelectedCase;
import com.covert.verify360.CaseBusinessVerificationActivity;
import com.covert.verify360.CasePaySlipVerificationActivity;
import com.covert.verify360.CaseResidentVerificationActivity;
import com.covert.verify360.GlobalConstants;
import com.covert.verify360.Helpers.ProcessAlertDialogue;
import com.covert.verify360.MainActivity;
import com.covert.verify360.R;

import java.util.ArrayList;
import java.util.List;

import Services.FactoryService;
import Services.GetCancellationReasons;
import Services.IPendingCases;
import Services.PassCancelReasons;
import database_utils.DatabaseHandler;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class PendingCasesFragment extends Fragment implements CancelSelectedCase, PassCancelReasons {
    String working_by;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private List<PendingCasesBean> pendingCasesBeanList;
    private ProcessAlertDialogue processAlertDialogue;
    private DatabaseHandler db_instance;
    PendingCasesBean pendingCasesBean;
    private PendingCasesAdapter pendingCasesAdapter;
    View v;
    View NoData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        processAlertDialogue= new ProcessAlertDialogue(getActivity());
        pendingCasesBeanList = new ArrayList<>();
        db_instance = DatabaseHandler.getDatabaseInstance(getActivity());
        AsyncTask.execute(() -> db_instance.pendingCasesDao().deleteAllCases());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_pending_cases, container, false);
        sharedPreferences = getActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Pending Cases");
        recyclerView = v.findViewById(R.id.pendingCasesRview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        NoData=v.findViewById(R.id.no_data);
        NoData.setVisibility(View.GONE);

        working_by = null;
        if (sharedPreferences != null && sharedPreferences.contains("EMP_ID")) {
            working_by = sharedPreferences.getString("EMP_ID", "");
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPendingData(working_by);

        db_instance.pendingCasesDao().getCasesFromDB().observe(getActivity(), pendingcases -> {
            pendingCasesBeanList = pendingcases;
            setAdapter(pendingCasesBeanList);
        });
    }


    private void setAdapter(List<PendingCasesBean> pendingCasesBeans){
        pendingCasesAdapter = new PendingCasesAdapter(pendingCasesBeanList,
                PendingCasesFragment.this.getActivity(),this);
        recyclerView.setAdapter(pendingCasesAdapter);

    }

    private void setPendingData(String working_by) {
        if (!MainActivity.isIsConnected()) {
            Snackbar.make(v, "Please connect to internet!",2000).show();
            return;
        }
        if (pendingCasesBeanList != null) {
            pendingCasesBeanList.clear();
        }
        processAlertDialogue.ShowDialogue();
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
                            AsyncTask.execute(() -> db_instance.pendingCasesDao().insertCases(pendingCasesResponse.getNewCasesList()));
                        } else {
                            Snackbar.make(getView(), "Data not found!",2000).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        processAlertDialogue.CloseDialogue();
                        //progressBar.setVisibility(View.GONE);
                        Snackbar.make(getView(), "Server connection problem!",2000).show();
                    }

                    @Override
                    public void onComplete() {
                        processAlertDialogue.CloseDialogue();
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

    @Override
    public void onResume(){
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Pending Cases");
    }

    @Override
    public void cancelCase(PendingCasesBean pendingCasesBean) {
        this.pendingCasesBean = pendingCasesBean;
        GetCancellationReasons  cancellationReasons = new GetCancellationReasons(getContext(),this);
        cancellationReasons.getAllReasons();
    }

    @Override
    public void ReasonsObtained(List<CancelledReasonsList> cancelledReasonsLists) {
        ArrayList<CancelledReasonsList> reasonsLists = new ArrayList<>(cancelledReasonsLists);
        CancelCaseDialogFragment dFragment = new CancelCaseDialogFragment();
        Bundle bundle= new Bundle();
        bundle.putSerializable("xyz", pendingCasesBean);
        bundle.putSerializable("abc",  reasonsLists);
        dFragment.setArguments(bundle);
        FragmentManager fm = new MainActivity().getFmanager();
        dFragment.show(fm,"");
    }

    @Override
    public void Error(String error) {

    }

    private class PendingCasesAdapter extends RecyclerView.Adapter<PendingCasesAdapter.ViewHolder> {
        private List<PendingCasesBean> listPendingCases;
        private Context context;
        CancelSelectedCase cancelSelectedCase;

        public PendingCasesAdapter(List<PendingCasesBean> listPendingCases, Context context,CancelSelectedCase cancelSelectedCase) {
            this.listPendingCases = listPendingCases;
            this.context = context;
            this.cancelSelectedCase = cancelSelectedCase;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewHolder view = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.modified_pendingcase_row, parent, false));
            return view;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.case_id.setText(listPendingCases.get(position).getCase_id());
            holder.activity_id.setText( listPendingCases.get(position).getCase_detail_id());
            holder.activity_type.setText( listPendingCases.get(position).getActivity_type());
            holder.name.setText(listPendingCases.get(position).getApplicant_first_name() + " "
                    + listPendingCases.get(position).getApplicant_last_name());
            holder.mobile.setText(listPendingCases.get(position).getPrimary_contact());
            holder.address.setText(listPendingCases.get(position).getDoor_number() + ", "
                    + listPendingCases.get(position).getStreet_address() + ", "
                    + listPendingCases.get(position).getLandmark() + "\n"
                    + listPendingCases.get(position).getLocation() + ", "
                    + listPendingCases.get(position).getPincode() + "\n" + listPendingCases.get(position).getRegion_name());

            holder.viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(holder.addressLayout.getVisibility() == View.VISIBLE){
                        holder.addressLayout.setVisibility(View.GONE);
                    }else{
                        holder.addressLayout.setVisibility(View.VISIBLE);
                    }
                }
            });


            holder.startButtonView.setOnClickListener(v -> {
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


            holder.cancelButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cancelSelectedCase.cancelCase(listPendingCases.get(position));
                }
            });

        }

        @Override
        public int getItemCount() {
            return listPendingCases.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView case_id, activity_id, activity_type, name, mobile, address,viewMore;
            private Button startButtonView,cancelButtonView;
            private LinearLayout addressLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                case_id = itemView.findViewById(R.id.caseid_textview);
                activity_id = itemView.findViewById(R.id.activityid_textview);
                activity_type = itemView.findViewById(R.id.acttype_textview);
                name = itemView.findViewById(R.id.custname_textview);
                mobile = itemView.findViewById(R.id.phnum_textview);
                viewMore = itemView.findViewById(R.id.moredetails_textview);
                address = itemView.findViewById(R.id.address_textview);
                startButtonView = itemView.findViewById(R.id.start_buttonView);
                cancelButtonView = itemView.findViewById(R.id.cancel_buttonView);
                addressLayout = itemView.findViewById(R.id.address_layout);
            }
        }
    }
}
