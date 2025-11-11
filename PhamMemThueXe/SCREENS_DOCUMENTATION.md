# 📱 Danh sách Giao diện/Màn hình - Phần mềm Quản lý Thuê Xe

## 📊 Tổng quan
- **Tổng số màn hình:** 33 màn hình
- **Phân quyền:** Public, Admin, Ketoan, Customer

---

## 🌐 PUBLIC (Không cần đăng nhập)

### 1. **Trang chủ**
- **Route:** `/Home/Index`
- **File:** `Views/Home/Index.cshtml`
- **Mô tả:** 
  - Hiển thị thông tin hệ thống
  - Danh sách xe cho thuê (cho user đã đăng nhập)
  - Bộ lọc và tìm kiếm xe
  - Đăng nhập/Đăng ký nếu chưa đăng nhập

---

## 🔐 AUTHENTICATION (Xác thực)

### 2. **Đăng nhập**
- **Route:** `/Auth/Login`
- **File:** `Views/Auth/Login.cshtml`
- **Mô tả:** Form đăng nhập với username/password
- **API:** `POST /api/AuthApi/login`

### 3. **Đăng ký**
- **Route:** `/Auth/Register`
- **File:** `Views/Auth/Register.cshtml`
- **Mô tả:** Form đăng ký tài khoản khách hàng mới
- **API:** `POST /api/AuthApi/register`

---

## 👤 ADMIN (Quản trị viên)

### 4. **Dashboard Admin**
- **Route:** `/Admin/Index`
- **File:** `Views/Admin/Index.cshtml`
- **Mô tả:** Trang tổng quan quản trị hệ thống

### 5. **Quản lý Tài khoản Kế toán**
- **Route:** `/Admin/Accountants`
- **File:** `Views/Admin/Accountants.cshtml`
- **Mô tả:** 
  - Danh sách tài khoản kế toán
  - Tạo/Sửa/Xóa tài khoản kế toán
- **API:** `GET/POST/PUT/DELETE /api/AdminApi/accountants`

### 6. **Tạo Tài khoản Kế toán**
- **Route:** `/Admin/CreateAccountant`
- **File:** `Views/Admin/CreateAccountant.cshtml`
- **Mô tả:** Form tạo tài khoản kế toán mới (Modal trong Accountants.cshtml)

### 7. **Sửa Tài khoản Kế toán**
- **Route:** `/Admin/EditAccountant/{id}`
- **File:** `Views/Admin/EditAccountant.cshtml`
- **Mô tả:** Form sửa thông tin tài khoản kế toán (Modal trong Accountants.cshtml)

### 8. **Quản lý Loại Xe**
- **Route:** `/Admin/LoaiXes`
- **File:** `Views/Admin/LoaiXes.cshtml`
- **Mô tả:** 
  - Danh sách loại xe
  - Tạo/Sửa/Xóa loại xe
  - Xem xe theo loại
- **API:** `GET/POST/PUT/DELETE /api/LoaiXeApi`

### 9. **Tạo Loại Xe**
- **Route:** `/Admin/CreateLoaiXe`
- **File:** `Views/Admin/CreateLoaiXe.cshtml`
- **Mô tả:** Form tạo loại xe mới (Modal trong LoaiXes.cshtml)

### 10. **Sửa Loại Xe**
- **Route:** `/Admin/EditLoaiXe/{id}`
- **File:** `Views/Admin/EditLoaiXe.cshtml`
- **Mô tả:** Form sửa thông tin loại xe (Modal trong LoaiXes.cshtml)

### 11. **Quản lý Xe**
- **Route:** `/Admin/Xes`
- **File:** `Views/Admin/Xes.cshtml`
- **Mô tả:** 
  - Danh sách tất cả xe
  - Tạo/Sửa/Xóa xe
  - Cập nhật trạng thái xe
  - Upload hình ảnh xe (ImageURL)
- **API:** `GET/POST/PUT/DELETE /api/XeApi`, `PUT /api/XeApi/{id}/status`

### 12. **Tạo Xe**
- **Route:** `/Admin/CreateXe`
- **File:** `Views/Admin/CreateXe.cshtml`
- **Mô tả:** Form tạo xe mới (Modal trong Xes.cshtml)

### 13. **Sửa Xe**
- **Route:** `/Admin/EditXe/{id}`
- **File:** `Views/Admin/EditXe.cshtml`
- **Mô tả:** Form sửa thông tin xe (Modal trong Xes.cshtml)

