package com.example.g2_nirav_sridheepan.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.g2_nirav_sridheepan.R;
import com.example.g2_nirav_sridheepan.TaskersActivity;
import com.example.g2_nirav_sridheepan.adapter.TaskerAdapter;
import com.example.g2_nirav_sridheepan.databinding.FragmentHireDetailBinding;
import com.example.g2_nirav_sridheepan.databinding.FragmentMyHiresBinding;
import com.example.g2_nirav_sridheepan.db.HireDAO;
import com.example.g2_nirav_sridheepan.db.MyDatabase;
import com.example.g2_nirav_sridheepan.models.Hire;
import com.example.g2_nirav_sridheepan.models.Tasker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HireDetailFragment extends Fragment {
    private FragmentHireDetailBinding binding;
    private Hire hire;
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
        binding = FragmentHireDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        this.hire =  HireDetailFragmentArgs.fromBundle(getArguments()).getSelectedHire();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            // hire date
            Date date = formatter.parse(hire.getHireDate() + " " +hire.getHireTime() +":00");
            Log.d("date", String.valueOf(date));

            //system date
            Date sysDate = new Date();
            long diff = date.getTime() - sysDate.getTime();
            long diffHours = diff / (60 * 60 * 1000);

            if (diffHours <= 24) {
                binding.etDate.setVisibility(View.INVISIBLE);
                binding.etTime.setVisibility(View.INVISIBLE);
                binding.btnUpdateSchedule.setVisibility(View.INVISIBLE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

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
                String dateInString = hire.getHireDate();
                String[] dateArr = dateInString.split("-");

                calendar.set(Integer.parseInt(dateArr[2]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[0]));

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis()+24*60*60*1000);
                dialog.show();
            }
        });

        this.binding.etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateInString = hire.getHireTime();
                String[] dateArr = dateInString.split(":");
                int hour = Integer.parseInt(dateArr[0]);
                int minute = Integer.parseInt(dateArr[1]);
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

        this.binding.btnUpdateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmUpdate();
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

        binding.tvName.setText(this.hire.getTasker_name());
        binding.tvTask.setText(this.hire.getTasker_type());
        binding.tvScheduledDate.setText(this.hire.getHireDate());
        binding.tvScheduledTime.setText(this.hire.getHireTime());
        binding.tvPromoUsed.setText(String.valueOf(this.hire.isPromoHire()));
        binding.tvPromocode.setText(this.hire.getPromocode().equals("") ? "--" : this.hire.getPromocode());
        if (this.hire.isPromoHire()){
            int original_price = (this.hire.getTotalAmount()*100) / 80;
            binding.tvDiscount.setText("$"+String.valueOf(Math.round(original_price*0.2)));
        } else {
            binding.tvDiscount.setText("--");
        }
        binding.tvAmountPaid.setText("$"+String.valueOf(this.hire.getTotalAmount()));
        binding.etDate.setText(this.hire.getHireDate());
        binding.etTime.setText(this.hire.getHireTime());

    }

    private void confirmUpdate() {
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Confirm Update!");
            builder.setMessage("Do you want to update this hire ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    hire.setHireDate(binding.etDate.getText().toString());
                    hire.setHireTime(binding.etTime.getText().toString());

                    hireDAO.update(hire);

                    showSuccess();
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
                dialog.cancel();
                Toast.makeText(getActivity(), "Your hire has been updated.", Toast.LENGTH_SHORT).show();

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