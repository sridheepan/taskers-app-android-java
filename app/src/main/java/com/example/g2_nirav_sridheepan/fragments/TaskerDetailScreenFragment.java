package com.example.g2_nirav_sridheepan.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.g2_nirav_sridheepan.TaskersActivity;
import com.example.g2_nirav_sridheepan.databinding.FragmentTaskerDetailScreenBinding;
import com.example.g2_nirav_sridheepan.db.HireDAO;
import com.example.g2_nirav_sridheepan.db.MyDatabase;
import com.example.g2_nirav_sridheepan.db.UserDAO;
import com.example.g2_nirav_sridheepan.models.Hire;
import com.example.g2_nirav_sridheepan.models.Tasker;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class TaskerDetailScreenFragment extends Fragment {
    private FragmentTaskerDetailScreenBinding binding;
    private SharedPreferences prefs;
    private MyDatabase db;
    private final String PROMOCODE = "CANADA20";
    private HireDAO hireDAO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initiaze the database and get the dao
        this.hireDAO = MyDatabase.getDatabase(getContext()).hireDAO();
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentTaskerDetailScreenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String format = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);
                binding.etDate.setText(sdf.format(calendar.getTime()));
            }

        };

        this.binding.etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis()+24*60*60*1000);
                dialog.show();
            }
        });

        this.binding.etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        binding.etTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                dialog.setTitle("Select Time");
                dialog.show();
            }
        });

        this.binding.btnHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HireClick", binding.etDate.getText().toString() + " " + binding.etTime.getText().toString());

                if (validateData()){
                    confirmHire();
                }
            }
        });

        this.binding.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String promocode = binding.etPromocode.getText().toString();
                binding.tvError.setText("");
                if (promocode.equals("") || (!promocode.equals(PROMOCODE))){
                    binding.tvError.setText("Invalid Promocode");
                }
                else {
                    Toast.makeText(getActivity(), "Promocode Applied", Toast.LENGTH_SHORT).show();
                    binding.tvApply.setText("Applied");
                    binding.tvApply.setEnabled(false);
                    binding.etPromocode.setEnabled(false);

                    Tasker receivedTasker = TaskerDetailScreenFragmentArgs.fromBundle(getArguments()).getTasker();

                    double discountedRate = receivedTasker.getRate() - (receivedTasker.getRate() * 0.2);
                    binding.btnHire.setText("Hire for $" + Math.round(discountedRate));
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

        Tasker receivedTasker = TaskerDetailScreenFragmentArgs.fromBundle(getArguments()).getTasker();
        Picasso.get().load(receivedTasker.getImage_url())
                .centerCrop()
                .resize(500, 500)
                .transform(new CropCircleTransformation())
                .onlyScaleDown()
                .into(binding.imgTasker);
        binding.tvNameAge.setText(receivedTasker.getName() +", "+ receivedTasker.getAge() );
        binding.tvTask.setText(receivedTasker.getTask());
        binding.btnHire.setText("Hire for $" + receivedTasker.getRate());

    }

    private boolean validateData() {
        boolean isValid = false;
        String selectedDate = binding.etDate.getText().toString();
        String selectedTime = binding.etTime.getText().toString();

        binding.tvError.setText(""); // clear previous errors from screen

        if (selectedDate.equals("") && selectedTime.equals("")) {
            binding.tvError.setText("Please select Date/Time");
        }
        else if (selectedDate.equals("")) {
            binding.tvError.setText("Please select a Date");
        }
        else if (selectedTime.equals("")) {
            binding.tvError.setText("Please select Time");
        }
        else {
            isValid = true;
        }

        return isValid;
    }

    private void placeHire() {
        Tasker receivedTasker = TaskerDetailScreenFragmentArgs.fromBundle(getArguments()).getTasker();
        this.prefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        int userID = this.prefs.getInt("loggedInUserId", 0);
        String taskerName = receivedTasker.getName();
        String taskerType = receivedTasker.getTask();
        String taskerImageURL = receivedTasker.getImage_url();
        String hireDate = binding.etDate.getText().toString();
        String hireTime = binding.etTime.getText().toString();
        String promocode = binding.etPromocode.getText().toString();
        boolean isPromoHire = binding.tvApply.getText().toString().equals("Applied");

        int amount = Integer.parseInt(binding.btnHire.getText().toString().split("\\$", 2)[1]);

        Hire hire = new Hire(taskerName, taskerType, taskerImageURL, userID, hireDate, hireTime, promocode, isPromoHire, amount);
        hireDAO.insert(hire);

        Log.d("new hire created", hire.toString());
        Toast.makeText(getActivity(), "Your hire has been received.", Toast.LENGTH_SHORT).show();

        showSuccess(); // show success and goto main screen
    }

    private void confirmHire() {
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Confirm Hire!");
            builder.setMessage("Do you want to confirm this hire ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    placeHire();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private void showSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Success");
        builder.setMessage("Hire updated successfully!");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                goToHomeScreen();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void goToHomeScreen() {

        Intent intent = new Intent(getActivity().getApplicationContext(), TaskersActivity.class);
        startActivity(intent);
    }


}