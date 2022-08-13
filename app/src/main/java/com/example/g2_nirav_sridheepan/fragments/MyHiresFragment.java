package com.example.g2_nirav_sridheepan.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.g2_nirav_sridheepan.OnRowClicked;
import com.example.g2_nirav_sridheepan.OnRowClickedHires;
import com.example.g2_nirav_sridheepan.adapter.HiresAdapter;
import com.example.g2_nirav_sridheepan.adapter.TaskerAdapter;
import com.example.g2_nirav_sridheepan.databinding.FragmentMyHiresBinding;
import com.example.g2_nirav_sridheepan.db.HireDAO;
import com.example.g2_nirav_sridheepan.db.MyDatabase;
import com.example.g2_nirav_sridheepan.models.Hire;
import com.example.g2_nirav_sridheepan.models.Tasker;

import java.util.ArrayList;
import java.util.List;


public class MyHiresFragment extends Fragment implements OnRowClickedHires {
    private FragmentMyHiresBinding binding;
    private SharedPreferences prefs;

    private ArrayList<Hire> hiresList = new ArrayList<Hire>();
    private HiresAdapter adapter;
    private MyDatabase db;
    private HireDAO hireDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initiaze the database and get hte dao
        this.hireDAO = MyDatabase.getDatabase(getContext()).hireDAO();
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentMyHiresBinding.inflate(inflater, container, false);
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
        // create an instance of the adapter
        this.adapter = new HiresAdapter(view.getContext(), this.hiresList, this::onHireRowClicked);

        // configure the recyclerview to use the adapter
        binding.rvHires.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.rvHires.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        binding.rvHires.setAdapter(this.adapter);

        //Function to call API
        this.getAllHires();
        this.adapter.notifyDataSetChanged();
    }

    private void getAllHires() {
        this.prefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        int loggedInUserID = this.prefs.getInt("loggedInUserId", 0);

        List<Hire> hiresFromDb = hireDAO.getAllHires(loggedInUserID);
        hiresList.clear();
        hiresList.addAll(hiresFromDb);
    }

    @Override
    public void onHireRowClicked(Hire selectedHire) {
        NavDirections action = MyHiresFragmentDirections.actionMyHiresFragment3ToHireDetailFragment(selectedHire);
        Navigation.findNavController(getView()).navigate(action);
    }
}