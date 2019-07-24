package com.covert.verify360.AdapterClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.covert.verify360.BeanClasses.InnerSubSection;
import com.covert.verify360.R;

import java.util.List;

public class InnerSubSecAdapter extends RecyclerView.Adapter<InnerSubSecAdapter.ViewHolder> {

    private Context context;
    private List<InnerSubSection> list;
    String mIs_multiple;

    public InnerSubSecAdapter(Context context, List<InnerSubSection> list, String is_multiple) {
        this.context = context;
        this.list = list;
        mIs_multiple = is_multiple;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sub_sec, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_sub_sec_heading.setText(list.get(position).getSubSection());

        RadioAdapter adapter = new RadioAdapter(context,mIs_multiple, list.get(position).getOptionssection(),
                new RadioAdapter.OnRadioClick() {
                    @Override
                    public void onItemChange(int pos) {
                        holder.sub_section_remarks.requestFocus();
                    }
                });
        holder.sub_sec_outer_list.setLayoutManager(new LinearLayoutManager(context));
        holder.sub_sec_outer_list.setNestedScrollingEnabled(false);
        holder.sub_sec_outer_list.setAdapter(adapter);

        holder.sub_section_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                list.get(position).setBuilder(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null)return 0;
        else return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_sub_sec_heading;
        RecyclerView sub_sec_outer_list;
        EditText sub_section_remarks;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_sub_sec_heading = itemView.findViewById(R.id.txt_sub_sec_heading);
            sub_sec_outer_list = itemView.findViewById(R.id.sub_sec_outer_list);
            sub_section_remarks = itemView.findViewById(R.id.sub_sectionRemarks);
        }
    }
}
