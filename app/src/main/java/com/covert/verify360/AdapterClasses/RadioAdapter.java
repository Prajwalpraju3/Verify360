package com.covert.verify360.AdapterClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.covert.verify360.BeanClasses.Optionssection;
import com.covert.verify360.R;

import java.util.List;

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {

    private Context context;
    private List<Optionssection> list;
    private OnRadioClick onRadioClick;

    public RadioAdapter(Context context, List<Optionssection> list, OnRadioClick onRadioClick) {
        this.context = context;
        this.list = list;
        this.onRadioClick = onRadioClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_radio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btn_radio.setText(list.get(position).getOptions());
        holder.btn_radio.setChecked(list.get(position).isSelected());
        holder.btn_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = ((CompoundButton)v).isChecked();
                if (isSelected) {
                    check(position);
                    notifyDataSetChanged();
                    holder.btn_radio.clearFocus();
                    onRadioClick.onItemChange(position);
                }
            }
        });
    }

    private void check(int pos){
        for (int i = 0; i < list.size(); i++) {
            if (i == pos){
                list.get(i).setSelected(true);
            } else {
                list.get(i).setSelected(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        else return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RadioButton btn_radio;
        public ViewHolder(View itemView) {
            super(itemView);
            btn_radio = itemView.findViewById(R.id.btn_radio);
        }
    }

    public interface OnRadioClick{
        void onItemChange(int pos);
    }
}
