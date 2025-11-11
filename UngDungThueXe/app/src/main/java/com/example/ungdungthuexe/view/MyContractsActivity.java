package com.example.ungdungthuexe.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ungdungthuexe.adapter.MyContractAdapter;
import com.example.ungdungthuexe.databinding.ActivityMyContractsBinding;
import com.example.ungdungthuexe.model.MyContract;
import com.example.ungdungthuexe.viewmodel.MyContractsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyContractsActivity extends AppCompatActivity {
    private ActivityMyContractsBinding binding;
    private MyContractsViewModel viewModel;
    private MyContractAdapter adapter;
    private SharedPreferences sharedPreferences;
    
    private static final String PREF_NAME = "UngDungThueXe";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyContractsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MyContractsViewModel.class);

        // Initialize Adapter
        adapter = new MyContractAdapter(new ArrayList<>());
        binding.rvMyContracts.setAdapter(adapter);
        binding.rvMyContracts.setLayoutManager(new LinearLayoutManager(this));

        // Setup Observers
        observeContracts();
        observeLoading();
        observeError();

        // Load data
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        if (username != null) {
            viewModel.loadContracts(username);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void observeContracts() {
        viewModel.getContracts().observe(this, contracts -> {
            if (contracts != null) {
                adapter.updateList(contracts);
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

