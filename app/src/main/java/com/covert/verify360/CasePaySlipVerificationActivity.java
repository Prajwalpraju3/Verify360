package com.covert.verify360;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.covert.verify360.AdapterClasses.MainSectionAdapter;
import com.covert.verify360.BeanClasses.FormElementDatum;
import com.covert.verify360.BeanClasses.Items;
import com.covert.verify360.BeanClasses.PendingCaseDetails;
import com.covert.verify360.BeanClasses.ResponseMessage;

import java.util.List;
import java.util.Objects;

import Services.FactoryService;
import Services.FinalRemarks;
import Services.FormSubmissionService;
import Services.IPendingCaseDetails;
import Services.PaySlipenquiryService;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CasePaySlipVerificationActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recylerViewPaySlipVerification)
    RecyclerView recyclerViewPaySlip;
    @BindView(android.R.id.content)
    ViewGroup v;
    @BindView(R.id.buttonSubmitForm)
    Button buttonSubmitform;
    @BindView(R.id.submitPaySlipDetails)
    Button buttonSubmitPaySlipDetails;
    @BindView(R.id.buttonFinalstatus)
    Button buttonfinalStatus;

    @BindView(R.id.sMet)
    TextInputEditText sMet;
    @BindView(R.id.sDesignation)
    TextInputEditText sDesignation;
    @BindView(R.id.sApplicantDesignation)
    TextInputEditText sApplicangDesignation;
    @BindView(R.id.sEmployeeSince)
    TextInputEditText sEmployeeSince;
    @BindView(R.id.sNatureOFBusiness)
    TextInputEditText sNature_of_business;
    @BindView(R.id.striggerExplaination)
    TextInputEditText sExplaination;
    @BindView(R.id.radioGroupSalarySlipStatus)
    RadioGroup radioGroupSalaryStatus;

    @BindView(R.id.finalStatusRadioGroup)
    RadioGroup radioGroupfinalStatus;
    @BindView(R.id.additionalRemarks)
    EditText finaladditionalRemarks;


    private String case_id;
    private String case_detail_id;
    private String working_by;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private MainSectionAdapter mainSectionAdapter;
    private List<FormElementDatum> formElementData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pay_slip_verification);
        ButterKnife.bind(this);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleMarginStart(20);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        progressBar.setVisibility(View.GONE);
        recyclerViewPaySlip.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPaySlip.setHasFixedSize(true);
        recyclerViewPaySlip.setNestedScrollingEnabled(false);

        sharedPreferences = this.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
