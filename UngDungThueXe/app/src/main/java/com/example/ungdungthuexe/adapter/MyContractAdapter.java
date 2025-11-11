package com.example.ungdungthuexe.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungthuexe.databinding.ItemMyContractBinding;
import com.example.ungdungthuexe.model.MyContract;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyContractAdapter extends RecyclerView.Adapter<MyContractAdapter.MyContractViewHolder> {
    private List<MyContract> contractList;

    public MyContractAdapter(List<MyContract> contractList) {
        this.contractList = contractList;
    }

    @NonNull
    @Override
    public MyContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyContractBinding binding = ItemMyContractBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MyContractViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyContractViewHolder holder, int position) {
        MyContract contract = contractList.get(position);
        holder.bind(contract);
    }

    @Override
    public int getItemCount() {
        return contractList != null ? contractList.size() : 0;
    }

    public void updateList(List<MyContract> newList) {
        this.contractList = newList;
        notifyDataSetChanged();
    }

    class MyContractViewHolder extends RecyclerView.ViewHolder {
        private ItemMyContractBinding binding;

        public MyContractViewHolder(@NonNull ItemMyContractBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MyContract contract) {
            binding.tvMaHopDong.setText("Hợp đồng #" + contract.getMaHopDong());
            binding.tvNgayNhanXe.setText("Nhận: " + formatDate(contract.getNgayNhanXe()));
            binding.tvNgayTraXe.setText("Trả: " + formatDate(contract.getNgayTraXe()));
            
            // Format total price
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedPrice = formatter.format(contract.getTongTien());
            binding.tvTongTien.setText(formattedPrice + " VND");
            
            // Set status text and color
            String status = contract.getTrangThai();
            binding.tvTrangThai.setText(status);
            
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
                    case "quá hạn":
                    case "qua han":
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
        }

        private String formatDate(String dateStr) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = inputFormat.parse(dateStr);
                return outputFormat.format(date);
            } catch (ParseException e) {
                return dateStr; // Return original if parsing fails
            }
        }
    }
}

