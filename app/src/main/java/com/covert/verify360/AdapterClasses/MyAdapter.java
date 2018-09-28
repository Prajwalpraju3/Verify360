package com.covert.verify360.AdapterClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.covert.verify360.R;

import java.io.File;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> list = new ArrayList<>();
    private Context context;

    public MyAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    public void addImages(ArrayList<String> list) {
//        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addImage(String path){
        list.add(path);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.image_row, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Uri imageUri = Uri.fromFile(new File(list.get(position)));// For files on device
        //Log.e("hello", "- " + imageUri.toString());

        /*File f = new File(list.get(position));
        Bitmap d = new BitmapDrawable(context.getResources(), f.getAbsolutePath()).getBitmap();
        //Bitmap scaled = com.fxn.utility.Utility.getScaledBitmap(512, com.fxn.utility.Utility.getExifCorrectedBitmap(f));
        Bitmap scaled = com.fxn.utility.Utility.getScaledBitmap(150, d);
        ((Holder) holder).iv.setImageBitmap(scaled);
        // ((Holder) holder).iv.setImageURI(imageUri);*/

        ((Holder) holder).remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        System.out.println("Mayur: path " + position + " " + list.get(position));
        Glide.with(context).load(list.get(position)).into(((Holder) holder).iv);
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public ImageView iv, remove;
        public Holder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.image_view);
            remove = itemView.findViewById(R.id.remove);
        }
    }
}
