package com.example.ungdungthuexe.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungthuexe.databinding.ItemManageBookingBinding;
import com.example.ungdungthuexe.model.BookingDetailsAccountant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManageBookingAdapter extends RecyclerView.Adapter<ManageBookingAdapter.ManageBookingViewHolder> {
    private List<BookingDetailsAccountant> bookingList;
    private OnBookingActionsListener listener;

    public interface OnBookingActionsListener {
        void onApproveClick(int maDatXe);
        void onRejectClick(int maDatXe);
        void onDeleteClick(int maDatXe);
    }

    public ManageBookingAdapter(List<BookingDetailsAccountant> bookingList, OnBookingActionsListener listener) {
        this.bookingList = bookingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ManageBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemManageBookingBinding binding = ItemManageBookingBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ManageBookingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageBookingViewHolder holder, int position) {
        BookingDetailsAccountant booking = bookingList.get(position);
        holder.bind(booking);
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    public void updateList(List<BookingDetailsAccountant> newList) {
        this.bookingList = newList;
        notifyDataSetChanged();
    }

    class ManageBookingViewHolder extends RecyclerView.ViewHolder {
        private ItemManageBookingBinding binding;

        public ManageBookingViewHolder(@NonNull ItemManageBookingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BookingDetailsAccountant booking) {
            binding.tvTenKhachHang.setText("KH: " + booking.getKhachHang().getHoTen());
            binding.tvLoaiXe.setText("Xe: " + booking.getLoaiXe().getTenLoaiXe());
            binding.tvNgayDat.setText("Ngày đặt: " + formatDate(booking.getNgayDat()));
            
            // Set status text and color
            String status = booking.getTrangThai();
            binding.tvTrangThai.setText("Trạng thái: " + status);
            
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
                        color = android.R.color.holo_green_dark;
                        break;
                    case "đã từ chối":
                    case "da tu choi":
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
            
            // Show/hide approve and reject buttons based on status
            boolean isPending = status != null && status.toLowerCase().contains("chờ");
            binding.btnApprove.setVisibility(isPending ? android.view.View.VISIBLE : android.view.View.GONE);
            binding.btnReject.setVisibility(isPending ? android.view.View.VISIBLE : android.view.View.GONE);
            
            // Setup button clicks
            binding.btnApprove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onApproveClick(booking.getMaDatXe());
                }
            });
            
            binding.btnReject.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRejectClick(booking.getMaDatXe());
                }
            });
            
            binding.btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(booking.getMaDatXe());
                }
            });
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

