using Microsoft.EntityFrameworkCore;
using PhamMemThueXe.Models;

namespace PhamMemThueXe.Data
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options)
        {
        }

        // Danh mục
        public DbSet<LoaiXe> LoaiXes { get; set; }
        public DbSet<TaiKhoan> TaiKhoans { get; set; }
        public DbSet<KhachHang> KhachHangs { get; set; }
        public DbSet<Xe> Xes { get; set; }
        public DbSet<BangGia> BangGias { get; set; }

        // Nghiệp vụ
        public DbSet<PhieuDatXe> PhieuDatXes { get; set; }
        public DbSet<HopDong> HopDongs { get; set; }
        public DbSet<ChiTietHopDong> ChiTietHopDongs { get; set; }
        public DbSet<HoaDon> HoaDons { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // Cấu hình các ràng buộc và quan hệ
            modelBuilder.Entity<TaiKhoan>()
                .HasIndex(t => t.TenDangNhap)
                .IsUnique();

            modelBuilder.Entity<KhachHang>()
                .HasIndex(k => k.SoCMND)
                .IsUnique();

            modelBuilder.Entity<Xe>()
                .HasIndex(x => x.BienSoXe)
                .IsUnique();

            // Cấu hình ImageURL cho Xe (cho phép null và map đúng tên cột)
            // Nếu cột trong DB là NVARCHAR(MAX), không cần HasMaxLength
            modelBuilder.Entity<Xe>()
                .Property(x => x.ImageURL)
                .HasColumnType("NVARCHAR(MAX)")  // Khớp với kiểu dữ liệu trong DB
                .IsRequired(false);

            // Cấu hình foreign key cho Xe -> LoaiXe
            modelBuilder.Entity<Xe>()
                .HasOne(x => x.LoaiXe)
                .WithMany()
                .HasForeignKey(x => x.MaLoaiXe)
                .OnDelete(DeleteBehavior.Restrict);

            // Cấu hình foreign key cho BangGia -> LoaiXe
            modelBuilder.Entity<BangGia>()
                .HasOne(bg => bg.LoaiXe)
                .WithMany()
                .HasForeignKey(bg => bg.MaLoaiXe)
                .OnDelete(DeleteBehavior.Restrict);

            // Cấu hình foreign key cho PhieuDatXe -> KhachHang
            modelBuilder.Entity<PhieuDatXe>()
                .HasOne(pdx => pdx.KhachHang)
                .WithMany()
                .HasForeignKey(pdx => pdx.MaKH)
                .OnDelete(DeleteBehavior.Restrict);

            // Cấu hình foreign key cho PhieuDatXe -> LoaiXe
            modelBuilder.Entity<PhieuDatXe>()
                .HasOne(pdx => pdx.LoaiXe)
                .WithMany()
                .HasForeignKey(pdx => pdx.MaLoaiXe)
                .OnDelete(DeleteBehavior.Restrict);

            // Cấu hình foreign key cho HopDong -> KhachHang
            modelBuilder.Entity<HopDong>()
                .HasOne(hd => hd.KhachHang)
                .WithMany()
                .HasForeignKey(hd => hd.MaKH)
                .OnDelete(DeleteBehavior.Restrict);

            // Cấu hình foreign key cho HopDong -> NhanVien (TaiKhoan)
            modelBuilder.Entity<HopDong>()
                .HasOne(hd => hd.NhanVien)
                .WithMany()
                .HasForeignKey(hd => hd.MaNhanVien)
                .OnDelete(DeleteBehavior.Restrict);

            // Cấu hình foreign key cho HopDong -> PhieuDatXe (optional)
            modelBuilder.Entity<HopDong>()
                .HasOne(hd => hd.PhieuDatXe)
                .WithMany()
                .HasForeignKey(hd => hd.MaDatXe)
                .OnDelete(DeleteBehavior.Restrict);

            // Cấu hình foreign key cho ChiTietHopDong -> HopDong
            modelBuilder.Entity<ChiTietHopDong>()
                .HasOne(cthd => cthd.HopDong)
                .WithMany(hd => hd.ChiTietHopDongs)
                .HasForeignKey(cthd => cthd.MaHopDong)
                .OnDelete(DeleteBehavior.Restrict);

            // Cấu hình foreign key cho ChiTietHopDong -> Xe
            modelBuilder.Entity<ChiTietHopDong>()
                .HasOne(cthd => cthd.Xe)
                .WithMany()
                .HasForeignKey(cthd => cthd.MaXe)
                .OnDelete(DeleteBehavior.Restrict);

            // Cấu hình composite key cho ChiTietHopDong
            modelBuilder.Entity<ChiTietHopDong>()
                .HasKey(ct => new { ct.MaHopDong, ct.MaXe });

            // Cấu hình foreign key cho HoaDon -> HopDong
            modelBuilder.Entity<HoaDon>()
                .HasOne(hd => hd.HopDong)
                .WithMany(hd => hd.HoaDons)
                .HasForeignKey(hd => hd.MaHopDong)
                .OnDelete(DeleteBehavior.Restrict);

            // Cấu hình foreign key cho HoaDon -> NhanVien (TaiKhoan)
            modelBuilder.Entity<HoaDon>()
                .HasOne(hd => hd.NhanVien)
                .WithMany()
                .HasForeignKey(hd => hd.MaNhanVien)
                .OnDelete(DeleteBehavior.Restrict);

            // Seed data cho Admin
            modelBuilder.Entity<TaiKhoan>().HasData(
                new TaiKhoan
                {
                    MaNhanVien = 1,
                    TenDangNhap = "admin",
                    MatKhauHash = BCrypt.Net.BCrypt.HashPassword("admin123"),
                    HoTen = "Quản trị viên hệ thống",
                    Quyen = "Admin"
                }
            );

            // Seed data cho loại xe
            modelBuilder.Entity<LoaiXe>().HasData(
                new LoaiXe { MaLoaiXe = 1, TenLoaiXe = "Xe 4 chỗ", MoTa = "Xe sedan, hatchback 4 chỗ ngồi" },
                new LoaiXe { MaLoaiXe = 2, TenLoaiXe = "Xe 7 chỗ", MoTa = "Xe SUV, crossover 7 chỗ ngồi" },
                new LoaiXe { MaLoaiXe = 3, TenLoaiXe = "Xe 16 chỗ", MoTa = "Xe minibus 16 chỗ ngồi" }
            );

            // Seed data cho bảng giá
            modelBuilder.Entity<BangGia>().HasData(
                new BangGia { MaBangGia = 1, MaLoaiXe = 1, DonGiaTheoNgay = 500000, NgayApDung = DateTime.Today },
                new BangGia { MaBangGia = 2, MaLoaiXe = 2, DonGiaTheoNgay = 800000, NgayApDung = DateTime.Today },
                new BangGia { MaBangGia = 3, MaLoaiXe = 3, DonGiaTheoNgay = 1200000, NgayApDung = DateTime.Today }
            );
        }
    }
}
