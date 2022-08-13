package com.example.g2_nirav_sridheepan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g2_nirav_sridheepan.OnRowClicked;
import com.example.g2_nirav_sridheepan.databinding.CustomRowLayoutBinding;
import com.example.g2_nirav_sridheepan.models.Tasker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class TaskerAdapter extends RecyclerView.Adapter<TaskerAdapter.MyViewHolder> {

    private final Context context;
    //private final ArrayList<Tasker> dataSourceArray;
    private ArrayList<Tasker> dataSourceArray;
    CustomRowLayoutBinding binding;
    private final OnRowClicked clickListener;

    public TaskerAdapter(Context context, ArrayList<Tasker> taskers, OnRowClicked clickListener){
        this.dataSourceArray = taskers;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(CustomRowLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Tasker item = dataSourceArray.get(position);
        holder.bind(context, item, clickListener);
    }

    @Override
    public int getItemCount() {
        Log.d("MyAdapter", "getItemCount: Number of items " +this.dataSourceArray.size() );
        return this.dataSourceArray.size();
    }

    public void filteredList(ArrayList<Tasker> filteredList){
        dataSourceArray = filteredList;
        notifyDataSetChanged();
    }

    public void sortedList(ArrayList<Tasker> sortedList){
        dataSourceArray = sortedList;
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CustomRowLayoutBinding itemBinding;

        public MyViewHolder(CustomRowLayoutBinding binding){
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Context context, Tasker currTasker, OnRowClicked clickListener){
            itemBinding.tvName.setText(currTasker.getName());
            itemBinding.tvTask.setText(currTasker.getTask());
            Picasso.get().load(currTasker.getImage_url())
                    .centerCrop()
                    .resize(120, 120)
                    .transform(new CropCircleTransformation())
                    .onlyScaleDown()
                    .into(itemBinding.imgThumb);

            itemBinding.tvDistance.setText(String.valueOf( String.format("%.1f",currTasker.getDistance()) + " km" ));

            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onRowClicked(currTasker);
                }
            });
        }
    }
}

