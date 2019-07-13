package com.covert.verify360.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.covert.verify360.BeanClasses.CanclledCasesUser;
import com.covert.verify360.R;

public class CancelledCasesDetailsFragment extends Fragment {
    View v;
    private SharedPreferences sharedPreferences;
    TextView case_id, activity_id, client, pre_post, product_type, activity_type, company_name,
            applicant_name, father_name, mobile_number, email, address, cancelledDateTv, remarks, CancelReason, CancelComment;
    Button buttonDownload, buttonAccept, buttonReject, viewButton;
    CanclledCasesUser canclledCasesUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.cancelledcases_details_fragment,container,false);
        sharedPreferences = getActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Case Details");
        try {
            Bundle bundle= getArguments();
            canclledCasesUser = (CanclledCasesUser) bundle.getSerializable("xyz");
        }catch (Exception e){
            e.printStackTrace();
        }
        case_id = v.findViewById(R.id.case_id);
        activity_id = v.findViewById(R.id.activity_id);
        client = v.findViewById(R.id.client);
        pre_post = v.findViewById(R.id.pre_post);
        product_type = v.findViewById(R.id.product_type);
        activity_type = v.findViewById(R.id.activity_type);
        company_name = v.findViewById(R.id.company_name);
        applicant_name = v.findViewById(R.id.applicant_name);
        father_name = v.findViewById(R.id.father_name);
        mobile_number = v.findViewById(R.id.mobile_number);
        email = v.findViewById(R.id.email);
        address = v.findViewById(R.id.address);
        cancelledDateTv = v.findViewById(R.id.est_complete_date);
        remarks = v.findViewById(R.id.remarks);
        CancelReason= v.findViewById(R.id.cancel_reason);
        CancelComment= v.findViewById(R.id.cancel_comments);
        case_id.setText(canclledCasesUser.getCase_id());
        activity_id.setText(canclledCasesUser.getCase_detail_id());
        client.setText(canclledCasesUser.getClient_name());
        pre_post.setText(canclledCasesUser.getPre_post());
        product_type.setText(canclledCasesUser.getProduct_type());
        activity_type.setText(canclledCasesUser.getActivity_type());
        company_name.setText(canclledCasesUser.getCompany_name());
        applicant_name.setText(canclledCasesUser.getApplicant_first_name()
                + " " + canclledCasesUser.getApplicant_last_name());
        father_name.setText(canclledCasesUser.getFather_name());
        mobile_number.setText(canclledCasesUser.getPrimary_contact());
        email.setText(canclledCasesUser.getEmail());
        address.setText(canclledCasesUser.getDoor_number() + ", "
                + canclledCasesUser.getStreet_address() + ", "
                + canclledCasesUser.getLandmark() + "\n"
                + canclledCasesUser.getLocation() + ", "
                + canclledCasesUser.getPincode() + "\n" + canclledCasesUser.getRegion_name());
        cancelledDateTv.setText(canclledCasesUser.getCancelledOn());
        remarks.setText(canclledCasesUser.getRemarks());
        CancelReason.setText(canclledCasesUser.getCancelled_reason());
        CancelComment.setText(canclledCasesUser.getCancelled_reason_description());
        return v;
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
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cancelled Case Details");
    }
}
