package com.example.g2_nirav_sridheepan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g2_nirav_sridheepan.OnRowClicked;
import com.example.g2_nirav_sridheepan.OnRowClickedHires;
import com.example.g2_nirav_sridheepan.databinding.CustomRowLayoutBinding;
import com.example.g2_nirav_sridheepan.databinding.CustomRowLayoutHiresBinding;
import com.example.g2_nirav_sridheepan.models.Hire;
import com.example.g2_nirav_sridheepan.models.Tasker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class HiresAdapter extends RecyclerView.Adapter<HiresAdapter.MyViewHolder> {

    private final Context context;
    private ArrayList<Hire> dataSourceArray;
    CustomRowLayoutBinding binding;
    private final OnRowClickedHires clickListener;

    public HiresAdapter(Context context, ArrayList<Hire> hires, OnRowClickedHires clickListener){
        this.dataSourceArray = hires;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(CustomRowLayoutHiresBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Hire item = dataSourceArray.get(position);
        holder.bind(context, item, clickListener);
    }

    @Override
    public int getItemCount() {
        Log.d("MyAdapter", "getItemCount: Number of items " +this.dataSourceArray.size() );
        return this.dataSourceArray.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CustomRowLayoutHiresBinding itemBinding;

        public MyViewHolder(CustomRowLayoutHiresBinding binding){
            super(binding.getRoot());
            this.itemBinding = binding;
        }

        public void bind(Context context, Hire hire, OnRowClickedHires clickListener){
            itemBinding.tvName.setText("Name: "+hire.getTasker_name());
            itemBinding.tvTask.setText("Task: "+hire.getTasker_type());
            itemBinding.tvDate.setText("Date: "+hire.getHireDate());
            itemBinding.tvTime.setText("Time: "+hire.getHireTime());
            Picasso.get().load(hire.getTasker_imageURL())
                    .centerCrop()
                    .resize(120, 120)
                    .transform(new CropCircleTransformation())
                    .onlyScaleDown()
                    .into(itemBinding.imgThumb);

            itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onHireRowClicked(hire);
                }
            });
        }
    }
}

