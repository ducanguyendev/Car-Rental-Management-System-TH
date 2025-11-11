package com.example.ungdungthuexe.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungthuexe.databinding.ItemManageContractBinding;
import com.example.ungdungthuexe.model.ManageContractResponse;

import java.text.DecimalFormat;
import java.util.List;

public class ManageContractAdapter extends RecyclerView.Adapter<ManageContractAdapter.ManageContractViewHolder> {
    private List<ManageContractResponse> contractList;
    private OnContractActionsListener listener;

    public interface OnContractActionsListener {
        void onCompleteClick(int maHopDong);
    }

    public ManageContractAdapter(List<ManageContractResponse> contractList, OnContractActionsListener listener) {
        this.contractList = contractList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ManageContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemManageContractBinding binding = ItemManageContractBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ManageContractViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageContractViewHolder holder, int position) {
        ManageContractResponse contract = contractList.get(position);
        holder.bind(contract);
    }

    @Override
    public int getItemCount() {
        return contractList != null ? contractList.size() : 0;
    }

    public void updateList(List<ManageContractResponse> newList) {
        this.contractList = newList;
        notifyDataSetChanged();
    }

    class ManageContractViewHolder extends RecyclerView.ViewHolder {
        private ItemManageContractBinding binding;

        public ManageContractViewHolder(@NonNull ItemManageContractBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ManageContractResponse contract) {
            binding.tvMaHopDong.setText("Hợp đồng #" + contract.getMaHopDong());
            binding.tvTenKhachHang.setText("KH: " + contract.getKhachHang().getHoTen());
            
            // Build car list string
            String danhSachXe = "Xe: ";
            if (contract.getChiTietHopDongs() != null && !contract.getChiTietHopDongs().isEmpty()) {
                for (int i = 0; i < contract.getChiTietHopDongs().size(); i++) {
                    danhSachXe += contract.getChiTietHopDongs().get(i).getTenXe();
                    if (i < contract.getChiTietHopDongs().size() - 1) {
                        danhSachXe += ", ";
                    }
                }
            }
            binding.tvDanhSachXe.setText(danhSachXe);
            
            // Format total price
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedPrice = formatter.format(contract.getTongTien());
            binding.tvTongTien.setText("Tổng tiền: " + formattedPrice + " VND");
            
            // Set status text and color
            String status = contract.getTrangThai();
            binding.tvTrangThai.setText("Trạng thái: " + status);
            
            // Color coding based on status
            int color;
            if (status != null) {
                switch (status.toLowerCase()) {
                    case "đang thuê":
                    case "dang thue":
                        color = android.R.color.holo_blue_dark;
                        break;
                    case "đã hoàn thành":
                    case "da hoan thanh":
                        color = android.R.color.holo_green_dark;
                        break;
                    case "đã hủy":
                    case "da huy":
                        color = android.R.color.holo_red_dark;
                        break;
                    default:
                        color = android.R.color.darker_gray;
                        break;
                }
            } else {
                color = android.R.color.darker_gray;
            }
            
            binding.tvTrangThai.setTextColor(ContextCompat.getColor(itemView.getContext(), color));
            
            // Show/hide complete button based on status
            boolean isRenting = status != null && status.toLowerCase().contains("thuê");
            binding.btnCompleteContract.setVisibility(isRenting ? android.view.View.VISIBLE : android.view.View.GONE);
            
            // Setup button click
            binding.btnCompleteContract.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCompleteClick(contract.getMaHopDong());
                }
            });
        }
    }
}

