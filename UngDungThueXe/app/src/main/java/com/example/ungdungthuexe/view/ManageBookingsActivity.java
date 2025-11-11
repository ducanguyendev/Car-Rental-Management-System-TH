package com.example.ungdungthuexe.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ungdungthuexe.adapter.ManageBookingAdapter;
import com.example.ungdungthuexe.databinding.ActivityManageBookingsBinding;
import com.example.ungdungthuexe.model.BookingDetailsAccountant;
import com.example.ungdungthuexe.viewmodel.ManageBookingsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ManageBookingsActivity extends AppCompatActivity implements ManageBookingAdapter.OnBookingActionsListener {
    private ActivityManageBookingsBinding binding;
    private ManageBookingsViewModel viewModel;
    private ManageBookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageBookingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ManageBookingsViewModel.class);

        // Initialize Adapter
        adapter = new ManageBookingAdapter(new ArrayList<>(), this);
        binding.rvAllBookings.setAdapter(adapter);
        binding.rvAllBookings.setLayoutManager(new LinearLayoutManager(this));

        // Setup Observers
        observeBookings();
        observeLoading();
        observeError();
        observeSuccessMessage();

        // Load data
        viewModel.loadAllBookings();
    }

    private void observeBookings() {
        viewModel.getBookings().observe(this, bookings -> {
            if (bookings != null) {
                adapter.updateList(bookings);
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

    private void observeSuccessMessage() {
        viewModel.getSuccessMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onApproveClick(int maDatXe) {
        viewModel.approveBooking(maDatXe);
    }

    @Override
    public void onRejectClick(int maDatXe) {
        viewModel.rejectBooking(maDatXe);
    }

    @Override
    public void onDeleteClick(int maDatXe) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phiếu đặt xe này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    viewModel.deleteBooking(maDatXe);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

