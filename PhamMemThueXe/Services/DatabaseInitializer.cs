using Microsoft.EntityFrameworkCore;
using PhamMemThueXe.Data;
using PhamMemThueXe.Models;

namespace PhamMemThueXe.Services
{
    public class DatabaseInitializer
    {
        private readonly ApplicationDbContext _context;

        public DatabaseInitializer(ApplicationDbContext context)
        {
            _context = context;
        }

        public async Task InitializeAsync()
        {
            // Tạo database nếu chưa tồn tại
            await _context.Database.EnsureCreatedAsync();

            // Kiểm tra xem đã có dữ liệu chưa
            if (!await _context.TaiKhoans.AnyAsync())
            {
                await SeedDataAsync();
            }
        }

        private async Task SeedDataAsync()
        {
            // Tạo tài khoản Admin
            var admin = new TaiKhoan
            {
                TenDangNhap = "admin",
                MatKhauHash = BCrypt.Net.BCrypt.HashPassword("admin123"),
                HoTen = "Quản trị viên hệ thống",
                Quyen = "Admin"
            };
            _context.TaiKhoans.Add(admin);

            // Tạo các loại xe
            var loaiXe1 = new LoaiXe { TenLoaiXe = "Xe 4 chỗ", MoTa = "Xe sedan, hatchback 4 chỗ ngồi" };
            var loaiXe2 = new LoaiXe { TenLoaiXe = "Xe 7 chỗ", MoTa = "Xe SUV, crossover 7 chỗ ngồi" };
            var loaiXe3 = new LoaiXe { TenLoaiXe = "Xe 16 chỗ", MoTa = "Xe minibus 16 chỗ ngồi" };
            
            _context.LoaiXes.AddRange(loaiXe1, loaiXe2, loaiXe3);
            await _context.SaveChangesAsync();

            // Tạo bảng giá sau khi đã có ID của loại xe
            var bangGias = new List<BangGia>
            {
                new BangGia { MaLoaiXe = loaiXe1.MaLoaiXe, DonGiaTheoNgay = 500000, NgayApDung = DateTime.Today },
                new BangGia { MaLoaiXe = loaiXe2.MaLoaiXe, DonGiaTheoNgay = 800000, NgayApDung = DateTime.Today },
                new BangGia { MaLoaiXe = loaiXe3.MaLoaiXe, DonGiaTheoNgay = 1200000, NgayApDung = DateTime.Today }
            };
            _context.BangGias.AddRange(bangGias);

            await _context.SaveChangesAsync();
        }
    }
}
