package com.covert.verify360.AdapterClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.covert.verify360.BeanClasses.CanclledCasesUser;
import com.covert.verify360.R;

import java.util.ArrayList;

public class CancelledCasesAdapter extends RecyclerView.Adapter<CancelledCasesAdapter.ViewHolder> {

    Context context;
    ArrayList<CanclledCasesUser> canclledCasesUsers;
    CaseSelected caseSelected;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cancel_item, parent, false);
        return new CancelledCasesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.caseIdTv.setText(canclledCasesUsers.get(position).getCase_id());
        holder.activityIdTv.setText(canclledCasesUsers.get(position).getCase_detail_id());
        holder.activityTypeTv.setText(canclledCasesUsers.get(position).getActivity_type());
        holder.applicantNameTv.setText(canclledCasesUsers.get(position).getApplicant_first_name());

        holder.rowCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caseSelected.selectedNewCase(holder.getAdapterPosition(),canclledCasesUsers.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return canclledCasesUsers.size();
    }

    public interface CaseSelected{

        void selectedNewCase(int position, CanclledCasesUser canclledCasesUser);
    }

    public CancelledCasesAdapter(Context context, ArrayList<CanclledCasesUser> canclledCasesUsers,CaseSelected caseSelected) {
        this.context = context;
        this.canclledCasesUsers = canclledCasesUsers;
        this.caseSelected = caseSelected;
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView caseIdTv,activityIdTv,activityTypeTv,applicantNameTv;
        private CardView rowCard;

        public ViewHolder(View itemView) {
            super(itemView);
            caseIdTv = itemView.findViewById(R.id.caseid_textview);
            activityIdTv = itemView.findViewById(R.id.casedetailid_textview);
            //activityIdHeaderTv = itemView.findViewById(R.id.casedetailidhead_textview);
            activityTypeTv = itemView.findViewById(R.id.activitytype_textview);
            applicantNameTv = itemView.findViewById(R.id.applicantname_textview);
            rowCard = itemView.findViewById(R.id.row_card);
        }
    }
}
