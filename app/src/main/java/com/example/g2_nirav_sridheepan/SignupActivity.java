package com.example.g2_nirav_sridheepan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.g2_nirav_sridheepan.databinding.ActivitySignupBinding;
import com.example.g2_nirav_sridheepan.db.MyDatabase;
import com.example.g2_nirav_sridheepan.models.User;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    private MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.db = MyDatabase.getDatabase(getApplicationContext());

        // click handlers
        TextView signUp = this.binding.tvSignIn;
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignIn();
            }
        });

        this.binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = binding.etName.getText().toString();
                String emailAddress = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();

                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
                String formattedDate = df.format(date);

                // Create new user
                if (validateSignup(fullName, emailAddress, password)) {
                    User user = new User(fullName, emailAddress, password , formattedDate);
                    db.userDAO().insert(user);

                    // Navigate to Sign in
                    showSuccess();
                }
            }
        });
    }

    public void goToSignIn(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public boolean isExistingUser(String emailAddress) {
        boolean exists = false;

        List<User> users = db.userDAO().getAllUsers();
        Log.d("SignupActivity", users.toString());

        for (User user:users) {
            if (user.getEmail_id().equals(emailAddress)) {
                exists = true;
            }
        }

        return exists;
    }

    private boolean validateSignup(String fullName, String emailAddress, String password) {
        boolean isValid = false;

        if(emailAddress.isEmpty() && password.isEmpty() && fullName.isEmpty()){
            binding.tvError.setText("All Fields are mandatory");
            binding.etName.setError("Name cannot be empty");
            binding.etUsername.setError("Username cannot be empty");
            binding.etPassword.setError("Password cannot be empty");
        }
        else if(emailAddress.isEmpty() && password.isEmpty()){
            binding.tvError.setText("Username/Pasword is mandatory");
            binding.etUsername.setError("Username cannot be empty");
        }
        else if(emailAddress.isEmpty()){
            binding.tvError.setText("Username is mandatory");
            binding.etUsername.setError("Username cannot be empty");
        }
        else if(password.isEmpty()){
            binding.tvError.setText("Password is mandatory");
            binding.etPassword.setError("Password cannot be empty");
        }
        else if(fullName.isEmpty()){
            binding.tvError.setText("Name is mandatory");
            binding.etPassword.setError("Name cannot be empty");
        }
        else if (isExistingUser(emailAddress)){
            binding.tvError.setText("User already exists");
        }
        else {
            isValid = true;
        }

        return isValid;
    }

    private void showSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);

        builder.setTitle("Account Created");
        builder.setMessage("Please login to continue...!");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                goToSignIn();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}