package com.example.ungdungthuexe.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ungdungthuexe.R;
import com.example.ungdungthuexe.databinding.ItemXeBinding;
import com.example.ungdungthuexe.model.Xe;

import java.util.List;

public class XeAdapter extends RecyclerView.Adapter<XeAdapter.XeViewHolder> {
    private List<Xe> xeList;
    private OnXeClickListener listener;

    public interface OnXeClickListener {
        void onXeClick(Xe xe);
    }

    public XeAdapter(List<Xe> xeList, OnXeClickListener listener) {
        this.xeList = xeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public XeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemXeBinding binding = ItemXeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new XeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull XeViewHolder holder, int position) {
        Xe xe = xeList.get(position);
        holder.bind(xe);
    }

    @Override
    public int getItemCount() {
        return xeList != null ? xeList.size() : 0;
    }

    public void updateList(List<Xe> newList) {
        this.xeList = newList;
        notifyDataSetChanged();
    }

    class XeViewHolder extends RecyclerView.ViewHolder {
        private ItemXeBinding binding;

        public XeViewHolder(@NonNull ItemXeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Xe xe) {
            binding.tvTenXe.setText(xe.getTenXe());
            binding.tvBienSoXe.setText("Biển số: " + xe.getBienSoXe());
            binding.tvTrangThai.setText(xe.getTrangThai());
            
            // Load image with Glide
            if (xe.getImageURL() != null && !xe.getImageURL().isEmpty()) {
                Glide.with(binding.getRoot().getContext())
                        .load(xe.getImageURL())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(binding.ivXeImage);
            }
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onXeClick(xe);
                }
            });
        }
    }
}

