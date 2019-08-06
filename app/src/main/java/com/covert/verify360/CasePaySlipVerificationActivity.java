package com.covert.verify360;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.covert.verify360.AdapterClasses.MyAdapter;
import com.covert.verify360.BeanClasses.FormElementDatum;
import com.covert.verify360.BeanClasses.InnerSubSection;
import com.covert.verify360.BeanClasses.PendingCaseDetails;
import com.covert.verify360.BeanClasses.ResponseMessage;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Services.FactoryService;
import Services.FinalRemarks;
import Services.IPendingCaseDetails;
import Services.MFormSubmissionService;
import Services.NeighbourCheckService;
import Services.PaySlipenquiryService;
import Services.UploadImage;
import Utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    @BindView(R.id.add_image)
    Button add_image;

    @BindView(R.id.buttonNeighbourCheck)
    Button buttonNeighbourCheck;

    @BindView(R.id.neighbour1_Name)
    TextInputEditText neighbour1_Name;
    @BindView(R.id.neighbour1_Address)
    TextInputEditText neighbour1_Address;
    @BindView(R.id.neighbour1_Remarks)
    TextInputEditText neighbour1_Remarks;

    @BindView(R.id.radioGroupAdverse)
    RadioGroup radioGroupAdverse;
    @BindView(R.id.radioGroupConfirmedStay)
    RadioGroup radioGroupConfirmedStay;
    @BindView(R.id.radioGroupPoliticalContact)
    RadioGroup radioGroupPoliticalContact;
    @BindView(R.id.radioGroupRowdism)
    RadioGroup radioGroupRowdism;


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
        buttonSubmitform.setOnClickListener(v1 -> submitForm());
        buttonSubmitPaySlipDetails.setOnClickListener(v1 -> submitPaySlipEnquiry());
        buttonfinalStatus.setOnClickListener(v2 -> submitfinalStatus());
        buttonNeighbourCheck.setOnClickListener(v1 -> submitNeighbourdetails());

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
                Intent intent = new Intent(CasePaySlipVerificationActivity.this,
                        LocationPhotoActivity.class);
                intent.putExtra(Constants.KEY_SELCTOR, 2);
                intent.putExtra(Constants.KEY_CASE_ID, case_id);
                intent.putExtra(Constants.KEY_CASE_DETAIL_ID, case_detail_id);
                intent.putExtra(Constants.KEY_WORKING_BY, working_by);
                startActivity(intent);
            }
        });
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
                            finish();
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
        Map<String, String> mMap = new HashMap<>();


        if (formElementData != null && formElementData.size() > 0) {


            for(int j=0;j<formElementData.size();j++){
                for(int i = 0;i<formElementData.get(j).getOuterSubSection().size();i++){
                    if (!formElementData.get(j).getOuterSubSection().get(i).isCatagory_selected()){
                        Toast.makeText(this,"Please fill all values...!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }



            if (validated(formElementData)) {
                for (int i = 0; i < formElementData.size(); i++) {
                    FormElementDatum datum = formElementData.get(i);
                    for (int j = 0; j < datum.getOuterSubSection().size(); j++) {
                        InnerSubSection section = datum.getOuterSubSection().get(j);
                        String selectedId = "";
                        String remark = "";
                        for (int k = 0; k < section.getOptionssection().size(); k++) {
                            if (section.getOptionssection().get(k).isSelected()) {
                                selectedId = "" + section.getOptionssection().get(k).getFormElementId();
                                remark = section.getBuilder();
                                mMap.put(selectedId, remark);
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

            } else {

                Toast.makeText(this, "Please enter all mandatory fields...! ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validated(List<FormElementDatum> formElementData) {

        for (int i =0;i<formElementData.size();i++){
            if (!formElementData.get(i).isVaidated()){
                return false;
            }
        }

        return true;


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
                        Toast.makeText(CasePaySlipVerificationActivity.this,
                                "Form details submitted", Toast.LENGTH_SHORT).show();
                        buttonSubmitform.setText("Form submited");
                        buttonSubmitform.setTextColor(getResources().getColor(R.color.md_red));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Toast.makeText(CasePaySlipVerificationActivity.this,
                        "Failed to submit details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void submitForm() {
        List<Items> itemsList = mainSectionAdapter.getItems();
        if (itemsList != null) {
            for (Items items : itemsList) {
                submitFields(items.getCheckedId(), items.getRemarks());
//                Toast.makeText(this, items.getCheckedId() + "" + items.getRemarks(), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Form Submited successfully", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Please select form details", Toast.LENGTH_SHORT).show();

    }*/

    /*private void submitFields(int checkedRadioButtonId, String s) {
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
    }*/

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
                        mainSectionAdapter = new MainSectionAdapter(CasePaySlipVerificationActivity.this,
                                formElementData, new MainSectionAdapter.OnMainClick() {
                            @Override
                            public void onChange(int pos,boolean value) {
                                try {
                                    formElementData.get(pos).setVaidated(value);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
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


    private void submitNeighbourdetails() {
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
            Call<ResponseMessage> call1 = neighbourCheckService.submitNeighbourCheck(case_id, case_detail_id,
                    neighbour1_Name.getText().toString(),
                    neighbour1_Address.getText().toString(),
                    neighbour1_Remarks.getText().toString(),
                    adverseText, confirmedstay, political,
                    rowdism,
                    working_by, "1");

            call1.enqueue(new Callback<ResponseMessage>() {
                @Override
                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().isError()) {
                            Toast.makeText(CasePaySlipVerificationActivity.this, "Neighbour 1 details submitted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CasePaySlipVerificationActivity.this, "Failed, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseMessage> call, Throwable t) {
                    Toast.makeText(CasePaySlipVerificationActivity.this, "Something went wrong, try again ", Toast.LENGTH_SHORT).show();
                }
            });

//            boolean adverseText1, confirmedstay1, political1, rowdism1;
//            RadioButton radioButtonAdverse1 = findViewById(radioGroupAdverse1.getCheckedRadioButtonId());
//            if (radioButtonAdverse1.getText().toString().equalsIgnoreCase("yes")) {
//                adverseText1 = true;
//            } else {
//                adverseText1 = false;
//            }
//            RadioButton radioButtonconfirmedStay1 = findViewById(radioGroupConfirmedStay1.getCheckedRadioButtonId());
//            if (radioButtonconfirmedStay1.getText().toString().equalsIgnoreCase("yes")) {
//                confirmedstay1 = true;
//            } else {
//                confirmedstay1 = false;
//            }
//            RadioButton radioButtonpolitical1 = findViewById(radioGroupPoliticalContact1.getCheckedRadioButtonId());
//            if (radioButtonpolitical1.getText().toString().equalsIgnoreCase("yes")) {
//                political1 = true;
//            } else {
//                political1 = false;
//            }
//            RadioButton radioButtonrowdism1 = findViewById(radioGroupRowdism1.getCheckedRadioButtonId());
//            if (radioButtonrowdism1.getText().toString().equalsIgnoreCase("yes")) {
//                rowdism1 = true;
//            } else {
//                rowdism1 = false;
//            }
//            NeighbourCheckService neighbourCheckService1 = FactoryService.createService(NeighbourCheckService.class);
//            Call<ResponseMessage> call = neighbourCheckService1.submitNeighbourCheck(case_id, case_detail_id, neighbour2_Name.getText().toString()
//                    , neighbour2_Address.getText().toString(), neighbour2_Remarks.getText().toString(),
//                    adverseText1, confirmedstay1, political1, rowdism1, working_by, "1");
//            call.enqueue(new Callback<ResponseMessage>() {
//                @Override
//                public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
//                    if (response.isSuccessful()) {
//                        if (!response.body().isError()) {
//                            Toast.makeText(CasePaySlipVerificationActivity.this, "Neighbour 2 details submitted", Toast.LENGTH_SHORT).show();
////                            viewGroupNeighbour.setVisibility(View.GONE);
//                        } else {
//                            Toast.makeText(CasePaySlipVerificationActivity.this, "Failed, try again", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseMessage> call, Throwable t) {
//                    Toast.makeText(CasePaySlipVerificationActivity.this, "Something went wrong, try again ", Toast.LENGTH_SHORT).show();
//                }
//            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}

