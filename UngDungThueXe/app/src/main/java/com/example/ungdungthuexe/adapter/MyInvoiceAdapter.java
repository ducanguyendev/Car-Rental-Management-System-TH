package com.example.ungdungthuexe.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungthuexe.databinding.ItemMyInvoiceBinding;
import com.example.ungdungthuexe.model.MyInvoice;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyInvoiceAdapter extends RecyclerView.Adapter<MyInvoiceAdapter.MyInvoiceViewHolder> {
    private List<MyInvoice> invoiceList;

    public MyInvoiceAdapter(List<MyInvoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    @NonNull
    @Override
    public MyInvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyInvoiceBinding binding = ItemMyInvoiceBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MyInvoiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyInvoiceViewHolder holder, int position) {
        MyInvoice invoice = invoiceList.get(position);
        holder.bind(invoice);
    }

    @Override
    public int getItemCount() {
        return invoiceList != null ? invoiceList.size() : 0;
    }

    public void updateList(List<MyInvoice> newList) {
        this.invoiceList = newList;
        notifyDataSetChanged();
    }

    class MyInvoiceViewHolder extends RecyclerView.ViewHolder {
        private ItemMyInvoiceBinding binding;

        public MyInvoiceViewHolder(@NonNull ItemMyInvoiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MyInvoice invoice) {
            binding.tvMaHoaDon.setText("Hóa đơn #" + invoice.getMaHoaDon());
            binding.tvMaHopDong.setText("Hợp đồng: #" + invoice.getMaHopDong());
            binding.tvNgayLap.setText("Ngày lập: " + formatDate(invoice.getNgayLap()));
            
            // Format payment amount
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedAmount = formatter.format(invoice.getSoTienThanhToan());
            binding.tvSoTienThanhToan.setText(formattedAmount + " VND");
            
            // Display payment type
            binding.tvLoaiThanhToan.setText("Loại: " + invoice.getLoaiThanhToan());
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

