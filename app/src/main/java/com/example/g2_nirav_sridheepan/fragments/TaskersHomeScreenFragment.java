package com.example.g2_nirav_sridheepan.fragments;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.g2_nirav_sridheepan.OnRowClicked;
import com.example.g2_nirav_sridheepan.adapter.TaskerAdapter;
import com.example.g2_nirav_sridheepan.databinding.FragmentTaskersHomeScreenBinding;
import com.example.g2_nirav_sridheepan.helpers.LocationHelper;
import com.example.g2_nirav_sridheepan.models.Tasker;
import com.example.g2_nirav_sridheepan.models.TaskerContainer;
import com.example.g2_nirav_sridheepan.network.RetrofitClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TaskersHomeScreenFragment extends Fragment implements OnRowClicked {
    private FragmentTaskersHomeScreenBinding binding;
    private String TAG = this.getClass().getCanonicalName();

    //Location start
    private LocationHelper locationHelper;
    private Location lastLocation;
    private LocationCallback locationCallback;
    //Location end



    // define the data source for the adapter
    private ArrayList<Tasker> taskerList = new ArrayList<Tasker>();
    private TaskerAdapter adapter;

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentTaskersHomeScreenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        this.locationHelper = LocationHelper.getInstance();
        this.locationHelper.checkPermissions(view.getContext());

        this.locationHelper.getLastLocation(view.getContext()).observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if (location != null){
                    lastLocation = location;

                    Address obtainedAddress = locationHelper.performForwardGeocoding(view.getContext(), lastLocation);

                    if (obtainedAddress != null){
                        binding.tvCurrentLocation.setText(obtainedAddress.getAddressLine(0));
                    }else{
                        binding.tvCurrentLocation.setText(lastLocation.toString());
                    }
                }else{
                    binding.tvCurrentLocation.setText("Last location not obtained");
                }
            }
        });














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
        this.adapter = new TaskerAdapter(view.getContext(), this.taskerList, this::onRowClicked);

        // configure the recyclerview to use the adapter
        binding.rvTasker.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.rvTasker.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        binding.rvTasker.setAdapter(this.adapter);

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        //Function to call API
        this.getAllTaskers(view);
    }

    private void filter(String searchString){
        ArrayList<Tasker> filteredTaskerList = new ArrayList<>();

        for (Tasker item : taskerList){
            if(item.getName().toLowerCase().contains(searchString.toLowerCase())){
                filteredTaskerList.add(item);
            }
            if(item.getTask().toLowerCase().contains(searchString.toLowerCase())){
                filteredTaskerList.add(item);
            }
        }
        adapter.filteredList(filteredTaskerList);
    }

    private void getAllTaskers(View view){
        Call<TaskerContainer> taskerCall = RetrofitClient.getInstance().getApi().getTasker();

        try{
            taskerCall.enqueue(new Callback<TaskerContainer>() {
                @Override
                public void onResponse(Call<TaskerContainer> call, Response<TaskerContainer> response) {
                    Log.d("Nirav", "in onResponse");
                    if (response.code() == 200){
                        if (response.body() != null) {
                            Log.d(TAG, "onResponse: " + response.body().getTaskersList());

                            ArrayList<Tasker> taskers = response.body().getTaskersList();
                            Log.d(TAG, "onResponse: Number of taskers received : " + taskers.size());


                            taskerList.clear();
                            taskerList.addAll(taskers);

                            initiateLocationListener(view);

                            //notify the adapter of data change
                            adapter.notifyDataSetChanged();
                        }else{
                            Log.d(TAG, "onResponse: Null response received");
                        }
                    }else{
                        Log.d(TAG, "onResponse: Unsuccessful response received " + response.code() );
                    }

                    call.cancel();
                }

                @Override
                public void onFailure(Call<TaskerContainer> call, Throwable t) {
                    Log.d("Nirav", "in onFailure");
                    call.cancel();
                    Log.d(TAG, "onFailure: Failed to get tasker " + t.getLocalizedMessage() );
                }
            });
        }catch (Exception ex){
            Log.d(TAG, "getTasker: Cannot retrieve tasker" + ex.getLocalizedMessage() );
        }
    }


    @Override
    public void onRowClicked(Tasker currTasker) {
        NavDirections action = TaskersHomeScreenFragmentDirections.actionTaskersHomeScreenFragmentToTaskerDetailScreenFragment(currTasker);
        Navigation.findNavController(getView()).navigate(action);
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onResume");
        this.locationHelper.stopLocationUpdates(getView().getContext(), this.locationCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume");
        this.locationHelper.getLocationUpdates(getView().getContext(), this.locationCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onResume");
        this.locationHelper.stopLocationUpdates(getView().getContext(), this.locationCallback);
    }

    private void initiateLocationListener(View view){
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location loc : locationResult.getLocations()){
                    lastLocation = loc;

                    Log.d(TAG, "onLocationResult: updated location : " + lastLocation.toString() );

                    Address obtainedAddress = locationHelper.performForwardGeocoding(view.getContext(), lastLocation);

                    if (obtainedAddress != null){
                        binding.tvCurrentLocation.setText(obtainedAddress.getAddressLine(0));
                        appendDistanceToUser(lastLocation);
                    }else{
                        binding.tvCurrentLocation.setText(lastLocation.toString());
                        appendDistanceToUser(lastLocation);
                    }
                }
            }
        };

        this.locationHelper.getLocationUpdates(view.getContext(), locationCallback);
    }

    public void appendDistanceToUser(Location loc){
        Log.d(TAG, "appendDistanceToUser: current Latitude: " + loc.getLatitude() );
        Log.d(TAG, "appendDistanceToUser: current Longitude: " + loc.getLongitude() );
        Double currentLatitude = loc.getLatitude();
        Double currentLongitude = loc.getLongitude();

        ArrayList<Tasker> tempArray = new ArrayList<>();

        for (Tasker curTasker : taskerList) {
            Double taskerLatitude = Double.parseDouble(curTasker.getLatitude());
            Double taskerLongitude = Double.parseDouble(curTasker.getLongitude());
            Double distance = calculateDistance(currentLatitude, currentLongitude, taskerLatitude, taskerLongitude);


            curTasker.setDistance(distance);

            tempArray.add(curTasker);
        }

        Collections.sort(tempArray, new Comparator<Tasker>() {
            @Override
            public int compare(Tasker tasker, Tasker t1) {
                return Double.compare(tasker.getDistance(), t1.getDistance());
            }
        });

        this.taskerList = tempArray;
        adapter.sortedList(tempArray);
    }














    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}