package com.example.g2_nirav_sridheepan.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.g2_nirav_sridheepan.LoginActivity;
import com.example.g2_nirav_sridheepan.R;
import com.example.g2_nirav_sridheepan.TaskersActivity;
import com.example.g2_nirav_sridheepan.databinding.FragmentProfileScreenBinding;
import com.example.g2_nirav_sridheepan.db.MyDatabase;
import com.example.g2_nirav_sridheepan.db.UserDAO;
import com.example.g2_nirav_sridheepan.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class ProfileScreenFragment extends Fragment {
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private Integer loggedInUserID;
    private FragmentProfileScreenBinding binding;
    private UserDAO dao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dao = MyDatabase.getDatabase(getContext()).userDAO();
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentProfileScreenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.prefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        this.editor = this.prefs.edit();
        this.loggedInUserID = this.prefs.getInt("loggedInUserId", 0);

        this.setUserDetails(this.loggedInUserID);

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("saveLogin", false).commit();
                editor.remove("loggedInUserId").commit();
                Snackbar.make(binding.getRoot(), "Button Pressed", Snackbar.LENGTH_LONG).show();
                goToLoginScreen();
            }
        });
    }

    private void setUserDetails(Integer userId){
        User user = dao.getUserById(userId);
        if (user != null) {
            Log.d("ProfileScreen", user.toString());

            binding.tvFullName.setText(user.getFullName());
            binding.tvEmailValue.setText(user.getEmail_id());

            String[] dateArray = this.getDateArray(user.getDate());
            for(int i=0; i< dateArray.length; i++){
                if(i == 0){
                    binding.tvDateValue.setText(dateArray[i]);
                }
                if(i == 1){
                    binding.tvTimeValue.setText(dateArray[i]);
                }
                else{
                    binding.tvTimeValue.setText("--");
                }
                if(i == 2){
                    binding.tvTimeValue.setText(dateArray[i-1] + " " + dateArray[i]);
                }
                else{
                    binding.tvTimeValue.setText("--");
                }
            }
        } else {
            Snackbar.make(binding.getRoot(), "Could not find user details ", Snackbar.LENGTH_LONG).show();
        }
    }

    private String[] getDateArray(String date) {
        String[] splitedDate = date.split("\\s+");
        return  splitedDate;
    }

    private void goToLoginScreen() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}