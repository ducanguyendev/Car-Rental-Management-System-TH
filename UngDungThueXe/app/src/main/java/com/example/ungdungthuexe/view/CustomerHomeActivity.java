package com.example.ungdungthuexe.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ungdungthuexe.adapter.LoaiXeAdapter;
import com.example.ungdungthuexe.adapter.XeAdapter;
import com.example.ungdungthuexe.databinding.ActivityCustomerHomeBinding;
import com.example.ungdungthuexe.model.LoaiXe;
import com.example.ungdungthuexe.model.Xe;
import com.example.ungdungthuexe.viewmodel.CustomerHomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerHomeActivity extends AppCompatActivity {
    private ActivityCustomerHomeBinding binding;
    private CustomerHomeViewModel viewModel;
    private XeAdapter xeAdapter;
    private LoaiXeAdapter loaiXeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CustomerHomeViewModel.class);

        // Initialize Adapters
        xeAdapter = new XeAdapter(new ArrayList<>(), xe -> {
            Intent intent = new Intent(this, XeDetailActivity.class);
            intent.putExtra("maXe", xe.getMaXe());
            startActivity(intent);
        });

        loaiXeAdapter = new LoaiXeAdapter(new ArrayList<>(), loaiXe -> {
            Toast.makeText(this, "Filter by: " + loaiXe.getTenLoaiXe(), Toast.LENGTH_SHORT).show();
            // TODO: Implement filtering logic
        });

        // Setup RecyclerViews
        binding.rvAvailableXe.setAdapter(xeAdapter);
        binding.rvAvailableXe.setLayoutManager(new GridLayoutManager(this, 2));

        binding.rvLoaiXeFilter.setAdapter(loaiXeAdapter);
        binding.rvLoaiXeFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Setup Observers
        observeXeList();
        observeLoaiXeList();
        observeLoading();
        observeError();

        // Load data
        viewModel.loadData();
    }

    private void observeXeList() {
        viewModel.getXeList().observe(this, xeList -> {
            if (xeList != null) {
                xeAdapter.updateList(xeList);
            }
        });
    }

    private void observeLoaiXeList() {
        viewModel.getLoaiXeList().observe(this, loaiXeList -> {
            if (loaiXeList != null) {
                loaiXeAdapter.updateList(loaiXeList);
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

