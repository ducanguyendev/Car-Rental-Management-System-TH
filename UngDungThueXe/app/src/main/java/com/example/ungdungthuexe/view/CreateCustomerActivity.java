package com.example.ungdungthuexe.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ungdungthuexe.databinding.ActivityCreateCustomerBinding;
import com.example.ungdungthuexe.model.CreateCustomerRequest;
import com.example.ungdungthuexe.model.CreateCustomerResponse;
import com.example.ungdungthuexe.viewmodel.CreateCustomerViewModel;

public class CreateCustomerActivity extends AppCompatActivity {
    private ActivityCreateCustomerBinding binding;
    private CreateCustomerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CreateCustomerViewModel.class);

        // Setup button click
        binding.btnCreateCustomer.setOnClickListener(v -> performSubmit());

        // Setup observers
        observeLoading();
        observeError();
        observeCreateResult();
    }

    private void observeLoading() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(android.view.View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(android.view.View.GONE);
            }
        });
    }

    private void observeError() {
        viewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeCreateResult() {
        viewModel.getCreateResult().observe(this, result -> {
            if (result != null && result.isSuccess()) {
                Toast.makeText(this, "Tạo thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void performSubmit() {
        // Validate hoTen (required)
        String hoTen = binding.etHoTen.getText().toString().trim();
        if (TextUtils.isEmpty(hoTen)) {
            binding.tilHoTen.setError("Vui lòng nhập họ tên");
            return;
        } else {
            binding.tilHoTen.setError(null);
        }

        // Validate soCMND (required)
        String soCMND = binding.etSoCMND.getText().toString().trim();
        if (TextUtils.isEmpty(soCMND)) {
            binding.tilSoCMND.setError("Vui lòng nhập số CMND");
            return;
        } else {
            binding.tilSoCMND.setError(null);
        }

        // Validate soDienThoai (required)
        String soDienThoai = binding.etSoDienThoai.getText().toString().trim();
        if (TextUtils.isEmpty(soDienThoai)) {
            binding.tilSoDienThoai.setError("Vui lòng nhập số điện thoại");
            return;
        } else {
            binding.tilSoDienThoai.setError(null);
        }

        // Get optional fields
        String soHoKhau = binding.etSoHoKhau.getText().toString().trim();
        String diaChiCoQuan = binding.etDiaChiCoQuan.getText().toString().trim();

        // Create request
        CreateCustomerRequest request = new CreateCustomerRequest(
                hoTen,
                soCMND,
                soHoKhau,
                soDienThoai,
                diaChiCoQuan
        );

        // Submit
        viewModel.submitCustomer(request);
    }
}
