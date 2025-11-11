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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ungdungthuexe.adapter.ChonXeAdapter;
import com.example.ungdungthuexe.databinding.ActivityCreateContractBinding;
import com.example.ungdungthuexe.model.ChiTietHopDongs;
import com.example.ungdungthuexe.model.CreateHopDongRequest;
import com.example.ungdungthuexe.model.GiaThue;
import com.example.ungdungthuexe.model.KhachHangSpinner;
import com.example.ungdungthuexe.model.PhieuDatXeDuyet;
import com.example.ungdungthuexe.model.Xe;
import com.example.ungdungthuexe.viewmodel.CreateContractViewModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateContractActivity extends AppCompatActivity implements ChonXeAdapter.OnXeCheckListener {
    private ActivityCreateContractBinding binding;
    private CreateContractViewModel viewModel;
    private ChonXeAdapter xeAdapter;
    private ArrayAdapter<KhachHangSpinner> khachHangAdapter;
    private ArrayAdapter<PhieuDatXeDuyet> phieuDatAdapter;
    private SharedPreferences sharedPreferences;
    private Calendar selectedPickupCalendar;
    private Calendar selectedReturnCalendar;
    private SimpleDateFormat dateFormat;
    
    private static final String PREF_NAME = "UngDungThueXe";
    private static final String KEY_MA_NHAN_VIEN = "maNhanVien";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateContractBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        selectedPickupCalendar = Calendar.getInstance();
        selectedReturnCalendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CreateContractViewModel.class);

        // Initialize Adapters
        xeAdapter = new ChonXeAdapter(new ArrayList<>(), this);
        binding.rvChonXe.setAdapter(xeAdapter);
        binding.rvChonXe.setLayoutManager(new LinearLayoutManager(this));

        khachHangAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        khachHangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerKhachHang.setAdapter(khachHangAdapter);

        phieuDatAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        phieuDatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPhieuDatXe.setAdapter(phieuDatAdapter);

        // Setup listeners
        setupListeners();

        // Setup observers
        observeData();
        observeLoading();
        observeError();
        observeSuccess();
        observeGiaThue();

        // Load initial data
        viewModel.loadInitialData();
    }

    private void setupListeners() {
        binding.etNgayNhanXe.setOnClickListener(v -> showPickupDatePicker());
        binding.btnChonNgayNhan.setOnClickListener(v -> showPickupDatePicker());
        binding.etNgayTraXe.setOnClickListener(v -> showReturnDatePicker());
        binding.btnChonNgayTra.setOnClickListener(v -> showReturnDatePicker());
        binding.btnTaoHopDong.setOnClickListener(v -> performSubmit());
        
        // Spinner listener: when booking selected, auto-select customer
        binding.spinnerPhieuDatXe.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                PhieuDatXeDuyet selected = phieuDatAdapter.getItem(position);
                if (selected != null) {
                    // Find and select corresponding customer
                    int maKH = selected.getKhachHang().getMaKH();
                    for (int i = 0; i < khachHangAdapter.getCount(); i++) {
                        KhachHangSpinner kh = khachHangAdapter.getItem(i);
                        if (kh.getMaKH() == maKH) {
                            binding.spinnerKhachHang.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void showPickupDatePicker() {
        android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedPickupCalendar.set(year, month, dayOfMonth);
                    binding.etNgayNhanXe.setText(dateFormat.format(selectedPickupCalendar.getTime()));
                },
                selectedPickupCalendar.get(Calendar.YEAR),
                selectedPickupCalendar.get(Calendar.MONTH),
                selectedPickupCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    private void showReturnDatePicker() {
        android.app.DatePickerDialog dialog = new android.app.DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedReturnCalendar.set(year, month, dayOfMonth);
                    binding.etNgayTraXe.setText(dateFormat.format(selectedReturnCalendar.getTime()));
                },
                selectedReturnCalendar.get(Calendar.YEAR),
                selectedReturnCalendar.get(Calendar.MONTH),
                selectedReturnCalendar.get(Calendar.DAY_OF_MONTH));
        // Return date should be after pickup date
        if (selectedPickupCalendar.getTimeInMillis() > 0) {
            dialog.getDatePicker().setMinDate(selectedPickupCalendar.getTimeInMillis());
        } else {
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        }
        dialog.show();
    }

    private void observeData() {
        viewModel.getKhachHangs().observe(this, khachHangs -> {
            if (khachHangs != null) {
                khachHangAdapter.clear();
                khachHangAdapter.addAll(khachHangs);
            }
        });

        viewModel.getBookings().observe(this, bookings -> {
            if (bookings != null) {
                phieuDatAdapter.clear();
                phieuDatAdapter.addAll(bookings);
            }
        });

        viewModel.getAvailableXe().observe(this, xeList -> {
            if (xeList != null) {
                xeAdapter.updateList(xeList);
            }
        });
    }

    private void observeLoading() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(android.view.View.VISIBLE);
                binding.btnTaoHopDong.setEnabled(false);
            } else {
                binding.progressBar.setVisibility(android.view.View.GONE);
                binding.btnTaoHopDong.setEnabled(true);
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

    private void observeSuccess() {
        viewModel.getSuccessMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void observeGiaThue() {
        viewModel.getGiaThue().observe(this, giaThue -> {
            if (giaThue != null) {
                updateAllSelectedXePrices(giaThue.getDonGiaTheoNgay());
                calculateTotal();
            }
        });
    }

    @Override
    public void onXeChecked(int maLoaiXe) {
        viewModel.getGiaChoXe(maLoaiXe);
    }

    private void updateAllSelectedXePrices(double giaThue) {
        List<Xe> selectedXe = xeAdapter.getSelectedXeList();
        for (Xe xe : selectedXe) {
            if (xe.getDonGiaTaiThoiDiemThue() == 0) {
                xeAdapter.updateXePrice(xe.getMaXe(), giaThue);
            }
        }
        xeAdapter.notifyDataSetChanged();
    }

    private void calculateTotal() {
        double total = 0;
        List<Xe> selectedXe = xeAdapter.getSelectedXeList();
        for (Xe xe : selectedXe) {
            total += xe.getDonGiaTaiThoiDiemThue();
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        binding.tvTongTien.setText("Tổng tiền: " + formatter.format(total) + " VND");
    }

    private void performSubmit() {
        // Validation
        if (binding.spinnerKhachHang.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Vui lòng chọn khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        String ngayNhanXe = binding.etNgayNhanXe.getText().toString().trim();
        if (TextUtils.isEmpty(ngayNhanXe)) {
            Toast.makeText(this, "Vui lòng chọn ngày nhận xe", Toast.LENGTH_SHORT).show();
            return;
        }

        String ngayTraXe = binding.etNgayTraXe.getText().toString().trim();
        if (TextUtils.isEmpty(ngayTraXe)) {
            Toast.makeText(this, "Vui lòng chọn ngày trả xe", Toast.LENGTH_SHORT).show();
            return;
        }

        String diaDiemNhan = binding.etDiaDiemNhan.getText().toString().trim();
        if (TextUtils.isEmpty(diaDiemNhan)) {
            Toast.makeText(this, "Vui lòng nhập địa điểm nhận", Toast.LENGTH_SHORT).show();
            return;
        }

        String diaDiemTra = binding.etDiaDiemTra.getText().toString().trim();
        if (TextUtils.isEmpty(diaDiemTra)) {
            Toast.makeText(this, "Vui lòng nhập địa điểm trả", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Xe> selectedXe = xeAdapter.getSelectedXeList();
        if (selectedXe.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 xe", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get values
        KhachHangSpinner selectedKH = (KhachHangSpinner) binding.spinnerKhachHang.getSelectedItem();
        int maKH = selectedKH.getMaKH();

        int maNhanVien = sharedPreferences.getInt(KEY_MA_NHAN_VIEN, 1); // Default to 1 if not found

        Integer maDatXe = null;
        if (binding.spinnerPhieuDatXe.getSelectedItemPosition() >= 0) {
            PhieuDatXeDuyet selectedPhieu = (PhieuDatXeDuyet) binding.spinnerPhieuDatXe.getSelectedItem();
            maDatXe = selectedPhieu.getMaDatXe();
        }

        double tienDatCoc = 0;
        String tienDatCocStr = binding.etTienDatCoc.getText().toString().trim();
        if (!TextUtils.isEmpty(tienDatCocStr)) {
            try {
                tienDatCoc = Double.parseDouble(tienDatCocStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Tiền đặt cọc không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        double tongTien = 0;
        for (Xe xe : selectedXe) {
            tongTien += xe.getDonGiaTaiThoiDiemThue();
        }

        // Create chiTietHopDongs
        List<ChiTietHopDongs> chiTietList = new ArrayList<>();
        for (Xe xe : selectedXe) {
            chiTietList.add(new ChiTietHopDongs(xe.getMaXe(), xe.getDonGiaTaiThoiDiemThue(), null));
        }

        // Create request
        CreateHopDongRequest request = new CreateHopDongRequest(
                maKH, maNhanVien, maDatXe, ngayNhanXe, diaDiemNhan,
                ngayTraXe, diaDiemTra, tongTien, tienDatCoc, chiTietList);

        // Submit
        viewModel.submitHopDong(request);
    }
}

