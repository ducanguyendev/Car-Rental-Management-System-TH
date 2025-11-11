using System.ComponentModel.DataAnnotations;

namespace PhamMemThueXe.Models
{
    public class LoaiXe
    {
        [Key]
        public int MaLoaiXe { get; set; }
        
        [Required(ErrorMessage = "Tên loại xe là bắt buộc")]
        [StringLength(100, ErrorMessage = "Tên loại xe không được vượt quá 100 ký tự")]
        public string TenLoaiXe { get; set; } = string.Empty;
        
        public string? MoTa { get; set; }
    }

    public class TaiKhoan
    {
        [Key]
        public int MaNhanVien { get; set; }
        
        [Required(ErrorMessage = "Tên đăng nhập là bắt buộc")]
        [StringLength(50, ErrorMessage = "Tên đăng nhập không được vượt quá 50 ký tự")]
        public string TenDangNhap { get; set; } = string.Empty;
        
        [Required(ErrorMessage = "Mật khẩu là bắt buộc")]
        public string MatKhauHash { get; set; } = string.Empty;
        
        [Required(ErrorMessage = "Họ tên là bắt buộc")]
        [StringLength(100, ErrorMessage = "Họ tên không được vượt quá 100 ký tự")]
        public string HoTen { get; set; } = string.Empty;
        
        [Required(ErrorMessage = "Quyền là bắt buộc")]
        public string Quyen { get; set; } = string.Empty; // Admin, Ketoan
    }

    public class KhachHang
    {
        [Key]
        public int MaKH { get; set; }
        
        [Required(ErrorMessage = "Họ tên là bắt buộc")]
        [StringLength(100, ErrorMessage = "Họ tên không được vượt quá 100 ký tự")]
        public string HoTen { get; set; } = string.Empty;
        
        [Required(ErrorMessage = "Số CMND là bắt buộc")]
        [StringLength(20, ErrorMessage = "Số CMND không được vượt quá 20 ký tự")]
        public string SoCMND { get; set; } = string.Empty;
        
        [Required(ErrorMessage = "Số hộ khẩu là bắt buộc")]
        [StringLength(50, ErrorMessage = "Số hộ khẩu không được vượt quá 50 ký tự")]
        public string SoHoKhau { get; set; } = string.Empty;
        
        [Required(ErrorMessage = "Số điện thoại là bắt buộc")]
        [StringLength(15, ErrorMessage = "Số điện thoại không được vượt quá 15 ký tự")]
        public string SoDienThoai { get; set; } = string.Empty;
        
        public string? DiaChiCoQuan { get; set; }
    }

    public class Xe
    {
        [Key]
        public int MaXe { get; set; }
        
        [Required(ErrorMessage = "Loại xe là bắt buộc")]
        public int MaLoaiXe { get; set; }
        
        [Required(ErrorMessage = "Biển số xe là bắt buộc")]
        [StringLength(15, ErrorMessage = "Biển số xe không được vượt quá 15 ký tự")]
        public string BienSoXe { get; set; } = string.Empty;
        
        [Required(ErrorMessage = "Tên xe là bắt buộc")]
        [StringLength(100, ErrorMessage = "Tên xe không được vượt quá 100 ký tự")]
        public string TenXe { get; set; } = string.Empty;
        
        [Required(ErrorMessage = "Trạng thái là bắt buộc")]
        public string TrangThai { get; set; } = string.Empty; // Sẵn sàng, Đang cho thuê, Bảo trì
        
        [StringLength(500, ErrorMessage = "URL hình ảnh không được vượt quá 500 ký tự")]
        public string? ImageURL { get; set; }
        
        // Navigation properties
        public LoaiXe? LoaiXe { get; set; }
    }

    public class BangGia
    {
        [Key]
        public int MaBangGia { get; set; }
        
        [Required(ErrorMessage = "Loại xe là bắt buộc")]
        public int MaLoaiXe { get; set; }
        
        [Required(ErrorMessage = "Đơn giá là bắt buộc")]
        [Range(0, double.MaxValue, ErrorMessage = "Đơn giá phải lớn hơn 0")]
        public decimal DonGiaTheoNgay { get; set; }
        
        [Required(ErrorMessage = "Ngày áp dụng là bắt buộc")]
        public DateTime NgayApDung { get; set; }
        
        // Navigation properties
        public LoaiXe? LoaiXe { get; set; }
    }

    public class PhieuDatXe
    {
        [Key]
        public int MaDatXe { get; set; }
        
        [Required(ErrorMessage = "Khách hàng là bắt buộc")]
        public int MaKH { get; set; }
        
        [Required(ErrorMessage = "Loại xe là bắt buộc")]
        public int MaLoaiXe { get; set; }
        
        public string? HoTenKH { get; set; }
        public string? TenLoaiXeDat { get; set; }
        
        [Required(ErrorMessage = "Ngày đặt là bắt buộc")]
        public DateTime NgayDat { get; set; }
        
