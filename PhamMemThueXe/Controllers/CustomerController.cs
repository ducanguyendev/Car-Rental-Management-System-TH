using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PhamMemThueXe.Data;
using System.Security.Claims;

namespace PhamMemThueXe.Controllers
{
    [Authorize(Roles = "Customer")]
    public class CustomerController : Controller
    {
        private readonly ApplicationDbContext _context;

        public CustomerController(ApplicationDbContext context)
        {
            _context = context;
        }

        public IActionResult Index()
        {
            return View();
        }

        // Trang đặt xe với chi tiết xe
        public async Task<IActionResult> BookCar(int? maXe)
        {
            if (maXe.HasValue)
            {
                ViewBag.MaXe = maXe.Value;
            }
            return View();
        }

        public async Task<IActionResult> PhieuDatXes()
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
            var user = await _context.TaiKhoans.FindAsync(userId);
            
            // Tìm khách hàng theo họ tên (vì khi đăng ký, cả 2 đều có cùng HoTen)
            var khachHang = await _context.KhachHangs
                .FirstOrDefaultAsync(kh => kh.HoTen == user.HoTen);
            
            if (khachHang == null) return View(new List<PhamMemThueXe.Models.PhieuDatXe>());

            var phieuDatXes = await _context.PhieuDatXes
                .Include(pdx => pdx.LoaiXe)
                .Where(pdx => pdx.MaKH == khachHang.MaKH)
                .OrderByDescending(pdx => pdx.NgayDat)
                .ToListAsync();

            return View(phieuDatXes);
        }

        public async Task<IActionResult> CreatePhieuDatXe()
        {
            ViewBag.LoaiXes = await _context.LoaiXes.ToListAsync();
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> CreatePhieuDatXe(int MaLoaiXe, DateTime NgayDuKienNhan)
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
            var user = await _context.TaiKhoans.FindAsync(userId);
            
            // Tìm khách hàng theo họ tên (vì khi đăng ký, cả 2 đều có cùng HoTen)
            var khachHang = await _context.KhachHangs
                .FirstOrDefaultAsync(kh => kh.HoTen == user.HoTen);
            
            if (khachHang == null)
            {
                TempData["ErrorMessage"] = "Không tìm thấy thông tin khách hàng";
                return RedirectToAction("PhieuDatXes");
            }

            var loaiXe = await _context.LoaiXes.FindAsync(MaLoaiXe);
            if (loaiXe == null)
            {
                TempData["ErrorMessage"] = "Loại xe không tồn tại";
                return RedirectToAction("PhieuDatXes");
            }

            var phieuDat = new PhamMemThueXe.Models.PhieuDatXe
            {
                MaKH = khachHang.MaKH,
                MaLoaiXe = MaLoaiXe,
                HoTenKH = khachHang.HoTen,
                TenLoaiXeDat = loaiXe.TenLoaiXe,
                NgayDat = DateTime.Now,
                NgayDuKienNhan = NgayDuKienNhan,
                TrangThai = "Đang chờ"
            };

            _context.PhieuDatXes.Add(phieuDat);
            await _context.SaveChangesAsync();
            TempData["SuccessMessage"] = "Đặt xe thành công!";
            return RedirectToAction("PhieuDatXes");
        }

        public async Task<IActionResult> HopDongs()
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
            var user = await _context.TaiKhoans.FindAsync(userId);
            
            // Tìm khách hàng theo họ tên (vì khi đăng ký, cả 2 đều có cùng HoTen)
            var khachHang = await _context.KhachHangs
                .FirstOrDefaultAsync(kh => kh.HoTen == user.HoTen);
            
            if (khachHang == null) return View(new List<PhamMemThueXe.Models.HopDong>());

            var hopDongs = await _context.HopDongs
                .Include(hd => hd.ChiTietHopDongs)
                    .ThenInclude(cthd => cthd.Xe)
                .Where(hd => hd.MaKH == khachHang.MaKH)
                .OrderByDescending(hd => hd.NgayLap)
                .ToListAsync();

            return View(hopDongs);
        }

        public async Task<IActionResult> HoaDons()
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)?.Value ?? "0");
            var user = await _context.TaiKhoans.FindAsync(userId);
            
            // Tìm khách hàng theo họ tên (vì khi đăng ký, cả 2 đều có cùng HoTen)
            var khachHang = await _context.KhachHangs
                .FirstOrDefaultAsync(kh => kh.HoTen == user.HoTen);
            
            if (khachHang == null) return View(new List<PhamMemThueXe.Models.HoaDon>());

            var hoaDons = await _context.HoaDons
                .Include(hd => hd.HopDong)
                .Where(hd => hd.HopDong.MaKH == khachHang.MaKH)
                .OrderByDescending(hd => hd.NgayLap)
                .ToListAsync();

            return View(hoaDons);
        }
    }
}
