package com.example.ungdungthuexe.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungthuexe.databinding.ItemManageCustomerBinding;
import com.example.ungdungthuexe.model.ManageCustomerResponse;

import java.util.List;

public class ManageCustomerAdapter extends RecyclerView.Adapter<ManageCustomerAdapter.ManageCustomerViewHolder> {
    private List<ManageCustomerResponse> customerList;

    public ManageCustomerAdapter(List<ManageCustomerResponse> customerList) {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public ManageCustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemManageCustomerBinding binding = ItemManageCustomerBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ManageCustomerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageCustomerViewHolder holder, int position) {
        ManageCustomerResponse customer = customerList.get(position);
        holder.bind(customer);
    }

    @Override
    public int getItemCount() {
        return customerList != null ? customerList.size() : 0;
    }

    public void updateList(List<ManageCustomerResponse> newList) {
        this.customerList = newList;
        notifyDataSetChanged();
    }

    class ManageCustomerViewHolder extends RecyclerView.ViewHolder {
        private ItemManageCustomerBinding binding;

        public ManageCustomerViewHolder(@NonNull ItemManageCustomerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ManageCustomerResponse customer) {
            binding.tvHoTen.setText("Họ tên: " + customer.getHoTen());
            binding.tvSoDienThoai.setText("SĐT: " + customer.getSoDienThoai());
            binding.tvSoCMND.setText("CMND: " + customer.getSoCMND());
            binding.tvSoHopDong.setText("Số HĐ: " + customer.getSoHopDong());
        }
    }
}
