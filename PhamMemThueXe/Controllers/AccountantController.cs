using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PhamMemThueXe.Data;
using PhamMemThueXe.Models;
using System.Security.Claims;

namespace PhamMemThueXe.Controllers
{
    [Authorize(Roles = "Ketoan")]
    public class AccountantController : Controller
    {
        private readonly ApplicationDbContext _context;

        public AccountantController(ApplicationDbContext context)
        {
            _context = context;
        }

        public IActionResult Index()
        {
            return View();
        }

        // ========== QUẢN LÝ PHIẾU ĐẶT XE ==========
        public async Task<IActionResult> PhieuDatXes()
        {
            var phieuDatXes = await _context.PhieuDatXes
                .Include(pdx => pdx.KhachHang)
                .Include(pdx => pdx.LoaiXe)
                .OrderByDescending(pdx => pdx.NgayDat)
                .ToListAsync();
            return View(phieuDatXes);
        }

        public async Task<IActionResult> CreatePhieuDatXe()
        {
            ViewBag.KhachHangs = await _context.KhachHangs.ToListAsync();
            ViewBag.LoaiXes = await _context.LoaiXes.ToListAsync();
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> CreatePhieuDatXe(int MaKH, int MaLoaiXe, DateTime NgayDuKienNhan)
        {
            var khachHang = await _context.KhachHangs.FindAsync(MaKH);
            var loaiXe = await _context.LoaiXes.FindAsync(MaLoaiXe);

            if (khachHang != null && loaiXe != null)
            {
                var phieuDat = new PhieuDatXe
                {
                    MaKH = MaKH,
                    MaLoaiXe = MaLoaiXe,
                    HoTenKH = khachHang.HoTen,
                    TenLoaiXeDat = loaiXe.TenLoaiXe,
                    NgayDat = DateTime.Now,
                    NgayDuKienNhan = NgayDuKienNhan,
                    TrangThai = "Đang chờ"
                };

                _context.PhieuDatXes.Add(phieuDat);
                await _context.SaveChangesAsync();
                TempData["SuccessMessage"] = "Tạo phiếu đặt xe thành công!";
                return RedirectToAction("PhieuDatXes");
            }
            return RedirectToAction("PhieuDatXes");
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> UpdatePhieuDatXeStatus(int id, string trangThai)
        {
            var phieuDat = await _context.PhieuDatXes.FindAsync(id);
            if (phieuDat != null)
            {
                phieuDat.TrangThai = trangThai;
                _context.Update(phieuDat);
                await _context.SaveChangesAsync();
                TempData["SuccessMessage"] = "Cập nhật trạng thái thành công!";
            }
            return RedirectToAction("PhieuDatXes");
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeletePhieuDatXe(int id)
        {
            var phieuDat = await _context.PhieuDatXes.FindAsync(id);
            if (phieuDat != null)
            {
                _context.PhieuDatXes.Remove(phieuDat);
                await _context.SaveChangesAsync();
                TempData["SuccessMessage"] = "Xóa phiếu đặt xe thành công!";
            }
            return RedirectToAction("PhieuDatXes");
        }

        // ========== QUẢN LÝ HỢP ĐỒNG ==========
        public async Task<IActionResult> HopDongs()
        {
            var hopDongs = await _context.HopDongs
                .Include(hd => hd.KhachHang)
                .Include(hd => hd.NhanVien)
                .Include(hd => hd.ChiTietHopDongs)
                    .ThenInclude(cthd => cthd.Xe)
                .OrderByDescending(hd => hd.NgayLap)
                .ToListAsync();
            return View(hopDongs);
        }

        public async Task<IActionResult> CreateHopDong()
        {
            ViewBag.KhachHangs = await _context.KhachHangs.ToListAsync();
            ViewBag.Xes = await _context.Xes.Where(x => x.TrangThai == "Sẵn sàng").Include(x => x.LoaiXe).ToListAsync();
            ViewBag.PhieuDatXes = await _context.PhieuDatXes
                .Where(pdx => pdx.TrangThai == "Đã xác nhận")
                .Include(pdx => pdx.KhachHang)
                .Include(pdx => pdx.LoaiXe)
                .ToListAsync();
            // Lấy bảng giá hiện tại cho mỗi loại xe, cần Include trước khi GroupBy và Select
            var bangGias = await _context.BangGias
                .Include(bg => bg.LoaiXe)
                .Where(bg => bg.NgayApDung <= DateTime.Today)
                .GroupBy(bg => bg.MaLoaiXe)
                .Select(g => g.OrderByDescending(bg => bg.NgayApDung).First())
                .ToListAsync();
            ViewBag.BangGias = bangGias;
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> CreateHopDong(int MaKH, int? MaDatXe, DateTime NgayNhanXe, string DiaDiemNhan, DateTime NgayTraXe, string DiaDiemTra, decimal TongTien, decimal TienDatCoc, string ChiTietHopDongs)
        {
            var nhanVienId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
            var khachHang = await _context.KhachHangs.FindAsync(MaKH);
            var nhanVien = await _context.TaiKhoans.FindAsync(nhanVienId);

            if (khachHang == null || nhanVien == null)
            {
                TempData["ErrorMessage"] = "Dữ liệu không hợp lệ";
                return RedirectToAction("CreateHopDong");
            }

            List<ChiTietHopDongViewModel> chiTietList;
            try
            {
                chiTietList = System.Text.Json.JsonSerializer.Deserialize<List<ChiTietHopDongViewModel>>(ChiTietHopDongs ?? "[]") ?? new();
            }
            catch
            {
                chiTietList = new();
            }

            if (chiTietList.Count == 0)
            {
                TempData["ErrorMessage"] = "Vui lòng chọn ít nhất một xe";
                return RedirectToAction("CreateHopDong");
            }

            using var transaction = await _context.Database.BeginTransactionAsync();

            try
            {
                var hopDong = new HopDong
                {
                    MaKH = MaKH,
                    MaNhanVien = nhanVienId,
                    MaDatXe = MaDatXe,
                    HoTenKH = khachHang.HoTen,
                    SoDienThoaiKH = khachHang.SoDienThoai,
                    HoTenNVLap = nhanVien.HoTen,
                    NgayLap = DateTime.Now,
                    NgayNhanXe = NgayNhanXe,
                    DiaDiemNhan = DiaDiemNhan,
                    NgayTraXe = NgayTraXe,
                    DiaDiemTra = DiaDiemTra,
                    TongTien = TongTien,
                    TienDatCoc = TienDatCoc,
                    TrangThai = "Đang thuê"
                };

                _context.HopDongs.Add(hopDong);
                await _context.SaveChangesAsync();

                foreach (var item in chiTietList)
                {
                    var xe = await _context.Xes.FindAsync(item.MaXe);
                    if (xe != null)
                    {
                        var chiTiet = new ChiTietHopDong
                        {
                            MaHopDong = hopDong.MaHopDong,
                            MaXe = item.MaXe,
                            BienSoXe = xe.BienSoXe,
                            TenXe = xe.TenXe,
                            DonGiaTaiThoiDiemThue = item.DonGiaTaiThoiDiemThue,
                            GhiChu = item.GhiChu
                        };
                        _context.ChiTietHopDongs.Add(chiTiet);

                        xe.TrangThai = "Đang cho thuê";
                        _context.Update(xe);
                    }
                }

                if (MaDatXe.HasValue)
                {
                    var phieuDat = await _context.PhieuDatXes.FindAsync(MaDatXe.Value);
                    if (phieuDat != null)
                    {
                        phieuDat.TrangThai = "Đã thành hợp đồng";
                        _context.Update(phieuDat);
                    }
                }

                await _context.SaveChangesAsync();
                await transaction.CommitAsync();

                TempData["SuccessMessage"] = "Tạo hợp đồng thành công!";
                return RedirectToAction("HopDongs");
            }
            catch (Exception ex)
            {
                await transaction.RollbackAsync();
                TempData["ErrorMessage"] = "Có lỗi xảy ra khi tạo hợp đồng: " + ex.Message;
                return RedirectToAction("CreateHopDong");
            }
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> UpdateHopDongStatus(int id, string trangThai)
        {
            var hopDong = await _context.HopDongs
                .Include(hd => hd.ChiTietHopDongs)
                .FirstOrDefaultAsync(hd => hd.MaHopDong == id);

            if (hopDong != null)
            {
                hopDong.TrangThai = trangThai;

                if (trangThai == "Đã hoàn thành")
                {
                    foreach (var chiTiet in hopDong.ChiTietHopDongs)
                    {
                        var xe = await _context.Xes.FindAsync(chiTiet.MaXe);
                        if (xe != null)
                        {
                            xe.TrangThai = "Sẵn sàng";
                            _context.Update(xe);
                        }
                    }
                }

                _context.Update(hopDong);
                await _context.SaveChangesAsync();
                TempData["SuccessMessage"] = "Cập nhật trạng thái hợp đồng thành công!";
            }

            return RedirectToAction("HopDongs");
        }

        // ========== QUẢN LÝ HÓA ĐƠN ==========
        public async Task<IActionResult> HoaDons()
        {
            var hoaDons = await _context.HoaDons
                .Include(hd => hd.HopDong)
                    .ThenInclude(hd => hd.KhachHang)
                .Include(hd => hd.NhanVien)
                .OrderByDescending(hd => hd.NgayLap)
                .ToListAsync();
            return View(hoaDons);
        }

        public async Task<IActionResult> CreateHoaDon(int? hopDongId)
        {
            ViewBag.HopDongs = await _context.HopDongs
                .Include(hd => hd.KhachHang)
                .Where(hd => hopDongId == null || hd.MaHopDong == hopDongId)
                .ToListAsync();
            if (hopDongId.HasValue)
            {
                var hopDong = await _context.HopDongs.FindAsync(hopDongId.Value);
                var totalPaid = await _context.HoaDons
                    .Where(hd => hd.MaHopDong == hopDongId.Value)
                    .SumAsync(hd => (decimal?)hd.SoTienThanhToan) ?? 0;
                ViewBag.HopDong = hopDong;
                ViewBag.TotalPaid = totalPaid;
                ViewBag.Remaining = hopDong.TongTien - totalPaid;
            }
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> CreateHoaDon(CreateHoaDonViewModel model)
        {
            var nhanVienId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
            var nhanVien = await _context.TaiKhoans.FindAsync(nhanVienId);

            if (nhanVien == null)
            {
                TempData["ErrorMessage"] = "Không tìm thấy thông tin nhân viên";
                return RedirectToAction("CreateHoaDon");
            }

            var hoaDon = new HoaDon
            {
                MaHopDong = model.MaHopDong,
                MaNhanVien = nhanVienId,
                HoTenNVLap = nhanVien.HoTen,
                NgayLap = DateTime.Now,
                SoTienThanhToan = model.SoTienThanhToan,
                LoaiThanhToan = model.LoaiThanhToan
            };

            _context.HoaDons.Add(hoaDon);
            await _context.SaveChangesAsync();

            TempData["SuccessMessage"] = "Tạo hóa đơn thành công!";
            return RedirectToAction("HoaDons");
        }

        // ========== QUẢN LÝ KHÁCH HÀNG ==========
        public async Task<IActionResult> KhachHangs()
        {
            var khachHangs = await _context.KhachHangs.ToListAsync();
            return View(khachHangs);
        }

        public IActionResult CreateKhachHang()
        {
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> CreateKhachHang(KhachHang khachHang)
        {
            if (ModelState.IsValid)
            {
                _context.KhachHangs.Add(khachHang);
                await _context.SaveChangesAsync();
                TempData["SuccessMessage"] = "Thêm khách hàng thành công!";
                return RedirectToAction("KhachHangs");
            }
            return View(khachHang);
        }
    }

    // View Models
    public class CreateHopDongViewModel
    {
        public int MaKH { get; set; }
        public int? MaDatXe { get; set; }
        public DateTime NgayNhanXe { get; set; }
        public string DiaDiemNhan { get; set; } = string.Empty;
        public DateTime NgayTraXe { get; set; }
        public string DiaDiemTra { get; set; } = string.Empty;
        public decimal TongTien { get; set; }
        public decimal TienDatCoc { get; set; }
        public List<ChiTietHopDongViewModel> ChiTietHopDongs { get; set; } = new();
    }

    public class ChiTietHopDongViewModel
    {
        public int MaXe { get; set; }
        public decimal DonGiaTaiThoiDiemThue { get; set; }
        public string? GhiChu { get; set; }
    }

    public class CreateHoaDonViewModel
    {
        public int MaHopDong { get; set; }
        public decimal SoTienThanhToan { get; set; }
        public string LoaiThanhToan { get; set; } = string.Empty;
    }
}
