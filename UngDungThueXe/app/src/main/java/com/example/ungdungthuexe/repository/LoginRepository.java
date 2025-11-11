package com.example.ungdungthuexe.repository;

import com.example.ungdungthuexe.api.ApiService;
import com.example.ungdungthuexe.api.RetrofitClient;
import com.example.ungdungthuexe.model.LoginRequest;
import com.example.ungdungthuexe.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private ApiService apiService;

    public LoginRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public void login(LoginRequest request, LoginCallback callback) {
        Call<LoginResponse> call = apiService.login(request);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    // Kiểm tra success flag
                    if (loginResponse.isSuccess()) {
                        callback.onSuccess(loginResponse);
                    } else {
                        // Nếu success = false, lấy message từ response
                        String errorMsg = loginResponse.getMessage();
                        if (errorMsg == null || errorMsg.isEmpty()) {
                            errorMsg = "Đăng nhập thất bại";
                        }
                        callback.onError(errorMsg);
                    }
                } else {
                    // Parse error response nếu có
                    String errorMsg = "Đăng nhập thất bại";
                    if (response.errorBody() != null) {
                        try {
                            // Có thể parse error body ở đây nếu cần
                            errorMsg = "Tên đăng nhập hoặc mật khẩu không đúng";
                        } catch (Exception e) {
                            errorMsg = "Đăng nhập thất bại";
                        }
                    }
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String errorMessage = "Lỗi kết nối: ";
                
                // Kiểm tra loại lỗi và đưa ra thông báo phù hợp
                if (t.getMessage() != null) {
                    String message = t.getMessage().toLowerCase();
                    if (message.contains("failed to connect") || message.contains("connection refused")) {
                        errorMessage += "Không thể kết nối đến server.\n" +
                                      "Vui lòng kiểm tra:\n" +
                                      "1. Server backend đang chạy trên port 5192\n" +
                                      "2. Server đang lắng nghe trên 0.0.0.0 (không phải chỉ localhost)\n" +
                                      "3. Firewall không chặn kết nối";
                    } else if (message.contains("timeout")) {
                        errorMessage += "Hết thời gian chờ kết nối. Vui lòng thử lại.";
                    } else if (message.contains("network")) {
                        errorMessage += "Lỗi mạng. Vui lòng kiểm tra kết nối internet.";
                    } else {
                        errorMessage += t.getMessage();
                    }
                } else {
                    errorMessage += "Không xác định được lỗi. Vui lòng thử lại.";
                }
                
                callback.onError(errorMessage);
            }
        });
    }

    public interface LoginCallback {
        void onSuccess(LoginResponse response);
        void onError(String error);
    }
}

