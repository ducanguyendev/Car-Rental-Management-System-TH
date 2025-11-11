package com.example.ungdungthuexe.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungthuexe.databinding.ItemMyBookingBinding;
import com.example.ungdungthuexe.model.MyBooking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.MyBookingViewHolder> {
    private List<MyBooking> bookingList;

    public MyBookingAdapter(List<MyBooking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public MyBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyBookingBinding binding = ItemMyBookingBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MyBookingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingViewHolder holder, int position) {
        MyBooking booking = bookingList.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public void updateList(List<MyBooking> newList) {
        this.bookingList = newList;
        notifyDataSetChanged();
    }

    class MyBookingViewHolder extends RecyclerView.ViewHolder {
        private ItemMyBookingBinding binding;

        public MyBookingViewHolder(@NonNull ItemMyBookingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MyBooking booking) {
            binding.tvTenLoaiXe.setText("Phiếu đặt: " + booking.getLoaiXe().getTenLoaiXe());
            binding.tvNgayDat.setText("Ngày đặt: " + formatDate(booking.getNgayDat()));
            binding.tvNgayNhan.setText("Ngày nhận: " + formatDate(booking.getNgayDuKienNhan()));
            
            // Set status text and color
            String status = booking.getTrangThai();
            binding.tvTrangThai.setText(status);
            
            // Color coding based on status
            int color;
            if (status != null) {
                switch (status.toLowerCase()) {
                    case "đang chờ":
                    case "dang cho":
                        color = android.R.color.holo_orange_dark;
                        break;
                    case "đã xác nhận":
                    case "da xac nhan":
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

