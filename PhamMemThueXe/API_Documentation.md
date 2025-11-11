# API Documentation - Phần mềm quản lý thuê xe

## Tổng quan
Hệ thống API RESTful thuần túy cho phần mềm quản lý thuê xe với các chức năng đầy đủ cho Admin, Kế toán và Khách hàng. 
**Lưu ý**: Đây là API-only application, không có MVC views.

## Base URL
```
https://localhost:5001/api/
http://localhost:5000/api/
```

## Authentication
Hệ thống sử dụng Cookie Authentication. Tất cả API endpoints (trừ Auth) đều yêu cầu đăng nhập.

## API Endpoints

### 1. Authentication API (`/api/AuthApi`)

#### POST `/api/AuthApi/login`
Đăng nhập hệ thống
- **Body**: `LoginViewModel`
- **Response**: Thông tin user và token

#### POST `/api/AuthApi/register`
Đăng ký tài khoản khách hàng
- **Body**: `RegisterViewModel`
- **Response**: Thông tin khách hàng đã tạo

#### POST `/api/AuthApi/logout`
Đăng xuất
- **Authorization**: Required
- **Response**: Thông báo đăng xuất thành công

#### GET `/api/AuthApi/me`
Lấy thông tin user hiện tại
- **Authorization**: Required
- **Response**: Thông tin user

#### GET `/api/AuthApi/check-auth`
Kiểm tra trạng thái đăng nhập
- **Response**: Trạng thái đăng nhập và thông tin user

---

### 2. Admin API (`/api/AdminApi`)

#### GET `/api/AdminApi/accountants`
Lấy danh sách tài khoản kế toán
- **Authorization**: Admin only
- **Response**: Danh sách tài khoản kế toán

#### GET `/api/AdminApi/accountants/{id}`
Lấy thông tin tài khoản kế toán
- **Authorization**: Admin only
- **Response**: Thông tin tài khoản kế toán

#### POST `/api/AdminApi/accountants`
Tạo tài khoản kế toán mới
- **Authorization**: Admin only
- **Body**: `CreateAccountantViewModel`
- **Response**: Thông tin tài khoản đã tạo

#### PUT `/api/AdminApi/accountants/{id}`
Cập nhật tài khoản kế toán
- **Authorization**: Admin only
- **Body**: `CreateAccountantViewModel`
- **Response**: Thông tin tài khoản đã cập nhật

#### DELETE `/api/AdminApi/accountants/{id}`
Xóa tài khoản kế toán
- **Authorization**: Admin only
- **Response**: Thông báo xóa thành công

#### GET `/api/AdminApi/statistics`
Lấy thống kê tổng quan
- **Authorization**: Admin only
- **Response**: Thống kê hệ thống

---

### 3. Xe API (`/api/XeApi`)

#### GET `/api/XeApi`
Lấy danh sách tất cả xe
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách xe với thông tin loại xe

#### GET `/api/XeApi/{id}`
Lấy thông tin xe theo ID
- **Authorization**: Admin, Ketoan
- **Response**: Thông tin chi tiết xe

#### POST `/api/XeApi`
Thêm xe mới
- **Authorization**: Admin only
- **Body**: `Xe`
- **Response**: Thông tin xe đã tạo

#### PUT `/api/XeApi/{id}`
Cập nhật thông tin xe
- **Authorization**: Admin only
- **Body**: `Xe`
- **Response**: Thông tin xe đã cập nhật

#### DELETE `/api/XeApi/{id}`
Xóa xe
- **Authorization**: Admin only
- **Response**: Thông báo xóa thành công

#### GET `/api/XeApi/available`
Lấy danh sách xe sẵn sàng
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách xe có trạng thái "Sẵn sàng"

#### GET `/api/XeApi/by-type/{loaiXeId}`
Lấy xe theo loại
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách xe theo loại

#### PUT `/api/XeApi/{id}/status`
Cập nhật trạng thái xe
- **Authorization**: Admin, Ketoan
- **Body**: `UpdateXeStatusRequest`
- **Response**: Thông tin xe đã cập nhật

---

### 4. LoaiXe API (`/api/LoaiXeApi`)

#### GET `/api/LoaiXeApi`
Lấy danh sách loại xe
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách loại xe với số lượng xe

#### GET `/api/LoaiXeApi/{id}`
Lấy thông tin loại xe
- **Authorization**: Admin, Ketoan
- **Response**: Thông tin chi tiết loại xe

#### POST `/api/LoaiXeApi`
Thêm loại xe mới
- **Authorization**: Admin only
- **Body**: `LoaiXe`
- **Response**: Thông tin loại xe đã tạo

#### PUT `/api/LoaiXeApi/{id}`
Cập nhật loại xe
- **Authorization**: Admin only
- **Body**: `LoaiXe`
- **Response**: Thông tin loại xe đã cập nhật

