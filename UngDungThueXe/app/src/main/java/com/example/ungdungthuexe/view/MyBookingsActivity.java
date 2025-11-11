package com.example.ungdungthuexe.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ungdungthuexe.adapter.MyBookingAdapter;
import com.example.ungdungthuexe.databinding.ActivityMyBookingsBinding;
import com.example.ungdungthuexe.model.MyBooking;
import com.example.ungdungthuexe.viewmodel.MyBookingsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {
    private ActivityMyBookingsBinding binding;
    private MyBookingsViewModel viewModel;
    private MyBookingAdapter adapter;
    private SharedPreferences sharedPreferences;
    
    private static final String PREF_NAME = "UngDungThueXe";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyBookingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MyBookingsViewModel.class);

        // Initialize Adapter
        adapter = new MyBookingAdapter(new ArrayList<>());
        binding.rvMyBookings.setAdapter(adapter);
        binding.rvMyBookings.setLayoutManager(new LinearLayoutManager(this));

        // Setup Observers
        observeBookings();
        observeLoading();
        observeError();

        // Load data
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        if (username != null) {
            viewModel.loadBookings(username);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }
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
}