        [Required(ErrorMessage = "Ngày dự kiến nhận là bắt buộc")]
        public DateTime NgayDuKienNhan { get; set; }
        
        [Required(ErrorMessage = "Trạng thái là bắt buộc")]
        public string TrangThai { get; set; } = string.Empty; // Đang chờ, Đã xác nhận, Đã huỷ, Đã thành hợp đồng
        
        // Navigation properties
        public KhachHang? KhachHang { get; set; }
        public LoaiXe? LoaiXe { get; set; }
    }

    public class HopDong
    {
        [Key]
        public int MaHopDong { get; set; }
        
        [Required(ErrorMessage = "Khách hàng là bắt buộc")]
        public int MaKH { get; set; }
        
        [Required(ErrorMessage = "Nhân viên là bắt buộc")]
        public int MaNhanVien { get; set; }
        
        public int? MaDatXe { get; set; }
        
        public string? HoTenKH { get; set; }
        public string? SoDienThoaiKH { get; set; }
        public string? HoTenNVLap { get; set; }
        
        [Required(ErrorMessage = "Ngày lập là bắt buộc")]
        public DateTime NgayLap { get; set; }
        
        [Required(ErrorMessage = "Ngày nhận xe là bắt buộc")]
        public DateTime NgayNhanXe { get; set; }
        
        [Required(ErrorMessage = "Địa điểm nhận là bắt buộc")]
        [StringLength(255, ErrorMessage = "Địa điểm nhận không được vượt quá 255 ký tự")]
        public string DiaDiemNhan { get; set; } = string.Empty;
        
        [Required(ErrorMessage = "Ngày trả xe là bắt buộc")]
        public DateTime NgayTraXe { get; set; }
        
        [Required(ErrorMessage = "Địa điểm trả là bắt buộc")]
        [StringLength(255, ErrorMessage = "Địa điểm trả không được vượt quá 255 ký tự")]
        public string DiaDiemTra { get; set; } = string.Empty;
        
        [Required(ErrorMessage = "Tổng tiền là bắt buộc")]
        [Range(0, double.MaxValue, ErrorMessage = "Tổng tiền phải lớn hơn 0")]
        public decimal TongTien { get; set; }
        
        [Required(ErrorMessage = "Tiền đặt cọc là bắt buộc")]
        [Range(0, double.MaxValue, ErrorMessage = "Tiền đặt cọc phải lớn hơn 0")]
        public decimal TienDatCoc { get; set; }
        
        [Required(ErrorMessage = "Trạng thái là bắt buộc")]
        public string TrangThai { get; set; } = string.Empty; // Đang thuê, Đã hoàn thành, Đã huỷ
        
        // Navigation properties
        public KhachHang? KhachHang { get; set; }
        public TaiKhoan? NhanVien { get; set; }
        public PhieuDatXe? PhieuDatXe { get; set; }
        public ICollection<ChiTietHopDong> ChiTietHopDongs { get; set; } = new List<ChiTietHopDong>();
        public ICollection<HoaDon> HoaDons { get; set; } = new List<HoaDon>();
    }

    public class ChiTietHopDong
    {
        [Required(ErrorMessage = "Hợp đồng là bắt buộc")]
        public int MaHopDong { get; set; }
        
        [Required(ErrorMessage = "Xe là bắt buộc")]
        public int MaXe { get; set; }
        
        public string? BienSoXe { get; set; }
        public string? TenXe { get; set; }
        
        [Required(ErrorMessage = "Đơn giá tại thời điểm thuê là bắt buộc")]
        [Range(0, double.MaxValue, ErrorMessage = "Đơn giá phải lớn hơn 0")]
        public decimal DonGiaTaiThoiDiemThue { get; set; }
        
        public string? GhiChu { get; set; }
        
        // Navigation properties
        public HopDong? HopDong { get; set; }
        public Xe? Xe { get; set; }
    }

    public class HoaDon
    {
        [Key]
        public int MaHoaDon { get; set; }
        
        [Required(ErrorMessage = "Hợp đồng là bắt buộc")]
        public int MaHopDong { get; set; }
        
        [Required(ErrorMessage = "Nhân viên là bắt buộc")]
        public int MaNhanVien { get; set; }
        
        public string? HoTenNVLap { get; set; }
        
        [Required(ErrorMessage = "Ngày lập là bắt buộc")]
        public DateTime NgayLap { get; set; }
        
        [Required(ErrorMessage = "Số tiền thanh toán là bắt buộc")]
        [Range(0, double.MaxValue, ErrorMessage = "Số tiền phải lớn hơn 0")]
        public decimal SoTienThanhToan { get; set; }
        
        [Required(ErrorMessage = "Loại thanh toán là bắt buộc")]
        [StringLength(100, ErrorMessage = "Loại thanh toán không được vượt quá 100 ký tự")]
        public string LoaiThanhToan { get; set; } = string.Empty;
        
        // Navigation properties
        public HopDong? HopDong { get; set; }
        public TaiKhoan? NhanVien { get; set; }
    }
}
