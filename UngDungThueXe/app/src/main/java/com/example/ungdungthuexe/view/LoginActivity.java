package com.example.ungdungthuexe.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ungdungthuexe.R;
import com.example.ungdungthuexe.databinding.ActivityLoginBinding;
import com.example.ungdungthuexe.model.LoginResponse;
import com.example.ungdungthuexe.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UngDungThueXe";
    private static final String KEY_MA_KH = "maKH";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Khởi tạo ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Setup observers
        observeLoginResponse();
        observeError();
        observeLoading();

        // Setup button click
        binding.btnLogin.setOnClickListener(v -> {
            performLogin();
        });

        // Setup register link click
        binding.tvRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void performLogin() {
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            binding.tilUsername.setError("Vui lòng nhập tên đăng nhập");
            return;
        } else {
            binding.tilUsername.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("Vui lòng nhập mật khẩu");
            return;
        } else {
            binding.tilPassword.setError(null);
        }

        loginViewModel.login(username, password);
    }

    private void observeLoginResponse() {
        loginViewModel.getLoginResponse().observe(this, response -> {
            if (response != null) {
                // Lưu thông tin vào SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(KEY_MA_KH, response.getMaKH());
                editor.putString(KEY_TOKEN, response.getToken());
                editor.putString(KEY_USERNAME, response.getUsername());
                editor.apply();
                
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                
                // Chuyển sang màn hình chính
                Intent intent = new Intent(this, CustomerHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void observeError() {
        loginViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeLoading() {
        loginViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(android.view.View.VISIBLE);
                binding.btnLogin.setEnabled(false);
            } else {
                binding.progressBar.setVisibility(android.view.View.GONE);
                binding.btnLogin.setEnabled(true);
            }
        });
    }
}

