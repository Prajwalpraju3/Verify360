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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.covert.verify360.AdapterClasses.MainSectionAdapter;
import com.covert.verify360.BeanClasses.FormElementDatum;
import com.covert.verify360.BeanClasses.InnerSubSection;
import com.covert.verify360.BeanClasses.PendingCaseDetails;
import com.covert.verify360.BeanClasses.ResponseMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Services.EnquiryDetailsService;
import Services.FactoryService;
import Services.FinalRemarks;
import Services.IPendingCaseDetails;
import Services.MFormSubmissionService;
import Services.NeighbourCheckService;
import Utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaseResidentVerificationActivity extends AppCompatActivity {
    @BindView(R.id.linearLayoutresident)
    LinearLayout linearLayout;
    @BindView(R.id.enquirydetailslayout)
    ViewGroup viewGroupEnquiry;
    @BindView(R.id.neighbourlayout)
    ViewGroup viewGroupNeighbour;
    @BindView(R.id.finalstatuslayout)
    ViewGroup viewGroupFinalStatus;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recylerViewResidentVerification)
    RecyclerView recyclerViewResident;
    @BindView(R.id.buttonSubmitForm)
    Button buttonSubmitForm;
    @BindView(R.id.buttonEnquiryResident)
    Button buttonEnquiryResident;
    @BindView(R.id.buttonNeighbourCheck)
    Button buttonNeighbourCheck;
    @BindView(R.id.buttonFinalstatus)
    Button buttonfinalStatus;
    @BindView(R.id.rMet)
    TextInputEditText rMet;
    @BindView(R.id.rRel_with_applicant)
    TextInputEditText rRel_with_applicant;
    @BindView(R.id.rOwned_Rented)
    TextInputEditText rOwned_Rented;
    @BindView(R.id.rYear_of_stay)
    TextInputEditText rYear_of_stay;
    @BindView(R.id.rNo_of_family_members)
    TextInputEditText rNo_of_family_members;
    @BindView(R.id.rNo_of_working)
    TextInputEditText rNo_of_working;
    @BindView(R.id.radioGroupMarital)
    RadioGroup radioGroupMarital;
    @BindView(R.id.rCompanyName)
    TextInputEditText rCompanyName;
    @BindView(R.id.rDesignation)
    TextInputEditText rDesignation;
    @BindView(R.id.rYear_of_exp)
    TextInputEditText rYear_of_exp;

    @BindView(R.id.neighbour1_Name)
    TextInputEditText neighbour1_Name;
    @BindView(R.id.neighbour2_Name)
    TextInputEditText neighbour2_Name;
    @BindView(R.id.neighbour1_Address)
    TextInputEditText neighbour1_Address;
    @BindView(R.id.neighbour2_Address)
    TextInputEditText neighbour2_Address;
    @BindView(R.id.neighbour1_Remarks)
    TextInputEditText neighbour1_Remarks;
    @BindView(R.id.neighbour2_Remarks)
    TextInputEditText neighbour2_Remarks;

    @BindView(R.id.radioGroupAdverse)
    RadioGroup radioGroupAdverse;
    @BindView(R.id.radioGroupAdverse1)
    RadioGroup radioGroupAdverse1;
    @BindView(R.id.radioGroupConfirmedStay)
    RadioGroup radioGroupConfirmedStay;
    @BindView(R.id.radioGroupConfirmedStay1)
    RadioGroup radioGroupConfirmedStay1;
    @BindView(R.id.radioGroupPoliticalContact)
    RadioGroup radioGroupPoliticalContact;
    @BindView(R.id.radioGroupPoliticalContact1)
    RadioGroup radioGroupPoliticalContact1;
    @BindView(R.id.radioGroupRowdism)
    RadioGroup radioGroupRowdism;
    @BindView(R.id.radioGroupRowdism1)
    RadioGroup radioGroupRowdism1;

    @BindView(R.id.finalStatusRadioGroup)
    RadioGroup radioGroupfinalStatus;
    @BindView(R.id.additionalRemarks)
    EditText finaladditionalRemarks;

    @BindView(R.id.add_image)
    Button add_image;

    @BindView(android.R.id.content)
    ViewGroup v;
    private String case_id = null;
    private String case_detail_id = null;
    private String working_by = null;

    private Intent intent;
    private SharedPreferences sharedPreferences;
    private MainSectionAdapter mainSectionAdapter;
    private List<FormElementDatum> formElementData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_resident_verification);
        ButterKnife.bind(this);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleMarginStart(20);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        progressBar.setVisibility(View.GONE);
        recyclerViewResident.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewResident.setHasFixedSize(true);
        recyclerViewResident.setNestedScrollingEnabled(false);

        buttonSubmitForm.setOnClickListener(v1 -> submitForm());
        buttonEnquiryResident.setOnClickListener(v2 -> submitEnquiryDetails());
        buttonNeighbourCheck.setOnClickListener(v3 -> submitNeighbourDetails());
        buttonfinalStatus.setOnClickListener(v4 -> submitFinalStatus());

        sharedPreferences = this.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);

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

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaseResidentVerificationActivity.this,
                        LocationPhotoActivity.class);
                intent.putExtra(Constants.KEY_SELCTOR, 0);
                intent.putExtra(Constants.KEY_CASE_ID, case_id);
                intent.putExtra(Constants.KEY_CASE_DETAIL_ID, case_detail_id);
                intent.putExtra(Constants.KEY_WORKING_BY, working_by);
                startActivity(intent);
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
                        mainSectionAdapter = new MainSectionAdapter(CaseResidentVerificationActivity.this,
                                formElementData);
                        recyclerViewResident.setAdapter(mainSectionAdapter);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        ViewGroup viewGroup = findViewById(R.id.nestedScrollView);
                        viewGroup.removeAllViews();
                        View errorView = LayoutInflater.from(CaseResidentVerificationActivity.this).inflate(R.layout.layout_error, v, false);
                        viewGroup.addView(errorView);
                        Toast.makeText(CaseResidentVerificationActivity.this, "Error showing form", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PendingCaseDetails> call, Throwable t) {
                Toast.makeText(CaseResidentVerificationActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                ViewGroup viewGroup = findViewById(R.id.nestedScrollView);
                viewGroup.removeAllViews();
                View errorView = LayoutInflater.from(CaseResidentVerificationActivity.this).inflate(R.layout.layout_error, v, false);
                viewGroup.addView(errorView);
            }
        });
    }

    private void submitForm() {
        Map<String, String> mMap = new HashMap<>();
        if (formElementData != null && formElementData.size() > 0) {
            for (int i = 0; i < formElementData.size(); i++) {
                FormElementDatum datum = formElementData.get(i);
                for (int j = 0; j < datum.getOuterSubSection().size(); j++) {
                    InnerSubSection section = datum.getOuterSubSection().get(j);
                    String selectedId = "";
                    String remark = "";
                    for (int k = 0; k < section.getOptionssection().size(); k++) {
                        if (section.getOptionssection().get(k).isSelected()){
                            selectedId = "" + section.getOptionssection().get(k).getFormElementId();
                            remark = section.getBuilder();
                            mMap.put(selectedId, remark);
                        }
                    }
                }
            }
        }

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry :
                mMap.entrySet()) {
            builder.append(entry.getKey() + "~" + entry.getValue() + "|");
        }
        submitFields(builder.toString());
    }


    private void submitFields(String s) {
        MFormSubmissionService submissionService = FactoryService.createService(MFormSubmissionService.class);
        Call<ResponseMessage> call = submissionService.submitForm(case_id, case_detail_id, s,
                working_by, 1);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        Toast.makeText(CaseResidentVerificationActivity.this,
                                "Form details submitted", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Toast.makeText(CaseResidentVerificationActivity.this,
                        "Failed to submit details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitNeighbourDetails() {
        boolean isEmpty = false;
        if (neighbour1_Name.getText().toString().trim().length() == 0) {
            isEmpty = true;
            neighbour1_Name.setError("enter neighbour 1 name");
            return;
        }

        if (neighbour1_Address.getText().toString().trim().length() == 0) {
            isEmpty = true;
            neighbour1_Address.setError("enter neighbour 1 address");
            return;
        }
        if (neighbour1_Remarks.getText().toString().trim().length() == 0) {
            isEmpty = true;
            neighbour1_Remarks.setError("enter neighbour 1 remarks");
            return;
        }
        if (radioGroupAdverse.getCheckedRadioButtonId() == -1) {
            isEmpty = true;
            Toast.makeText(this, "Select neighbour adverse ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (radioGroupConfirmedStay.getCheckedRadioButtonId() == -1) {
            isEmpty = true;
            Toast.makeText(this, "Select neighbour confirmed stay ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (radioGroupPoliticalContact.getCheckedRadioButtonId() == -1) {
            isEmpty = true;
            Toast.makeText(this, "Select neighbour political contact ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (radioGroupRowdism.getCheckedRadioButtonId() == -1) {
            isEmpty = true;
            Toast.makeText(this, "Select neighbour rowdism ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (neighbour2_Name.getText().toString().trim().length() == 0) {
            isEmpty = true;
            neighbour2_Name.setError("enter neighbour 2 name");
            return;
        }
        if (neighbour2_Address.getText().toString().trim().length() == 0) {
            isEmpty = true;
            neighbour2_Address.setError("enter neighbour 2 address");
            return;
        }

        if (neighbour2_Remarks.getText().toString().trim().length() == 0) {
            isEmpty = true;
            neighbour2_Remarks.setError("enter neighbour 2 remarks");
            return;
        }
        if (radioGroupAdverse1.getCheckedRadioButtonId() == -1) {
            isEmpty = true;
            Toast.makeText(this, "Select neighbour2 adverse ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (radioGroupConfirmedStay1.getCheckedRadioButtonId() == -1) {
            isEmpty = true;
            Toast.makeText(this, "Select neighbour 2 confirmed stay ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (radioGroupPoliticalContact1.getCheckedRadioButtonId() == -1) {
            isEmpty = true;
            Toast.makeText(this, "Select neighbour 2 political contact ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (radioGroupRowdism1.getCheckedRadioButtonId() == -1) {
            isEmpty = true;
            Toast.makeText(this, "Select neighbour 2 rowdism ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isEmpty) {
//            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            boolean adverseText, confirmedstay, political, rowdism;
            RadioButton radioButtonAdverse = findViewById(radioGroupAdverse.getCheckedRadioButtonId());
            if (radioButtonAdverse.getText().toString().equalsIgnoreCase("yes")) {
                adverseText = true;
            } else {
                adverseText = false;
            }
            RadioButton radioButtonconfirmedStay = findViewById(radioGroupConfirmedStay.getCheckedRadioButtonId());
            if (radioButtonconfirmedStay.getText().toString().equalsIgnoreCase("yes")) {
                confirmedstay = true;
            } else {
                confirmedstay = false;
            }
            RadioButton radioButtonpolitical = findViewById(radioGroupPoliticalContact.getCheckedRadioButtonId());
            if (radioButtonpolitical.getText().toString().equalsIgnoreCase("yes")) {
                political = true;
            } else {
                political = false;
            }
            RadioButton radioButtonrowdism = findViewById(radioGroupRowdism.getCheckedRadioButtonId());
            if (radioButtonrowdism.getText().toString().equalsIgnoreCase("yes")) {
                rowdism = true;
            } else {
                rowdism = false;
            }
            NeighbourCheckService neighbourCheckService = FactoryService.createService(NeighbourCheckService.class);
            Call<ResponseMessage> call1 = neighbourCheckService.submitNeighbourCheck(case_id, case_detail_id, neighbour1_Name.getText().toString()
                    , neighbour1_Address.getText().toString(), neighbour1_Remarks.getText().toString(),
                    adverseText, confirmedstay, political, rowdism, working_by, "1");
            call1.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().isError()) {
                            Toast.makeText(CaseResidentVerificationActivity.this, "Neighbour 1 details submitted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CaseResidentVerificationActivity.this, "Failed, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    Toast.makeText(CaseResidentVerificationActivity.this, "Something went wrong, try again ", Toast.LENGTH_SHORT).show();
                }
            });

            boolean adverseText1, confirmedstay1, political1, rowdism1;
            RadioButton radioButtonAdverse1 = findViewById(radioGroupAdverse1.getCheckedRadioButtonId());
            if (radioButtonAdverse1.getText().toString().equalsIgnoreCase("yes")) {
                adverseText1 = true;
            } else {
                adverseText1 = false;
            }
            RadioButton radioButtonconfirmedStay1 = findViewById(radioGroupConfirmedStay1.getCheckedRadioButtonId());
            if (radioButtonconfirmedStay1.getText().toString().equalsIgnoreCase("yes")) {
                confirmedstay1 = true;
            } else {
                confirmedstay1 = false;
            }
            RadioButton radioButtonpolitical1 = findViewById(radioGroupPoliticalContact1.getCheckedRadioButtonId());
            if (radioButtonpolitical1.getText().toString().equalsIgnoreCase("yes")) {
                political1 = true;
            } else {
                political1 = false;
            }
            RadioButton radioButtonrowdism1 = findViewById(radioGroupRowdism1.getCheckedRadioButtonId());
            if (radioButtonrowdism1.getText().toString().equalsIgnoreCase("yes")) {
                rowdism1 = true;
            } else {
                rowdism1 = false;
            }
            NeighbourCheckService neighbourCheckService1 = FactoryService.createService(NeighbourCheckService.class);
            Call<ResponseMessage> call = neighbourCheckService1.submitNeighbourCheck(case_id, case_detail_id, neighbour2_Name.getText().toString()
                    , neighbour2_Address.getText().toString(), neighbour2_Remarks.getText().toString(),
                    adverseText1, confirmedstay1, political1, rowdism1, working_by, "1");
            call.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().isError()) {
                            Toast.makeText(CaseResidentVerificationActivity.this, "Neighbour 2 details submitted", Toast.LENGTH_SHORT).show();
                            viewGroupNeighbour.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(CaseResidentVerificationActivity.this, "Failed, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    Toast.makeText(CaseResidentVerificationActivity.this, "Something went wrong, try again ", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void submitEnquiryDetails() {
        boolean isEmpty = false;
        if (rMet.getText().toString().trim().length() == 0) {
            isEmpty = true;
            rMet.setError("enter name");
        }
        if (rRel_with_applicant.getText().toString().trim().length() == 0) {
            isEmpty = true;
            rRel_with_applicant.setError("enter relationship with applicant");
        }
        if (rOwned_Rented.getText().toString().trim().length() == 0) {
            isEmpty = true;
            rOwned_Rented.setError("enter owned or rented");
        }
        if (rYear_of_stay.getText().toString().trim().length() == 0) {
            isEmpty = true;
            rYear_of_stay.setError("enter year of stay");
        }
        if (rNo_of_family_members.getText().toString().trim().length() == 0) {
            isEmpty = true;
            rNo_of_family_members.setError("enter number of family members");
        }
        if (rNo_of_working.getText().toString().trim().length() == 0) {
            isEmpty = true;
            rNo_of_working.setError("enter number of working");
        }
        if (rDesignation.getText().toString().trim().length() == 0) {
            isEmpty = true;
            rDesignation.setError("enter designation");
        }
        if (rCompanyName.getText().toString().trim().length() == 0) {
            isEmpty = true;
            rCompanyName.setError("enter company");
        }
        if (rYear_of_exp.getText().toString().trim().length() == 0) {
            isEmpty = true;
            rYear_of_exp.setError("enter year of exp");
        }
        if (radioGroupMarital.getCheckedRadioButtonId() == -1) {
            isEmpty = true;
            Toast.makeText(this, "Select marital status", Toast.LENGTH_SHORT).show();
        }
        if (!isEmpty) {
            int id = radioGroupMarital.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(id);
            EnquiryDetailsService enquiryDetailsService = FactoryService.createService(EnquiryDetailsService.class);
            Call<ResponseMessage> call = enquiryDetailsService.submitEnquiryDetails(case_id, case_detail_id, rMet.getText().toString()
                    , rRel_with_applicant.getText().toString(), rOwned_Rented.getText().toString(), radioButton.getText().toString()
                    , rYear_of_stay.getText().toString(), rNo_of_family_members.getText().toString(), rNo_of_working.getText().toString()
                    , rCompanyName.getText().toString(), rDesignation.getText().toString(), rYear_of_exp.getText().toString(), working_by);
            call.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().isError()) {
                            Toast.makeText(CaseResidentVerificationActivity.this, "Enquiry Details submitted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CaseResidentVerificationActivity.this, "Failed to submit details try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    Toast.makeText(CaseResidentVerificationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void submitFinalStatus() {
        boolean isEmpty = false;
        if (radioGroupfinalStatus.getCheckedRadioButtonId() == -1) {
            isEmpty = true;
            Toast.makeText(this, "Select final status", Toast.LENGTH_SHORT).show();
            return;
        }
        if (finaladditionalRemarks.getText().toString().trim().length() == 0) {
            isEmpty = true;
            finaladditionalRemarks.setError("Enter remarks");
            return;
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
                            Toast.makeText(CaseResidentVerificationActivity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    Toast.makeText(CaseResidentVerificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}

