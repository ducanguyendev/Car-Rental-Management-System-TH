package com.example.ungdungthuexe.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ungdungthuexe.databinding.ActivityRegisterBinding;
import com.example.ungdungthuexe.model.RegisterResponse;
import com.example.ungdungthuexe.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo ViewModel
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Setup observers
        observeRegisterResponse();
        observeError();
        observeLoading();

        // Setup button click
        binding.btnRegister.setOnClickListener(v -> {
            performRegister();
        });
    }

    private void performRegister() {
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();
        String hoTen = binding.etHoTen.getText().toString().trim();
        String soCMND = binding.etSoCMND.getText().toString().trim();
        String soHoKhau = binding.etSoHoKhau.getText().toString().trim();
        String soDienThoai = binding.etSoDienThoai.getText().toString().trim();
        String diaChiCoQuan = binding.etDiaChiCoQuan.getText().toString().trim();

        // Validation
        if (!validateInput(username, password, confirmPassword, hoTen, soCMND, soHoKhau, soDienThoai, diaChiCoQuan)) {
            return;
        }

        registerViewModel.register(username, password, hoTen, soCMND, soHoKhau, soDienThoai, diaChiCoQuan);
    }

    private boolean validateInput(String username, String password, String confirmPassword, 
                                  String hoTen, String soCMND, String soHoKhau, 
                                  String soDienThoai, String diaChiCoQuan) {
        boolean isValid = true;

        if (TextUtils.isEmpty(username)) {
            binding.tilUsername.setError("Vui lòng nhập tên đăng nhập");
            isValid = false;
        } else {
            binding.tilUsername.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("Vui lòng nhập mật khẩu");
            isValid = false;
        } else {
            binding.tilPassword.setError(null);
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            binding.tilConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            binding.tilConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            isValid = false;
        } else {
            binding.tilConfirmPassword.setError(null);
        }

        if (TextUtils.isEmpty(hoTen)) {
            binding.tilHoTen.setError("Vui lòng nhập họ tên");
            isValid = false;
        } else {
            binding.tilHoTen.setError(null);
        }

        if (TextUtils.isEmpty(soCMND)) {
            binding.tilSoCMND.setError("Vui lòng nhập số CMND");
            isValid = false;
        } else {
            binding.tilSoCMND.setError(null);
        }

        if (TextUtils.isEmpty(soHoKhau)) {
            binding.tilSoHoKhau.setError("Vui lòng nhập số hộ khẩu");
            isValid = false;
        } else {
            binding.tilSoHoKhau.setError(null);
        }

        if (TextUtils.isEmpty(soDienThoai)) {
            binding.tilSoDienThoai.setError("Vui lòng nhập số điện thoại");
            isValid = false;
        } else {
            binding.tilSoDienThoai.setError(null);
        }

        if (TextUtils.isEmpty(diaChiCoQuan)) {
            binding.tilDiaChiCoQuan.setError("Vui lòng nhập địa chỉ cơ quan");
            isValid = false;
        } else {
            binding.tilDiaChiCoQuan.setError(null);
        }

        return isValid;
    }

    private void observeRegisterResponse() {
        registerViewModel.getRegisterResponse().observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                
                // TODO: Có thể chuyển sang màn hình đăng nhập hoặc màn hình chính
                // Intent intent = new Intent(this, LoginActivity.class);
                // startActivity(intent);
                // finish();
            }
        });
    }

    private void observeError() {
        registerViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeLoading() {
        registerViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(android.view.View.VISIBLE);
                binding.btnRegister.setEnabled(false);
            } else {
                binding.progressBar.setVisibility(android.view.View.GONE);
                binding.btnRegister.setEnabled(true);
            }
        });
    }
}

