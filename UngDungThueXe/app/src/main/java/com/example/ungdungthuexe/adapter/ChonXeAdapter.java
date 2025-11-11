package com.example.ungdungthuexe.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungthuexe.databinding.ItemChonXeBinding;
import com.example.ungdungthuexe.model.Xe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChonXeAdapter extends RecyclerView.Adapter<ChonXeAdapter.ChonXeViewHolder> {
    private List<Xe> xeList;
    private OnXeCheckListener listener;

    public interface OnXeCheckListener {
        void onXeChecked(int maLoaiXe);
    }

    public ChonXeAdapter(List<Xe> xeList, OnXeCheckListener listener) {
        this.xeList = xeList != null ? xeList : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChonXeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChonXeBinding binding = ItemChonXeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ChonXeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChonXeViewHolder holder, int position) {
        Xe xe = xeList.get(position);
        holder.bind(xe);
    }

    @Override
    public int getItemCount() {
        return xeList.size();
    }

    public void updateList(List<Xe> newList) {
        this.xeList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<Xe> getSelectedXeList() {
        List<Xe> selectedList = new ArrayList<>();
        for (Xe xe : xeList) {
            if (xe.isSelected()) {
                selectedList.add(xe);
            }
        }
        return selectedList;
    }

    public void updateXePrice(int maXe, double giaThue) {
        for (Xe xe : xeList) {
            if (xe.getMaXe() == maXe) {
                xe.setDonGiaTaiThoiDiemThue(giaThue);
                notifyItemChanged(xeList.indexOf(xe));
                break;
            }
        }
    }

    class ChonXeViewHolder extends RecyclerView.ViewHolder {
        private ItemChonXeBinding binding;

        public ChonXeViewHolder(@NonNull ItemChonXeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Xe xe) {
            binding.tvTenXe.setText(xe.getTenXe());
            binding.cbChonXe.setChecked(xe.isSelected());
            
            // Display price
            if (xe.getDonGiaTaiThoiDiemThue() > 0) {
                DecimalFormat formatter = new DecimalFormat("#,###");
                String formattedPrice = formatter.format(xe.getDonGiaTaiThoiDiemThue());
                binding.tvGiaThue.setText(formattedPrice + " VND / ngày");
            } else {
                binding.tvGiaThue.setText("Đang tải giá...");
            }
            
            // Setup checkbox listener
            binding.cbChonXe.setOnCheckedChangeListener((buttonView, isChecked) -> {
                xe.setSelected(isChecked);
                
                if (isChecked && listener != null) {
                    // Notify listener to fetch price
                    listener.onXeChecked(xe.getLoaiXe().getMaLoaiXe());
                }
            });
        }
    }
}

