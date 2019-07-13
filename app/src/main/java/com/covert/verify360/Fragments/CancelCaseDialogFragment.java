package com.covert.verify360.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.covert.verify360.BeanClasses.CancelledReasonsList;
import com.covert.verify360.BeanClasses.PendingCasesBean;
import com.covert.verify360.BeanClasses.StatusTxnDetailsPost;
import com.covert.verify360.Helpers.FragmentNavigator;
import com.covert.verify360.Helpers.InfoAlertDialogue;
import com.covert.verify360.Helpers.InfoAlertDialogueWithBtn;
import com.covert.verify360.Helpers.OnAlertBtnClick;
import com.covert.verify360.Helpers.ProcessAlertDialogue;
import com.covert.verify360.MainActivity;
import com.covert.verify360.R;

import java.util.ArrayList;

import Services.StatusTxnDetailsResponse;
import Services.StatusTxnResponse;

public class CancelCaseDialogFragment extends android.support.v4.app.DialogFragment implements StatusTxnDetailsResponse {
    View dialogueView;
    PendingCasesBean cancelCase;
    ArrayList<CancelledReasonsList> resonsList;
    Spinner reasonsToChoose;
    EditText remarksEt;
    Button submitBtn,cancelBtn;
    private SharedPreferences sharedPreferences;
    ArrayList<String> reasons = new ArrayList<>();
    String empid;
    ProcessAlertDialogue processAlertDialogue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        dialogueView = inflater.inflate(R.layout.cancelcase_dialog_layout,container,false);
        reasonsToChoose = dialogueView.findViewById(R.id.reasons_spinner);
        remarksEt = dialogueView.findViewById(R.id.remarks_edittext);
        submitBtn = dialogueView.findViewById(R.id.start_buttonView);
        cancelBtn = dialogueView.findViewById(R.id.cancel_buttonView);
        processAlertDialogue= new ProcessAlertDialogue(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);

        try {
            Bundle bundle= getArguments();
            cancelCase = (PendingCasesBean) bundle.getSerializable("xyz");
            resonsList = (ArrayList<CancelledReasonsList>) bundle.getSerializable("abc");
        }catch (Exception e){

            e.printStackTrace();
        }
        empid = null;
        if (sharedPreferences != null && sharedPreferences.contains("EMP_ID")) {
            empid = sharedPreferences.getString("EMP_ID", "");
        }
        for(int i=0; i<resonsList.size();i++){
            reasons.add(resonsList.get(i).getCancelledReasonText());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_row, reasons);
        reasonsToChoose.setAdapter(dataAdapter);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


        return dialogueView;
    }


    private void postData(){
        StatusTxnDetailsPost statusTxnDetailsPost = new StatusTxnDetailsPost(this,getActivity());
        String reasonId = resonsList.get(reasonsToChoose.getSelectedItemPosition()).getCase_detail_id();
        String reasonStr = "";
        if(remarksEt.getText().toString().length()== 0){
            reasonStr = "";
        }else if(remarksEt.getText().toString().length()> 0){
            reasonStr = remarksEt.getText().toString();
        }
        processAlertDialogue.ShowDialogue();
        statusTxnDetailsPost.postStatusTxndetailsResponse(cancelCase.getCase_id(),cancelCase.getCase_detail_id(),empid,"13",reasonId,reasonStr);
    }

    @Override
    public void responseDetails(StatusTxnResponse statusTxnResponse) {
        if(statusTxnResponse.getError().equals("false")){
            getDialog().dismiss();
            processAlertDialogue.CloseDialogue();
            new InfoAlertDialogueWithBtn(getActivity()).ShowDialogue("Information", "Case Cancelled", new OnAlertBtnClick() {
                @Override
                public void onClick(boolean whichBtn) {
                    FragmentNavigator.Navigate(MainActivity.fManager, new PendingCasesFragment());
                }
            });
        }else{
            new InfoAlertDialogue(getActivity()).ShowDialogue("Information", "Please try again.");
        }
    }

    @Override
    public void errorResponse(String error) {
        getDialog().dismiss();
        processAlertDialogue.CloseDialogue();
        new InfoAlertDialogue(getActivity()).ShowDialogue("Information", "Please try again.");
    }
}
