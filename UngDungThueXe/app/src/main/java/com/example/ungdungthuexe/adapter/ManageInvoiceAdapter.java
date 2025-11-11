package com.example.ungdungthuexe.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungthuexe.databinding.ItemManageInvoiceBinding;
import com.example.ungdungthuexe.model.ManageInvoiceResponse;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ManageInvoiceAdapter extends RecyclerView.Adapter<ManageInvoiceAdapter.ManageInvoiceViewHolder> {
    private List<ManageInvoiceResponse> invoiceList;
    private SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    public ManageInvoiceAdapter(List<ManageInvoiceResponse> invoiceList) {
        this.invoiceList = invoiceList;
    }

    @NonNull
    @Override
    public ManageInvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemManageInvoiceBinding binding = ItemManageInvoiceBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ManageInvoiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageInvoiceViewHolder holder, int position) {
        ManageInvoiceResponse invoice = invoiceList.get(position);
        holder.bind(invoice);
    }

    @Override
    public int getItemCount() {
        return invoiceList != null ? invoiceList.size() : 0;
    }

    public void updateList(List<ManageInvoiceResponse> newList) {
        this.invoiceList = newList;
        notifyDataSetChanged();
    }

    class ManageInvoiceViewHolder extends RecyclerView.ViewHolder {
        private ItemManageInvoiceBinding binding;

        public ManageInvoiceViewHolder(@NonNull ItemManageInvoiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ManageInvoiceResponse invoice) {
            binding.tvMaHoaDon.setText("Hóa đơn #" + invoice.getMaHoaDon());
            binding.tvTenKhachHang.setText("KH: " + invoice.getHopDong().getKhachHang().getHoTen());
            binding.tvMaHopDong.setText("Hợp đồng: #" + invoice.getMaHopDong());
            
            // Format amount
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedAmount = formatter.format(invoice.getSoTienThanhToan());
            binding.tvSoTienThanhToan.setText(formattedAmount + " VND");
            
            // Format date
            try {
                binding.tvLoaiThanhToan.setText("Loại: " + invoice.getLoaiThanhToan());
            } catch (Exception e) {
                binding.tvLoaiThanhToan.setText("Loại: " + invoice.getLoaiThanhToan());
            }
            
            // Format ngayLap if needed (optional)
            // You can add date display later if required
        }
    }
}

