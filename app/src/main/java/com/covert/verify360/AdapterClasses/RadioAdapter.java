package com.covert.verify360.AdapterClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.covert.verify360.BeanClasses.Optionssection;
import com.covert.verify360.R;

import java.util.List;

public class RadioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int CHECK = 1;
    private static final int RADIO = 2;
    private Context context;
    private List<Optionssection> list;
    private OnRadioClick onRadioClick;
    String mIs_multiple;
    private long mLastClickTime = 0;

    public RadioAdapter(Context context,String is_multiple, List<Optionssection> list, OnRadioClick onRadioClick) {
        this.context = context;
        this.list = list;
        this.onRadioClick = onRadioClick;
        mIs_multiple = is_multiple;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType ==RADIO){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_radio, parent, false);
            return new RadioViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_box, parent, false);
            return new CheckBoxViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
       if (mIs_multiple.matches("Y")){
           return CHECK;
       }else {
           return RADIO;
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType()==RADIO){
            RadioViewHolder radioViewHolder = (RadioViewHolder)holder;
            radioViewHolder.btn_radio.setText(list.get(position).getOptions());
            radioViewHolder.btn_radio.setChecked(list.get(position).isSelected());
            radioViewHolder.btn_radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSelected = ((CompoundButton)v).isChecked();
                    if (isSelected) {
                        check(position);
                        notifyDataSetChanged();
                        radioViewHolder.btn_radio.clearFocus();
                        onRadioClick.onItemChange(position,RADIO);
                    }
                }
            });
        }else {
            CheckBoxViewHolder checkBoxViewHolder = (CheckBoxViewHolder)holder;
            checkBoxViewHolder.bt_check.setText(list.get(position).getOptions());
//            checkBoxViewHolder.bt_check.setChecked(list.get(position).isSelected());
            checkBoxViewHolder.bt_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSelected = list.get(position).isSelected();
                    Log.d("kkk", "onClick: isselected---->"+isSelected);
                    if (isSelected) {
                        list.get(position).setSelected(false);
                        checkBoxViewHolder.bt_check.setChecked(list.get(position).isSelected());


                    }
                    else {
                        list.get(position).setSelected(true);
                        checkBoxViewHolder.bt_check.setChecked(list.get(position).isSelected());

                    }
                    onRadioClick.onItemChange(position,CHECK);
//                    notifyDataSetChanged();
                }
            });

        }

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

    private void checkbox(int pos){
        for (int i = 0; i < list.size(); i++) {
            if (i == pos){
                list.get(i).setSelected(false);
            } else {
                list.get(i).setSelected(true);
            }
        }
    }

    private void checkboxnegative(int pos){
        for (int i = 0; i < list.size(); i++) {
            if (i == pos){
                list.get(i).setSelected(false);
            } else {
//                list.get(i).setSelected(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        else return list.size();
    }

    public class RadioViewHolder extends RecyclerView.ViewHolder{
        RadioButton btn_radio;
        public RadioViewHolder(View itemView) {
            super(itemView);
            btn_radio = itemView.findViewById(R.id.btn_radio);
        }
    }

    public class CheckBoxViewHolder extends RecyclerView.ViewHolder{
        CheckBox bt_check;
        public CheckBoxViewHolder(View itemView) {
            super(itemView);
            bt_check = itemView.findViewById(R.id.bt_check);
        }
    }

    public interface OnRadioClick{
        void onItemChange(int pos, int type);
    }
}
