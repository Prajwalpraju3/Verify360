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
    OnSubClick onSubClick;

    public InnerSubSecAdapter(Context context, List<InnerSubSection> list, String is_multiple,OnSubClick onSubClick) {
        this.context = context;
        this.list = list;
        mIs_multiple = is_multiple;
        this.onSubClick = onSubClick;

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

        RadioAdapter adapter = new RadioAdapter(context, mIs_multiple, list.get(position).getOptionssection(),
                new RadioAdapter.OnRadioClick() {
                    @Override
                    public void onItemChange(int pos) {
                        if (list.get(position).getOptionssection().get(pos).getMandatory_option().matches("Y")) {
                            list.get(position).setMandatory(true);
                            holder.tv_mandate.setVisibility(View.VISIBLE);
                        } else {
                            list.get(position).setMandatory(false);
                            holder.tv_mandate.setVisibility(View.INVISIBLE);
                        }
                        list.get(position).setCatagory_selected(true);
                        holder.sub_section_remarks.requestFocus();
                        onSubClick.onItemChange(pos);
                    }
                });


        holder.sub_sec_outer_list.setLayoutManager(new LinearLayoutManager(context));
        holder.sub_sec_outer_list.setNestedScrollingEnabled(false);
        holder.sub_sec_outer_list.setAdapter(adapter);

        holder.sub_section_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    list.get(position).setHavedata(true);
                }else {
                    list.get(position).setHavedata(false);
                }

                onSubClick.onItemChange(0);
                list.get(position).setBuilder(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        else return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_sub_sec_heading, tv_mandate;
        RecyclerView sub_sec_outer_list;
        EditText sub_section_remarks;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_sub_sec_heading = itemView.findViewById(R.id.txt_sub_sec_heading);
            sub_sec_outer_list = itemView.findViewById(R.id.sub_sec_outer_list);
            sub_section_remarks = itemView.findViewById(R.id.sub_sectionRemarks);
            tv_mandate = itemView.findViewById(R.id.tv_mandate);
        }
    }


    public interface OnSubClick{
        void onItemChange(int pos);
    }
}
