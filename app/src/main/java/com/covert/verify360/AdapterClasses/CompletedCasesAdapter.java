package com.covert.verify360.AdapterClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.covert.verify360.BeanClasses.CompCasesUser;
import com.covert.verify360.BeanClasses.NewCasesBean;
import com.covert.verify360.R;

import java.util.ArrayList;

public class CompletedCasesAdapter extends RecyclerView.Adapter<CompletedCasesAdapter.ViewHolder> {

    Context context;
    ArrayList<CompCasesUser> compCasesUsers;
    CaseSelected caseSelected;

    public interface CaseSelected{

        void selectedNewCase(int position, CompCasesUser newCasesBean);
    }


    public CompletedCasesAdapter(Context context, ArrayList<CompCasesUser> compCasesUsers,CaseSelected caseSelected) {
        this.context = context;
        this.compCasesUsers = compCasesUsers;
        this.caseSelected = caseSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modified_completed_cases_row, parent, false);
        return new CompletedCasesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.caseIdTv.setText(compCasesUsers.get(position).getCase_id());
        holder.activityIdTv.setText(compCasesUsers.get(position).getCase_detail_id());
        holder.activityTypeTv.setText(compCasesUsers.get(position).getActivity_type());
        holder.applicantNameTv.setText(compCasesUsers.get(position).getApplicant_first_name());
        holder.CompleteDate.setText(compCasesUsers.get(position).getCompleted_date());
        holder.Priority.setText(compCasesUsers.get(position).getPriority());
        holder.CompletedTAT.setText(compCasesUsers.get(position).getCompleted_in_tat());

        holder.rowCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                caseSelected.selectedNewCase(holder.getAdapterPosition(),compCasesUsers.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return compCasesUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView caseIdTv,activityIdTv,activityTypeTv,applicantNameTv, CompleteDate, Priority, CompletedTAT;
        private CardView rowCard;

        public ViewHolder(View itemView) {
            super(itemView);
            caseIdTv = itemView.findViewById(R.id.caseid_textview);
            activityIdTv = itemView.findViewById(R.id.casedetailid_textview);
            //activityIdHeaderTv = itemView.findViewById(R.id.casedetailidhead_textview);
            activityTypeTv = itemView.findViewById(R.id.activitytype_textview);
            applicantNameTv = itemView.findViewById(R.id.applicantname_textview);
            CompleteDate= itemView.findViewById(R.id.completed_date);
            Priority= itemView.findViewById(R.id.priority);
            CompletedTAT= itemView.findViewById(R.id.completed_tat);

            rowCard = itemView.findViewById(R.id.row_card);
        }
    }
}
