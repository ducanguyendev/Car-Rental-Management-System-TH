package com.example.ungdungthuexe.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.ungdungthuexe.R;
import com.example.ungdungthuexe.databinding.ActivityXeDetailBinding;
import com.example.ungdungthuexe.model.GiaThue;
import com.example.ungdungthuexe.model.PhieuDatXeRequest;
import com.example.ungdungthuexe.model.PhieuDatXeResponse;
import com.example.ungdungthuexe.model.XeDetail;
import com.example.ungdungthuexe.viewmodel.XeDetailViewModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class XeDetailActivity extends AppCompatActivity {
    private ActivityXeDetailBinding binding;
    private XeDetailViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private Calendar selectedCalendar;
    private SimpleDateFormat dateFormat;
    
    private static final String PREF_NAME = "UngDungThueXe";
    private static final String KEY_MA_KH = "maKH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityXeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        selectedCalendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Get maXe from intent
        int maXe = getIntent().getIntExtra("maXe", -1);
        if (maXe == -1) {
            Toast.makeText(this, "Lỗi: Không có thông tin xe", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(XeDetailViewModel.class);

        // Setup observers
        observeXeDetail();
        observeGiaThue();
        observeLoading();
        observeError();
        observeDatXeResult();

        // Setup button clicks
        setupListeners();

        // Load data
        viewModel.loadXeDetail(maXe);
    }

    private void setupListeners() {
        binding.etNgayNhan.setOnClickListener(v -> showDatePicker());
        binding.btnChonNgay.setOnClickListener(v -> showDatePicker());
        binding.btnDatXe.setOnClickListener(v -> performDatXe());
    }

    private void showDatePicker() {
        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedCalendar.set(year, month, dayOfMonth);
                    binding.etNgayNhan.setText(dateFormat.format(selectedCalendar.getTime()));
                },
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH)
        );
        
        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void observeXeDetail() {
        viewModel.getXeDetail().observe(this, xeDetail -> {
            if (xeDetail != null) {
                displayXeDetail(xeDetail);
            }
        });
    }

    private void displayXeDetail(XeDetail xeDetail) {
        binding.tvTenXe.setText(xeDetail.getTenXe());
        binding.tvBienSoXe.setText("Biển số: " + xeDetail.getBienSoXe());
        binding.tvTrangThai.setText(xeDetail.getTrangThai());
        
        // Load image with Glide
        if (xeDetail.getImageURL() != null && !xeDetail.getImageURL().isEmpty()) {
            Glide.with(this)
                    .load(xeDetail.getImageURL())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.ivXeImage);
        }
    }

    private void observeGiaThue() {
        viewModel.getGiaThue().observe(this, giaThue -> {
            if (giaThue != null) {
                displayGiaThue(giaThue);
            }
        });
    }

    private void displayGiaThue(GiaThue giaThue) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedPrice = formatter.format(giaThue.getDonGiaTheoNgay());
        binding.tvGiaThue.setText(formattedPrice + " VND / ngày");
    }

    private void observeLoading() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(android.view.View.VISIBLE);
                binding.btnDatXe.setEnabled(false);
            } else {
                binding.progressBar.setVisibility(android.view.View.GONE);
                binding.btnDatXe.setEnabled(true);
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

    private void observeDatXeResult() {
        viewModel.getDatXeResult().observe(this, response -> {
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "Đặt xe thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void performDatXe() {
        // Validate date selected
        String ngayDuKienNhan = binding.etNgayNhan.getText().toString().trim();
        if (ngayDuKienNhan.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày dự kiến nhận", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get maKH from SharedPreferences
        int maKH = sharedPreferences.getInt(KEY_MA_KH, -1);
        if (maKH == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get maLoaiXe from xeDetail
        XeDetail xeDetail = viewModel.getXeDetail().getValue();
        if (xeDetail == null) {
            Toast.makeText(this, "Lỗi: Không có thông tin xe", Toast.LENGTH_SHORT).show();
            return;
        }

        int maLoaiXe = xeDetail.getMaLoaiXe();

        // Create request
        PhieuDatXeRequest request = new PhieuDatXeRequest(maKH, maLoaiXe, ngayDuKienNhan);
        
        // Call API
        viewModel.datXe(request);
    }
}

