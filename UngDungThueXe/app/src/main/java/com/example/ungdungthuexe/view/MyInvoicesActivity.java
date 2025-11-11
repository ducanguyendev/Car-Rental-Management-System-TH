package com.example.ungdungthuexe.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ungdungthuexe.adapter.MyInvoiceAdapter;
import com.example.ungdungthuexe.databinding.ActivityMyInvoicesBinding;
import com.example.ungdungthuexe.model.MyInvoice;
import com.example.ungdungthuexe.viewmodel.MyInvoicesViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyInvoicesActivity extends AppCompatActivity {
    private ActivityMyInvoicesBinding binding;
    private MyInvoicesViewModel viewModel;
    private MyInvoiceAdapter adapter;
    private SharedPreferences sharedPreferences;
    
    private static final String PREF_NAME = "UngDungThueXe";
    private static final String KEY_MA_KH = "maKH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyInvoicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MyInvoicesViewModel.class);

        // Initialize Adapter
        adapter = new MyInvoiceAdapter(new ArrayList<>());
        binding.rvMyInvoices.setAdapter(adapter);
        binding.rvMyInvoices.setLayoutManager(new LinearLayoutManager(this));

        // Setup Observers
        observeInvoices();
        observeLoading();
        observeError();

        // Load data
        int maKH = sharedPreferences.getInt(KEY_MA_KH, -1);
        if (maKH != -1) {
            viewModel.loadInvoices(maKH);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin khách hàng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void observeInvoices() {
        viewModel.getInvoices().observe(this, invoices -> {
            if (invoices != null) {
                adapter.updateList(invoices);
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

