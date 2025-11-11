package com.example.ungdungthuexe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungthuexe.R;
import com.example.ungdungthuexe.databinding.ItemLoaiXeFilterBinding;
import com.example.ungdungthuexe.model.LoaiXe;

import java.util.List;

public class LoaiXeAdapter extends RecyclerView.Adapter<LoaiXeAdapter.LoaiXeViewHolder> {
    private List<LoaiXe> loaiXeList;
    private OnLoaiXeClickListener listener;

    public interface OnLoaiXeClickListener {
        void onLoaiXeClick(LoaiXe loaiXe);
    }

    public LoaiXeAdapter(List<LoaiXe> loaiXeList, OnLoaiXeClickListener listener) {
        this.loaiXeList = loaiXeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LoaiXeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLoaiXeFilterBinding binding = ItemLoaiXeFilterBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new LoaiXeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiXeViewHolder holder, int position) {
        LoaiXe loaiXe = loaiXeList.get(position);
        holder.bind(loaiXe);
    }

    @Override
    public int getItemCount() {
        return loaiXeList != null ? loaiXeList.size() : 0;
    }

    public void updateList(List<LoaiXe> newList) {
        this.loaiXeList = newList;
        notifyDataSetChanged();
    }

    class LoaiXeViewHolder extends RecyclerView.ViewHolder {
        private ItemLoaiXeFilterBinding binding;

        public LoaiXeViewHolder(@NonNull ItemLoaiXeFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LoaiXe loaiXe) {
            binding.tvTenLoaiXe.setText(loaiXe.getTenLoaiXe());
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLoaiXeClick(loaiXe);
                }
            });
        }
    }
}