### 14. **Quản lý Bảng Giá**
- **Route:** `/Admin/BangGias`
- **File:** `Views/Admin/BangGias.cshtml`
- **Mô tả:** 
  - Danh sách bảng giá
  - Tạo/Sửa/Xóa bảng giá
  - Cập nhật giá theo loại xe
- **API:** `GET/POST/PUT/DELETE /api/BangGiaApi`, `POST /api/BangGiaApi/update-price`

### 15. **Tạo Bảng Giá**
- **Route:** `/Admin/CreateBangGia`
- **File:** `Views/Admin/CreateBangGia.cshtml`
- **Mô tả:** Form tạo bảng giá mới (Modal trong BangGias.cshtml)

### 16. **Thống kê**
- **Route:** `/Admin/Statistics`
- **File:** `Views/Admin/Statistics.cshtml`
- **Mô tả:** 
  - Thống kê tổng quan hệ thống
  - Biểu đồ và số liệu
- **API:** `GET /api/AdminApi/statistics`

---

## 💼 ACCOUNTANT (Kế toán)

### 17. **Dashboard Kế toán**
- **Route:** `/Accountant/Index`
- **File:** `Views/Accountant/Index.cshtml`
- **Mô tả:** Trang tổng quan kế toán

### 18. **Quản lý Phiếu Đặt Xe**
- **Route:** `/Accountant/PhieuDatXes`
- **File:** `Views/Accountant/PhieuDatXes.cshtml`
- **Mô tả:** 
  - Danh sách phiếu đặt xe
  - Cập nhật trạng thái phiếu đặt xe
  - Xóa phiếu đặt xe
- **API:** `GET /api/PhieuDatXeApi`, `PUT /api/PhieuDatXeApi/{id}/status`, `DELETE /api/PhieuDatXeApi/{id}`

### 19. **Tạo Phiếu Đặt Xe**
- **Route:** `/Accountant/CreatePhieuDatXe`
- **File:** `Views/Accountant/CreatePhieuDatXe.cshtml`
- **Mô tả:** Form tạo phiếu đặt xe mới
- **API:** `POST /api/PhieuDatXeApi`

### 20. **Quản lý Hợp đồng**
- **Route:** `/Accountant/HopDongs`
- **File:** `Views/Accountant/HopDongs.cshtml`
- **Mô tả:** 
  - Danh sách hợp đồng
  - Cập nhật trạng thái hợp đồng
  - Link đến tạo hóa đơn
- **API:** `GET /api/HopDongApi`, `PUT /api/HopDongApi/{id}/status`

### 21. **Tạo Hợp đồng**
- **Route:** `/Accountant/CreateHopDong`
- **File:** `Views/Accountant/CreateHopDong.cshtml`
- **Mô tả:** 
  - Form tạo hợp đồng thuê xe
  - Chọn khách hàng, phiếu đặt xe (tự động chọn xe)
  - Chọn nhiều xe
  - Tự động tính số ngày thuê và tổng tiền
  - Tự động cập nhật trạng thái xe thành "Đang cho thuê"
- **API:** `POST /api/HopDongApi`

### 22. **Quản lý Hóa đơn**
- **Route:** `/Accountant/HoaDons`
- **File:** `Views/Accountant/HoaDons.cshtml`
- **Mô tả:** 
  - Danh sách hóa đơn
  - Xem chi tiết hóa đơn
- **API:** `GET /api/HoaDonApi`

### 23. **Tạo Hóa đơn**
- **Route:** `/Accountant/CreateHoaDon`
- **File:** `Views/Accountant/CreateHoaDon.cshtml`
- **Mô tả:** 
  - Form tạo hóa đơn
  - Hiển thị thông tin thanh toán (đã thanh toán, còn lại)
- **API:** `POST /api/HoaDonApi`, `GET /api/HoaDonApi/payment-summary/{contractId}`

### 24. **Quản lý Khách hàng**
- **Route:** `/Accountant/KhachHangs`
- **File:** `Views/Accountant/KhachHangs.cshtml`
- **Mô tả:** 
  - Danh sách khách hàng
  - Tìm kiếm khách hàng
- **API:** `GET /api/KhachHangApi`, `GET /api/KhachHangApi/search`

### 25. **Tạo Khách hàng**
- **Route:** `/Accountant/CreateKhachHang`
- **File:** `Views/Accountant/CreateKhachHang.cshtml`
- **Mô tả:** Form thêm khách hàng mới
- **API:** `POST /api/KhachHangApi`