#### DELETE `/api/LoaiXeApi/{id}`
Xóa loại xe
- **Authorization**: Admin only
- **Response**: Thông báo xóa thành công

#### GET `/api/LoaiXeApi/{id}/xes`
Lấy xe theo loại xe
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách xe thuộc loại

#### GET `/api/LoaiXeApi/{id}/prices`
Lấy giá theo loại xe
- **Authorization**: Admin, Ketoan
- **Response**: Lịch sử giá của loại xe

---

### 5. KhachHang API (`/api/KhachHangApi`)

#### GET `/api/KhachHangApi`
Lấy danh sách khách hàng
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách khách hàng với số hợp đồng

#### GET `/api/KhachHangApi/{id}`
Lấy thông tin khách hàng
- **Authorization**: Admin, Ketoan
- **Response**: Thông tin chi tiết khách hàng

#### POST `/api/KhachHangApi`
Thêm khách hàng mới
- **Authorization**: Admin, Ketoan
- **Body**: `KhachHang`
- **Response**: Thông tin khách hàng đã tạo

#### PUT `/api/KhachHangApi/{id}`
Cập nhật khách hàng
- **Authorization**: Admin, Ketoan
- **Body**: `KhachHang`
- **Response**: Thông tin khách hàng đã cập nhật

#### DELETE `/api/KhachHangApi/{id}`
Xóa khách hàng
- **Authorization**: Admin only
- **Response**: Thông báo xóa thành công

#### GET `/api/KhachHangApi/search`
Tìm kiếm khách hàng
- **Authorization**: Admin, Ketoan
- **Query**: `keyword`
- **Response**: Danh sách khách hàng tìm được

#### GET `/api/KhachHangApi/{id}/contracts`
Lấy hợp đồng của khách hàng
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách hợp đồng

#### GET `/api/KhachHangApi/by-cmnd/{cmnd}`
Tìm khách hàng theo CMND
- **Authorization**: Admin, Ketoan
- **Response**: Thông tin khách hàng

---

### 6. PhieuDatXe API (`/api/PhieuDatXeApi`)

#### GET `/api/PhieuDatXeApi`
Lấy danh sách phiếu đặt xe
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách phiếu đặt xe

#### GET `/api/PhieuDatXeApi/{id}`
Lấy thông tin phiếu đặt xe
- **Authorization**: Admin, Ketoan, Customer
- **Response**: Thông tin chi tiết phiếu đặt xe

#### POST `/api/PhieuDatXeApi`
Tạo phiếu đặt xe mới
- **Authorization**: Admin, Ketoan, Customer
- **Body**: `CreatePhieuDatXeRequest`
- **Response**: Thông tin phiếu đặt xe đã tạo

#### PUT `/api/PhieuDatXeApi/{id}/status`
Cập nhật trạng thái phiếu đặt xe
- **Authorization**: Admin, Ketoan
- **Body**: `UpdatePhieuDatXeStatusRequest`
- **Response**: Thông tin phiếu đặt xe đã cập nhật

#### DELETE `/api/PhieuDatXeApi/{id}`
Xóa phiếu đặt xe
- **Authorization**: Admin, Ketoan
- **Response**: Thông báo xóa thành công

#### GET `/api/PhieuDatXeApi/by-customer/{customerId}`
Lấy phiếu đặt xe theo khách hàng
- **Authorization**: Admin, Ketoan, Customer
- **Response**: Danh sách phiếu đặt xe của khách hàng

#### GET `/api/PhieuDatXeApi/by-status/{status}`
Lấy phiếu đặt xe theo trạng thái
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách phiếu đặt xe theo trạng thái

---

### 7. HopDong API (`/api/HopDongApi`)

#### GET `/api/HopDongApi`
Lấy danh sách hợp đồng
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách hợp đồng với chi tiết

#### GET `/api/HopDongApi/{id}`
Lấy thông tin hợp đồng
- **Authorization**: Admin, Ketoan
- **Response**: Thông tin chi tiết hợp đồng

#### POST `/api/HopDongApi`
Tạo hợp đồng mới
- **Authorization**: Admin, Ketoan
- **Body**: `CreateHopDongRequest`
- **Response**: Thông tin hợp đồng đã tạo

#### PUT `/api/HopDongApi/{id}/status`
Cập nhật trạng thái hợp đồng
- **Authorization**: Admin, Ketoan
- **Body**: `UpdateHopDongStatusRequest`
- **Response**: Thông tin hợp đồng đã cập nhật

#### GET `/api/HopDongApi/by-customer/{customerId}`
Lấy hợp đồng theo khách hàng
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách hợp đồng của khách hàng

#### GET `/api/HopDongApi/by-status/{status}`
Lấy hợp đồng theo trạng thái
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách hợp đồng theo trạng thái

