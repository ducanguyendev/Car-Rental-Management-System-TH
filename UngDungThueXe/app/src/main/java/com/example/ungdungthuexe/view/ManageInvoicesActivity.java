package com.example.ungdungthuexe.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ungdungthuexe.adapter.ManageInvoiceAdapter;
import com.example.ungdungthuexe.databinding.ActivityManageInvoicesBinding;
import com.example.ungdungthuexe.model.ManageInvoiceResponse;
import com.example.ungdungthuexe.viewmodel.ManageInvoicesViewModel;

import java.util.ArrayList;

public class ManageInvoicesActivity extends AppCompatActivity {
    private ActivityManageInvoicesBinding binding;
    private ManageInvoicesViewModel viewModel;
    private ManageInvoiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageInvoicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ManageInvoicesViewModel.class);

        // Initialize Adapter
        adapter = new ManageInvoiceAdapter(new ArrayList<>());
        binding.rvAllInvoices.setAdapter(adapter);
        binding.rvAllInvoices.setLayoutManager(new LinearLayoutManager(this));

        // Setup Observers
        observeInvoices();
        observeLoading();
        observeError();

        // Setup FAB Click
        binding.fabCreateInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateInvoiceActivity.class);
            startActivity(intent);
        });

        // Load data
        viewModel.loadAllInvoices();
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

