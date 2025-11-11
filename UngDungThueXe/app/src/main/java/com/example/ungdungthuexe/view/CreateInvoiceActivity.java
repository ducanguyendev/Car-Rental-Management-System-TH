package com.example.ungdungthuexe.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ungdungthuexe.databinding.ActivityCreateInvoiceBinding;
import com.example.ungdungthuexe.model.CreateHoaDonRequest;
import com.example.ungdungthuexe.model.HopDongSpinnerItem;
import com.example.ungdungthuexe.viewmodel.CreateInvoiceViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateInvoiceActivity extends AppCompatActivity {
    private ActivityCreateInvoiceBinding binding;
    private CreateInvoiceViewModel viewModel;
    private ArrayAdapter<HopDongSpinnerItem> contractAdapter;
    private ArrayAdapter<String> paymentTypeAdapter;
    private SharedPreferences sharedPreferences;
    
    private static final String PREF_NAME = "UngDungThueXe";
    private static final String KEY_MA_NHAN_VIEN = "maNhanVien";
    private static final List<String> PAYMENT_TYPES = Arrays.asList("Tiền mặt", "Chuyển khoản");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateInvoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CreateInvoiceViewModel.class);

        // Initialize Adapters
        contractAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        contractAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerHopDong.setAdapter(contractAdapter);

        paymentTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PAYMENT_TYPES);
        paymentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLoaiThanhToan.setAdapter(paymentTypeAdapter);

        // Setup button click
        binding.btnCreateInvoice.setOnClickListener(v -> performSubmit());

        // Setup observers
        observeContracts();
        observeLoading();
        observeError();
        observeCreateSuccess();

        // Load contracts
        viewModel.loadContracts();
    }

    private void observeContracts() {
        viewModel.getContractList().observe(this, contracts -> {
            if (contracts != null && !contracts.isEmpty()) {
                contractAdapter.clear();
                contractAdapter.addAll(contracts);
                contractAdapter.notifyDataSetChanged();
            }
        });
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

    private void observeCreateSuccess() {
        viewModel.getCreateSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Tạo thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void performSubmit() {
        // Validate contract selection
        HopDongSpinnerItem selectedContract = (HopDongSpinnerItem) binding.spinnerHopDong.getSelectedItem();
        if (selectedContract == null) {
            binding.tilHopDong.setError("Vui lòng chọn hợp đồng");
            return;
        } else {
            binding.tilHopDong.setError(null);
        }

        // Validate amount
        String amountStr = binding.etSoTienThanhToan.getText().toString().trim();
        if (TextUtils.isEmpty(amountStr)) {
            binding.tilSoTienThanhToan.setError("Vui lòng nhập số tiền thanh toán");
            return;
        } else {
            binding.tilSoTienThanhToan.setError(null);
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                binding.tilSoTienThanhToan.setError("Số tiền phải lớn hơn 0");
                return;
            }
        } catch (NumberFormatException e) {
            binding.tilSoTienThanhToan.setError("Số tiền không hợp lệ");
            return;
        }

        // Validate payment type
        String paymentType = (String) binding.spinnerLoaiThanhToan.getSelectedItem();
        if (TextUtils.isEmpty(paymentType)) {
            Toast.makeText(this, "Vui lòng chọn loại thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get maNhanVien from SharedPreferences
        int maNhanVien = sharedPreferences.getInt(KEY_MA_NHAN_VIEN, 1); // Default to 1 if not found

        // Create request
        CreateHoaDonRequest request = new CreateHoaDonRequest(
                selectedContract.getMaHopDong(),
                maNhanVien,
                amount,
                paymentType
        );

        // Submit
        viewModel.submitHoaDon(request);
    }
}
