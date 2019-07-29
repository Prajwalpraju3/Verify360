package com.covert.verify360.AdapterClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.covert.verify360.BeanClasses.FormElementDatum;
import com.covert.verify360.BeanClasses.InnerSubSection;
import com.covert.verify360.BeanClasses.Items;
import com.covert.verify360.R;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

public class MainSectionAdapter extends RecyclerView.Adapter<MainSectionAdapter.ViewHolder> {
    private Context context;
    private List<FormElementDatum> formElementDatumList;
    private List<Items> items;
    OnMainClick onMainClick;

    public MainSectionAdapter(Context context, List<FormElementDatum> formElementDatumList, OnMainClick onMainClick) {
        this.context = context;
        this.formElementDatumList = formElementDatumList;
        items = new ArrayList<>();
        this.onMainClick = onMainClick;
    }

    @NonNull
    @Override
    public MainSectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder view;
        view = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_main_section, parent, false));
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull MainSectionAdapter.ViewHolder holder, int position) {
        holder.main_section.setText(formElementDatumList.get(position).getMainSection());
//        final int length = formElementDatumList.get(position).getOuterSubSection().size();
        Toast.makeText(context, formElementDatumList.get(position).getIs_multiple(), Toast.LENGTH_SHORT).show();


        InnerSubSecAdapter adapter = new InnerSubSecAdapter(context,
                formElementDatumList.get(position).getOuterSubSection(), formElementDatumList.get(position).getIs_multiple(), new InnerSubSecAdapter.OnSubClick() {
            @Override
            public void onItemChange(int pos) {
                ArrayList<Boolean> booleanArrayList = new ArrayList<>();
                List<InnerSubSection> innerSubSections = formElementDatumList.get(position).getOuterSubSection();
                for (int i = 0; i < innerSubSections.size(); i++) {
                    if (innerSubSections.get(i).isMandatory() && innerSubSections.get(i).isHavedata()) {
                        booleanArrayList.add(true);
                    } else if (innerSubSections.get(i).isMandatory() && !innerSubSections.get(i).isHavedata()) {
                        booleanArrayList.add(false);
                    } else {
                        booleanArrayList.add(true);
                    }
                }

                if (booleanArrayList.contains(false)) {
                    Log.d("ttttt", "onItemChange:  contains false");
                    formElementDatumList.get(position).setVaidated(false);
                    onMainClick.onChange(position,false);
                } else {
                    Log.d("ttttt", "onItemChange:  contains true");
                    formElementDatumList.get(position).setVaidated(true);
                    onMainClick.onChange(position,true);
                }


            }
        });


        holder.sub_sec_outer_list.setLayoutManager(new LinearLayoutManager(context));
        holder.sub_sec_outer_list.setNestedScrollingEnabled(false);
        holder.sub_sec_outer_list.setAdapter(adapter);

        // TODO: 9/20/2018 Connect next adapter here for sub section

        /*for (int j = 0; j < length; j++) {
            if (formElementDatumList.get(position).getOuterSubSection().get(j).getOuterSubSection().equals("") ||
                    formElementDatumList.get(position).getOuterSubSection().get(j).getOuterSubSection() == null) {
                holder.sub_section.setVisibility(View.GONE);
            } else {
                holder.sub_section.setVisibility(View.VISIBLE);
                holder.sub_section.setText(formElementDatumList.get(position).getOuterSubSection().get(j).getOuterSubSection());
            }

            // TODO: 9/19/2018 Held radio recyclerview Adapter here

            final int len = formElementDatumList.get(position).getOuterSubSection().get(j).getOptionssection().size();
            final RadioButton[] radioGroup = new RadioButton[len];
            holder.options.setOrientation(RadioGroup.VERTICAL);
            for (int i = 0; i < len; i++) {
                radioGroup[i] = new RadioButton(context);
                radioGroup[i].setText((formElementDatumList.get(position).getOuterSubSection().get(j).getOptionssection().get(i).getOptions()));
                radioGroup[i].setId(((formElementDatumList.get(position).getOuterSubSection().get(j).getOptionssection().get(i).getFormElementId())));
                holder.options.addView(radioGroup[i]);
            }
        }*/

    }

    @Override
    public int getItemCount() {
        if (formElementDatumList == null) return 0;
        return formElementDatumList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView main_section, sub_section;
        //        private RadioGroup options;
        private RecyclerView sub_sec_outer_list;

        public ViewHolder(View itemView) {
            super(itemView);
            main_section = itemView.findViewById(R.id.main_section);
            sub_section = itemView.findViewById(R.id.sub_section);
            sub_sec_outer_list = itemView.findViewById(R.id.sub_sec_outer_list);

//            options=itemView.findViewById(R.id.radioGroupOptions);
//            options.setOnCheckedChangeListener((group, checkedId) -> {
//               items.add(new Items(checkedId,sub_section_remarks.getText().toString()));
//            });

        }
    }

    public interface OnMainClick{
        void onChange(int pos,boolean value);
    }

    public List<Items> getItems() {
        return items;
    }

    public List<FormElementDatum> getFormElementDatumList() {
        return formElementDatumList;
    }

    public void clearItems() {
        items.clear();
    }
}
