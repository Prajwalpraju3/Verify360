package com.covert.verify360.AdapterClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.covert.verify360.BeanClasses.OuterSubSec;
import com.covert.verify360.R;

import java.util.List;

public class OuterSubSecAdapter extends RecyclerView.Adapter<OuterSubSecAdapter.ViewHolder> {

    private Context context;
    private List<OuterSubSec> list;

    public OuterSubSecAdapter(Context context, List<OuterSubSec> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gen_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recycler_view.setLayoutManager(new LinearLayoutManager(context));
        holder.recycler_view.setNestedScrollingEnabled(false);
        InnerSubSecAdapter adapter = new InnerSubSecAdapter(context, list.get(position).getInnerSubSection());
        holder.recycler_view.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        if (list == null)return 0;
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recycler_view;
        public ViewHolder(View itemView) {
            super(itemView);
            recycler_view = itemView.findViewById(R.id.recycler_view);
        }
    }
}
