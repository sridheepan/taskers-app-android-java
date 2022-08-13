package com.example.g2_nirav_sridheepan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.g2_nirav_sridheepan.databinding.ActivityLoginBinding;
import com.example.g2_nirav_sridheepan.db.MyDatabase;
import com.example.g2_nirav_sridheepan.models.User;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.prefs = getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE);
        this.editor = this.prefs.edit();
        this.db = MyDatabase.getDatabase(getApplicationContext());

        TextView signUp = this.binding.tvSignup;
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUp();
            }
        });

//        editor.putBoolean("saveLogin", false);
//        editor.commit();

        Switch ui_switch = this.binding.swRemember;
        Boolean saveLoginSwitch = prefs.getBoolean("saveLogin", false);
        ui_switch.setChecked(saveLoginSwitch);

        if (ui_switch.isChecked()){
            goToHomeScreen();
        } else {
            this.binding.btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("LoginActivity", "onClick: login button pressed");

                    String userName =   binding.etUsername.getText().toString();
                    String password =   binding.etPassword.getText().toString();

                    if(validateLogin(userName, password)) {
                        prefs.edit();
                        editor.putBoolean("saveLogin", ui_switch.isChecked());
                        editor.commit();

                        ui_switch.setChecked(ui_switch.isChecked());

                        goToHomeScreen();
                    }
                }
            });
        }
    }

    public void goToSignUp(){
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(getApplicationContext(), TaskersActivity.class);
        startActivity(intent);
    }


    public boolean validateLogin(String emailAddress, String password){
        boolean isValid = false;
        Log.d("LoginActivity", "validateLogin: 1");
        if(emailAddress.isEmpty() && password.isEmpty()){
            binding.tvError.setText("Username/Password is mandatory");
            binding.etUsername.setError("Username cannot be empty");
            binding.etPassword.setError("Password cannot be empty");
        }
        else if(emailAddress.isEmpty()){
            binding.tvError.setText("Username is mandatory");
            binding.etUsername.setError("Username cannot be empty");
        }
        else if(password.isEmpty()){
            binding.tvError.setText("Password is mandatory");
            binding.etPassword.setError("Password cannot be empty");
        }
        else {
            // search user in db
            User user = db.userDAO().getUser(emailAddress, password);
            if (user != null) {
                Log.d("LoginActivity", user.toString());
                editor.putInt("loggedInUserId", user.getId()).apply();
                isValid = true;
                clearFields();
            } else {
                clearFields();
                binding.tvError.setText("Username/Password combination is incorrect");
            }

        }

        return isValid;
    }

    public void clearFields(){
        binding.etUsername.setText("");
        binding.etPassword.setText("");
    }

}