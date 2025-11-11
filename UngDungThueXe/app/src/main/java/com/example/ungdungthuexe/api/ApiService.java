package com.example.ungdungthuexe.api;

import com.example.ungdungthuexe.model.BaseResponse;
import com.example.ungdungthuexe.model.BookingDetailsAccountant;
import com.example.ungdungthuexe.model.ChiTietHopDongs;
import com.example.ungdungthuexe.model.CreateCustomerRequest;
import com.example.ungdungthuexe.model.CreateCustomerResponse;
import com.example.ungdungthuexe.model.CreateHoaDonRequest;
import com.example.ungdungthuexe.model.CreateHopDongRequest;
import com.example.ungdungthuexe.model.GiaThue;
import com.example.ungdungthuexe.model.HopDongSpinnerItem;
import com.example.ungdungthuexe.model.KhachHangSpinner;
import com.example.ungdungthuexe.model.LoginRequest;
import com.example.ungdungthuexe.model.LoginResponse;
import com.example.ungdungthuexe.model.LoaiXe;
import com.example.ungdungthuexe.model.ManageContractResponse;
import com.example.ungdungthuexe.model.ManageCustomerResponse;
import com.example.ungdungthuexe.model.ManageInvoiceResponse;
import com.example.ungdungthuexe.model.MyBooking;
import com.example.ungdungthuexe.model.MyContract;
import com.example.ungdungthuexe.model.MyInvoice;
import com.example.ungdungthuexe.model.PhieuDatXeDuyet;
import com.example.ungdungthuexe.model.PhieuDatXeRequest;
import com.example.ungdungthuexe.model.PhieuDatXeResponse;
import com.example.ungdungthuexe.model.RegisterRequest;
import com.example.ungdungthuexe.model.RegisterResponse;
import com.example.ungdungthuexe.model.UpdateBookingStatusRequest;
import com.example.ungdungthuexe.model.UpdateContractStatusRequest;
import com.example.ungdungthuexe.model.Xe;
import com.example.ungdungthuexe.model.XeDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/AuthApi/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    
    @POST("api/AuthApi/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
    
    @GET("api/XeApi/available")
    Call<BaseResponse<List<Xe>>> getAvailableXe();
    
    @GET("api/LoaiXeApi")
    Call<BaseResponse<List<LoaiXe>>> getLoaiXe();
    
    @GET("api/XeApi/{id}")
    Call<BaseResponse<XeDetail>> getXeDetail(@Path("id") int maXe);
    
    @GET("api/BangGiaApi/price-for-type/{loaiXeId}")
    Call<BaseResponse<GiaThue>> getGiaThue(@Path("loaiXeId") int loaiXeId);
    
    @POST("api/PhieuDatXeApi")
    Call<PhieuDatXeResponse> createPhieuDatXe(@Body PhieuDatXeRequest request);
    
    @GET("api/PhieuDatXeApi/by-username/{username}")
    Call<BaseResponse<List<MyBooking>>> getMyBookings(@Path("username") String username);
    
    @GET("api/HopDongApi/by-username/{username}")
    Call<BaseResponse<List<MyContract>>> getMyContracts(@Path("username") String username);
    
    @GET("api/HoaDonApi/by-customer/{customerId}")
    Call<BaseResponse<List<MyInvoice>>> getMyInvoices(@Path("customerId") int customerId);
    
    @GET("api/PhieuDatXeApi")
    Call<BaseResponse<List<BookingDetailsAccountant>>> getAllBookings();
    
    @PUT("api/PhieuDatXeApi/{id}/status")
    Call<Void> updateBookingStatus(@Path("id") int bookingId, @Body UpdateBookingStatusRequest request);
    
    @DELETE("api/PhieuDatXeApi/{id}")
    Call<Void> deleteBooking(@Path("id") int bookingId);
    
    @GET("api/KhachHangApi")
    Call<BaseResponse<List<KhachHangSpinner>>> getKhachHangsForSpinner();
    
    @GET("api/PhieuDatXeApi/by-status")
    Call<BaseResponse<List<PhieuDatXeDuyet>>> getApprovedBookings(@Query("status") String status);
    
    @POST("api/HopDongApi")
    Call<Object> createHopDong(@Body CreateHopDongRequest request);
    
    @GET("api/HopDongApi")
    Call<BaseResponse<List<ManageContractResponse>>> getAllContracts();
    
    @PUT("api/HopDongApi/{id}/status")
    Call<Object> updateContractStatus(@Path("id") int contractId, @Body UpdateContractStatusRequest request);
    
    @GET("api/HoaDonApi")
    Call<BaseResponse<List<ManageInvoiceResponse>>> getAllInvoices();
    
    @GET("api/HopDongApi")
    Call<BaseResponse<List<HopDongSpinnerItem>>> getContractsForSpinner();
    
    @POST("api/HoaDonApi")
    Call<Object> createHoaDon(@Body CreateHoaDonRequest request);
    
    @GET("api/KhachHangApi")
    Call<BaseResponse<List<ManageCustomerResponse>>> getAllCustomers();
    
    @GET("api/KhachHangApi/search")
    Call<BaseResponse<List<ManageCustomerResponse>>> searchCustomers(@Query("keyword") String keyword);
    
    @POST("api/KhachHangApi")
    Call<CreateCustomerResponse> createCustomer(@Body CreateCustomerRequest request);
}

