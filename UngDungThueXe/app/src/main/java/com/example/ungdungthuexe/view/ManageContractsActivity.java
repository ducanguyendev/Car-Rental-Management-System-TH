package com.example.ungdungthuexe.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ungdungthuexe.adapter.ManageContractAdapter;
import com.example.ungdungthuexe.databinding.ActivityManageContractsBinding;
import com.example.ungdungthuexe.model.ManageContractResponse;
import com.example.ungdungthuexe.viewmodel.ManageContractsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ManageContractsActivity extends AppCompatActivity implements ManageContractAdapter.OnContractActionsListener {
    private ActivityManageContractsBinding binding;
    private ManageContractsViewModel viewModel;
    private ManageContractAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageContractsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ManageContractsViewModel.class);

        // Initialize Adapter
        adapter = new ManageContractAdapter(new ArrayList<>(), this);
        binding.rvAllContracts.setAdapter(adapter);
        binding.rvAllContracts.setLayoutManager(new LinearLayoutManager(this));

        // Setup Observers
        observeContracts();
        observeLoading();
        observeError();
        observeSuccessMessage();

        // Load data
        viewModel.loadAllContracts();
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

    private void observeSuccessMessage() {
        viewModel.getSuccessMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCompleteClick(int maHopDong) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận hoàn thành")
                .setMessage("Bạn có chắc muốn hoàn thành hợp đồng này? Xe sẽ được chuyển về Sẵn sàng.")
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    viewModel.completeContract(maHopDong);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