//        buttonSubmitform.setOnClickListener(v1 -> submitForm());
        buttonSubmitPaySlipDetails.setOnClickListener(v1 -> submitPaySlipEnquiry());
        buttonfinalStatus.setOnClickListener(v2 -> submitfinalStatus());

        intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            case_id = bundle.getString("CASE_ID");
            case_detail_id = bundle.getString("case_detailed_id");
            working_by = null;
            if (sharedPreferences != null && sharedPreferences.contains("EMP_ID")) {
                working_by = sharedPreferences.getString("EMP_ID", "");
            }
            setData(working_by, case_id, case_detail_id);
        } else {
            Toast.makeText(this, "Cannot fetch Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitPaySlipEnquiry() {
        boolean isEmpty = false;
        if (sMet.getText().toString().trim().length() == 0) {
            isEmpty = true;
            sMet.setError("Enter Met with");
        }
        if (sDesignation.getText().toString().trim().length() == 0) {
            isEmpty = true;
            sDesignation.setError("Enter Designation");
        }
        if (sApplicangDesignation.getText().toString().trim().length() == 0) {
            isEmpty = true;
            sApplicangDesignation.setError("Enter applicant designation");
        }
        if (sNature_of_business.getText().toString().trim().length() == 0) {
            isEmpty = true;
            sNature_of_business.setError("Enter nature of business");
        }
        if (sEmployeeSince.getText().toString().trim().length() == 0) {
            isEmpty = true;
            sEmployeeSince.setError("Enter employee since");
        }
        if (sExplaination.getText().toString().trim().length() == 0) {
            isEmpty = true;
            sExplaination.setError("Enter explaination");
        }
        if (radioGroupSalaryStatus.getCheckedRadioButtonId() == -1) {
            isEmpty = true;
            Toast.makeText(this, "Select Salary Status", Toast.LENGTH_SHORT).show();
        }

        if (!isEmpty) {
            final RadioButton radioButton=findViewById(radioGroupSalaryStatus.getCheckedRadioButtonId());
            PaySlipenquiryService paySlipenquiryService = FactoryService.createService(PaySlipenquiryService.class);
            Call<ResponseMessage> call = paySlipenquiryService.submitEnquiryDetails(case_id, case_detail_id, sMet.getText().toString()
                    , sDesignation.getText().toString(), sApplicangDesignation.getText().toString()
                    , sEmployeeSince.getText().toString(), radioButton.getText().toString()
                    , sNature_of_business.getText().toString(), sExplaination.getText().toString(), working_by);
            call.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if(response.isSuccessful()){
                        if(!response.body().isError()){
                            Toast.makeText(CasePaySlipVerificationActivity.this, "Payslip enquiry details submitted. ", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(CasePaySlipVerificationActivity.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    Toast.makeText(CasePaySlipVerificationActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void submitfinalStatus() {
        boolean isEmpty = false;
        if (radioGroupfinalStatus.getCheckedRadioButtonId() == -1) {
            isEmpty = true;
            Toast.makeText(this, "Select final status", Toast.LENGTH_SHORT).show();
        }
        if (finaladditionalRemarks.getText().toString().trim().length() == 0) {
            isEmpty = true;
            finaladditionalRemarks.setError("Enter remarks");
        }
        if (!isEmpty) {
            RadioButton radioButtonfinalStatus = findViewById(radioGroupfinalStatus.getCheckedRadioButtonId());
            FinalRemarks finalRemarks = FactoryService.createService(FinalRemarks.class);
            Call<ResponseMessage> call = finalRemarks.submitFinalRemarks(radioButtonfinalStatus.getText().toString(),
                    finaladditionalRemarks.getText().toString(), case_id, case_detail_id, working_by);
            call.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().isError()) {
                            Toast.makeText(CasePaySlipVerificationActivity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    Toast.makeText(CasePaySlipVerificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void submitForm() {
        List<Items> itemsList = mainSectionAdapter.getItems();
        if (itemsList != null) {
            for (Items items : itemsList) {
                submitFields(items.getCheckedId(), items.getRemarks());
//                Toast.makeText(this, items.getCheckedId() + "" + items.getRemarks(), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Form Submited successfully", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Please select form details", Toast.LENGTH_SHORT).show();

    }

    private void submitFields(int checkedRadioButtonId, String s) {
        FormSubmissionService submissionService = FactoryService.createService(FormSubmissionService.class);
        Call<ResponseMessage> call = submissionService.submitForm("1", case_id, case_detail_id, Integer.toString(checkedRadioButtonId), s, working_by);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(CaseBusinessVerificationActivity.this, "Form details submitted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Toast.makeText(CasePaySlipVerificationActivity.this, "Failed to submit details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(String working_by, String case_id, String case_detail_id) {
        progressBar.setVisibility(View.VISIBLE);
        if (formElementData != null) {
            formElementData.clear();
        }
        IPendingCaseDetails caseDetails = FactoryService.createService(IPendingCaseDetails.class);
        Call<PendingCaseDetails> call = caseDetails.getPendingCases(working_by, case_id, case_detail_id);
        call.enqueue(new Callback<PendingCaseDetails>() {
            @Override
            public void onResponse(Call<PendingCaseDetails> call, Response<PendingCaseDetails> response) {
                if (response.isSuccessful()) {

                    if (!response.body().getError()) {
                        formElementData = response.body().getFormElementData();
                        mainSectionAdapter = new MainSectionAdapter(CasePaySlipVerificationActivity.this, formElementData);
                        recyclerViewPaySlip.setAdapter(mainSectionAdapter);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        ViewGroup viewGroup = findViewById(R.id.nestedScrollView);
                        viewGroup.removeAllViews();
                        View errorView = LayoutInflater.from(CasePaySlipVerificationActivity.this).inflate(R.layout.layout_error, v, false);
                        viewGroup.addView(errorView);
                        Toast.makeText(CasePaySlipVerificationActivity.this, "Error showing form", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PendingCaseDetails> call, Throwable t) {
                Toast.makeText(CasePaySlipVerificationActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                ViewGroup viewGroup = findViewById(R.id.nestedScrollView);
                viewGroup.removeAllViews();
                View errorView = LayoutInflater.from(CasePaySlipVerificationActivity.this).inflate(R.layout.layout_error, v, false);
                viewGroup.addView(errorView);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}

