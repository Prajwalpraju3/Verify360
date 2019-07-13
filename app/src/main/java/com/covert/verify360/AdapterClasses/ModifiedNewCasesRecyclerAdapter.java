package com.covert.verify360.AdapterClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.covert.verify360.BeanClasses.NewCasesBean;
import com.covert.verify360.CaseBusinessVerificationActivity;
import com.covert.verify360.CasePaySlipVerificationActivity;
import com.covert.verify360.CaseResidentVerificationActivity;
import com.covert.verify360.GlobalConstants;
import com.covert.verify360.R;

import java.util.ArrayList;

public class ModifiedNewCasesRecyclerAdapter extends RecyclerView.Adapter<ModifiedNewCasesRecyclerAdapter.ViewHolder>{


    Context context;
    ArrayList<NewCasesBean> newCasesBeans;
    CaseSelected caseSelected;

    public interface CaseSelected{

        void selectedNewCase(int position, NewCasesBean newCasesBean);
    }

    public ModifiedNewCasesRecyclerAdapter(Context context, ArrayList<NewCasesBean> newCasesBeans,CaseSelected caseSelected) {
        this.context = context;
        this.newCasesBeans = newCasesBeans;
        this.caseSelected = caseSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modified_casesdetails_row, parent, false);
        return new ModifiedNewCasesRecyclerAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.case_id.setText(newCasesBeans.get(position).getCase_id());
        holder.activity_id.setText( newCasesBeans.get(position).getCase_detail_id());
        holder.activity_type.setText( newCasesBeans.get(position).getActivity_type());
        holder.name.setText(newCasesBeans.get(position).getApplicant_first_name() + " "
                + newCasesBeans.get(position).getApplicant_last_name());
        holder.mobile.setText(newCasesBeans.get(position).getPrimary_contact());
        holder.address.setText(newCasesBeans.get(position).getDoor_number() + ", "
                + newCasesBeans.get(position).getStreet_address() + ", "
                + newCasesBeans.get(position).getLandmark() + "\n"
                + newCasesBeans.get(position).getLocation() + ", "
                + newCasesBeans.get(position).getPincode() + "\n" + newCasesBeans.get(position).getRegion_name());

//        holder.viewMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(holder.addressLayout.getVisibility() == View.VISIBLE){
//                    holder.addressLayout.setVisibility(View.GONE);
//                }else{
//                    holder.addressLayout.setVisibility(View.VISIBLE);
//                }
//            }
//        });


        holder.startButtonView.setOnClickListener(v -> {
            final String activityType = newCasesBeans.get(position).getActivity_type();
            if (activityType.toLowerCase().equals(GlobalConstants.RESIDENT_VERIFICATION)) {
                Intent intent = new Intent(context, CaseResidentVerificationActivity.class);
                intent.putExtra("CASE_ID", newCasesBeans.get(position).getCase_id());
                intent.putExtra("case_detailed_id", newCasesBeans.get(position).getCase_detail_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else if (activityType.toLowerCase().equals(GlobalConstants.PAY_SLIP_VERIFICATION)) {
                Intent intent = new Intent(context, CasePaySlipVerificationActivity.class);
                intent.putExtra("CASE_ID", newCasesBeans.get(position).getCase_id());
                intent.putExtra("case_detailed_id", newCasesBeans.get(position).getCase_detail_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else if (activityType.toLowerCase().equals(GlobalConstants.BUSINESS_VERIFICATION)) {
                Intent intent = new Intent(context, CaseBusinessVerificationActivity.class);
                intent.putExtra("CASE_ID", newCasesBeans.get(position).getCase_id());
                intent.putExtra("case_detailed_id", newCasesBeans.get(position).getCase_detail_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caseSelected.selectedNewCase(position,newCasesBeans.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return newCasesBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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
