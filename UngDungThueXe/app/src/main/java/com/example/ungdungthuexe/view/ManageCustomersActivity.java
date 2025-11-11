package com.example.ungdungthuexe.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ungdungthuexe.adapter.ManageCustomerAdapter;
import com.example.ungdungthuexe.databinding.ActivityManageCustomersBinding;
import com.example.ungdungthuexe.viewmodel.ManageCustomersViewModel;

import java.util.ArrayList;

public class ManageCustomersActivity extends AppCompatActivity {
    private ActivityManageCustomersBinding binding;
    private ManageCustomersViewModel viewModel;
    private ManageCustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageCustomersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ManageCustomersViewModel.class);

        // Initialize Adapter
        adapter = new ManageCustomerAdapter(new ArrayList<>());
        binding.rvAllCustomers.setAdapter(adapter);
        binding.rvAllCustomers.setLayoutManager(new LinearLayoutManager(this));

        // Setup SearchView listener
        setupSearchView();

        // Setup Observers
        observeCustomers();
        observeLoading();
        observeError();

        // Load all customers on start
        viewModel.loadAllCustomers();
    }

    private void setupSearchView() {
        binding.searchViewCustomers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // When user submits search
                if (query != null && !query.trim().isEmpty()) {
                    viewModel.searchCustomers(query.trim());
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // When user types or clears text
                if (newText == null || newText.trim().isEmpty()) {
                    // If search is cleared, reload all customers
                    viewModel.loadAllCustomers();
                }
                return true;
            }
        });
    }

    private void observeCustomers() {
        viewModel.getCustomerList().observe(this, customers -> {
            if (customers != null) {
                adapter.updateList(customers);
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
