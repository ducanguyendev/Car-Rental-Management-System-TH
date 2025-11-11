package com.example.ungdungthuexe.api;

import java.util.HashSet;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    // Sử dụng địa chỉ http://10.0.2.2:5192/ cho Android Emulator
    // 10.0.2.2 là địa chỉ đặc biệt của Android Emulator để trỏ đến localhost của máy host
    // Nếu test trên thiết bị thật, thay bằng IP máy tính của bạn (ví dụ: http://192.168.1.100:5192/)
    // 
    // LƯU Ý: Đảm bảo server backend đang chạy trên port 5192
    // Kiểm tra: Mở trình duyệt và truy cập http://localhost:5192/api/AuthApi/login
    private static final String BASE_URL = "http://10.0.2.2:5192/";
    private static RetrofitClient instance;
    private Retrofit retrofit;
    
    // Simple in-memory cookie store
    private static final Set<Cookie> cookieStore = new HashSet<>();

    private RetrofitClient() {
        // Tạo OkHttpClient với logging interceptor, cookie jar và timeout
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.addAll(cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        return new ArrayList<>(cookieStore);
                    }
                })
                .connectTimeout(30, TimeUnit.SECONDS) // Timeout cho kết nối
                .readTimeout(30, TimeUnit.SECONDS)    // Timeout cho đọc dữ liệu
                .writeTimeout(30, TimeUnit.SECONDS)   // Timeout cho ghi dữ liệu
                .build();

        // Tạo Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
    
    // Method để clear cookies khi logout
    public static void clearCookies() {
        cookieStore.clear();
    }
}