---

## 👥 CUSTOMER (Khách hàng)

### 26. **Trang chủ Khách hàng**
- **Route:** `/Customer/Index`
- **File:** `Views/Customer/Index.cshtml`
- **Mô tả:** 
  - Hiển thị xe sẵn sàng cho thuê
  - Lọc theo loại xe
  - Xem giá thuê
  - Click vào xe để đặt xe
  - Action buttons: Phiếu đặt xe, Hợp đồng, Hóa đơn
- **API:** `GET /api/XeApi/available`, `GET /api/LoaiXeApi`, `GET /api/BangGiaApi/price-for-type/{id}`

### 27. **Đặt xe (Chi tiết)**
- **Route:** `/Customer/BookCar?maXe={id}`
- **File:** `Views/Customer/BookCar.cshtml`
- **Mô tả:** 
  - Hiển thị chi tiết xe (hình ảnh, thông tin, giá)
  - Form đặt xe với ngày dự kiến nhận
  - Validation ngày phải trong tương lai
- **API:** `GET /api/XeApi/{id}`, `GET /api/BangGiaApi/price-for-type/{id}`, `POST /api/PhieuDatXeApi`

### 28. **Phiếu Đặt Xe của tôi**
- **Route:** `/Customer/PhieuDatXes`
- **File:** `Views/Customer/PhieuDatXes.cshtml`
- **Mô tả:** 
  - Danh sách phiếu đặt xe của khách hàng đã đăng nhập
  - Xem trạng thái phiếu đặt xe
- **API:** `GET /api/PhieuDatXeApi/by-username/{username}`

### 29. **Tạo Phiếu Đặt Xe**
- **Route:** `/Customer/CreatePhieuDatXe`
- **File:** `Views/Customer/CreatePhieuDatXe.cshtml`
- **Mô tả:** Form tạo phiếu đặt xe mới
- **API:** `POST /api/PhieuDatXeApi`

### 30. **Hợp đồng của tôi**
- **Route:** `/Customer/HopDongs`
- **File:** `Views/Customer/HopDongs.cshtml`
- **Mô tả:** 
  - Danh sách hợp đồng của khách hàng đã đăng nhập
  - Xem chi tiết hợp đồng
- **API:** `GET /api/HopDongApi/by-customer/{customerId}`

### 31. **Hóa đơn của tôi**
- **Route:** `/Customer/HoaDons`
- **File:** `Views/Customer/HoaDons.cshtml`
- **Mô tả:** 
  - Danh sách hóa đơn của khách hàng đã đăng nhập
  - Xem chi tiết hóa đơn
- **API:** `GET /api/HoaDonApi/by-customer/{customerId}`

---

## 🔧 SHARED COMPONENTS

### 32. **Layout (Chung)**
- **File:** `Views/Shared/_Layout.cshtml`
- **Mô tả:** 
  - Header với navigation menu
  - Footer
  - Responsive design
  - User dropdown menu

### 33. **Validation Scripts**
- **File:** `Views/Shared/_ValidationScriptsPartial.cshtml`
- **Mô tả:** Client-side validation scripts

---

## 📈 Thống kê theo Phân quyền

| Phân quyền | Số lượng màn hình | Tỷ lệ |
|------------|------------------|-------|
| **Public** | 1 | 3% |
| **Auth** | 2 | 6% |
| **Admin** | 13 | 39% |
| **Accountant** | 9 | 27% |
| **Customer** | 6 | 18% |
| **Shared** | 2 | 6% |
| **Tổng** | **33** | **100%** |

---

## 🎯 Tính năng nổi bật

### ✨ Responsive Design
- Tất cả màn hình đều responsive
- Mobile-friendly
- Tablet optimized

### 🎨 Modern UI/UX
- Gradient backgrounds
- Smooth animations
- Hover effects
- Card-based design
- Modern color scheme

### 🔄 API Integration
- **100%** các màn hình sử dụng API
- Client-side rendering
- Real-time updates
- Error handling

### 🔒 Security
- Role-based access control
- Authentication required
- Secure API endpoints
- Input validation

---

## 📝 Ghi chú

- Tất cả các form Create/Edit đã được chuyển sang sử dụng API
- Các màn hình danh sách đều có pagination và search
- Trạng thái xe tự động cập nhật khi tạo/hoàn thành hợp đồng
- Tính toán tự động số ngày thuê và tổng tiền

---

**Cập nhật lần cuối:** 2025-01-31