#### GET `/api/HopDongApi/statistics`
Lấy thống kê hợp đồng
- **Authorization**: Admin, Ketoan
- **Response**: Thống kê hợp đồng và doanh thu

---

### 8. HoaDon API (`/api/HoaDonApi`)

#### GET `/api/HoaDonApi`
Lấy danh sách hóa đơn
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách hóa đơn

#### GET `/api/HoaDonApi/{id}`
Lấy thông tin hóa đơn
- **Authorization**: Admin, Ketoan
- **Response**: Thông tin chi tiết hóa đơn

#### POST `/api/HoaDonApi`
Tạo hóa đơn mới
- **Authorization**: Admin, Ketoan
- **Body**: `CreateHoaDonRequest`
- **Response**: Thông tin hóa đơn đã tạo

#### GET `/api/HoaDonApi/by-contract/{contractId}`
Lấy hóa đơn theo hợp đồng
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách hóa đơn của hợp đồng

#### GET `/api/HoaDonApi/by-customer/{customerId}`
Lấy hóa đơn theo khách hàng
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách hóa đơn của khách hàng

#### GET `/api/HoaDonApi/statistics`
Lấy thống kê hóa đơn
- **Authorization**: Admin, Ketoan
- **Response**: Thống kê hóa đơn và doanh thu

#### GET `/api/HoaDonApi/payment-summary/{contractId}`
Lấy tóm tắt thanh toán hợp đồng
- **Authorization**: Admin, Ketoan
- **Response**: Tóm tắt thanh toán và số dư

---

### 9. BangGia API (`/api/BangGiaApi`)

#### GET `/api/BangGiaApi`
Lấy danh sách bảng giá
- **Authorization**: Admin, Ketoan
- **Response**: Danh sách bảng giá

#### GET `/api/BangGiaApi/{id}`
Lấy thông tin bảng giá
- **Authorization**: Admin, Ketoan
- **Response**: Thông tin chi tiết bảng giá

#### POST `/api/BangGiaApi`
Thêm bảng giá mới
- **Authorization**: Admin only
- **Body**: `BangGia`
- **Response**: Thông tin bảng giá đã tạo

#### PUT `/api/BangGiaApi/{id}`
Cập nhật bảng giá
- **Authorization**: Admin only
- **Body**: `BangGia`
- **Response**: Thông tin bảng giá đã cập nhật

#### DELETE `/api/BangGiaApi/{id}`
Xóa bảng giá
- **Authorization**: Admin only
- **Response**: Thông báo xóa thành công

#### GET `/api/BangGiaApi/current`
Lấy giá hiện tại
- **Authorization**: Admin, Ketoan
- **Response**: Giá hiện tại của tất cả loại xe

#### GET `/api/BangGiaApi/by-type/{loaiXeId}`
Lấy giá theo loại xe
- **Authorization**: Admin, Ketoan
- **Response**: Lịch sử giá của loại xe

#### GET `/api/BangGiaApi/price-for-type/{loaiXeId}`
Lấy giá hiện tại của loại xe
- **Authorization**: Admin, Ketoan
- **Response**: Giá hiện tại của loại xe

#### POST `/api/BangGiaApi/update-price`
Cập nhật giá cho loại xe
- **Authorization**: Admin only
- **Body**: `UpdatePriceRequest`
- **Response**: Thông tin giá đã cập nhật

---

### 10. System API (`/api/SystemApi`)

#### GET `/api/SystemApi/info`
Lấy thông tin hệ thống
- **Authorization**: None required
- **Response**: Thông tin về hệ thống, tính năng và phân quyền

#### GET `/api/SystemApi/health`
Kiểm tra tình trạng hệ thống
- **Authorization**: None required
- **Response**: Trạng thái kết nối database và hệ thống

#### GET `/api/SystemApi/stats`
Lấy thống kê tổng quan hệ thống
- **Authorization**: None required
- **Response**: Thống kê tổng quan về dữ liệu hệ thống

---

## Response Format

Tất cả API responses đều có format chuẩn:

### Success Response
```json
{
  "success": true,
  "message": "Thông báo thành công",
  "data": { ... }
}
```

### Error Response
```json
{
  "success": false,
  "message": "Thông báo lỗi",
  "errors": { ... }
}
```

## Authorization Levels

- **Admin**: Toàn quyền truy cập tất cả API
- **Ketoan**: Truy cập các API nghiệp vụ (không bao gồm quản lý tài khoản)
- **Customer**: Chỉ truy cập API liên quan đến khách hàng

## Notes

- Tất cả API đều có validation đầy đủ
- Sử dụng Entity Framework với SQL Server
- Hỗ trợ phân trang và tìm kiếm
- Có thống kê và báo cáo
- Transaction support cho các thao tác phức tạp
