using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PhamMemThueXe.Data;
using PhamMemThueXe.Models;
using System.Security.Claims;

namespace PhamMemThueXe.Controllers
{
    public class AuthController : Controller
    {
        private readonly ApplicationDbContext _context;

        public AuthController(ApplicationDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public IActionResult Login()
        {
            if (User.Identity?.IsAuthenticated == true)
            {
                return RedirectToAction("Index", "Home");
            }
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Login(LoginViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return View(model);
            }

            var user = await _context.TaiKhoans
                .FirstOrDefaultAsync(u => u.TenDangNhap == model.TenDangNhap);

            if (user != null && BCrypt.Net.BCrypt.Verify(model.MatKhau, user.MatKhauHash))
            {
                var claims = new List<Claim>
                {
                    new Claim(ClaimTypes.NameIdentifier, user.MaNhanVien.ToString()),
                    new Claim(ClaimTypes.Name, user.HoTen),
                    new Claim(ClaimTypes.Role, user.Quyen),
                    new Claim("TenDangNhap", user.TenDangNhap)
                };

                var claimsIdentity = new ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme);
                var authProperties = new AuthenticationProperties
                {
                    IsPersistent = model.RememberMe,
                    ExpiresUtc = model.RememberMe ? DateTimeOffset.UtcNow.AddDays(7) : DateTimeOffset.UtcNow.AddHours(1)
                };

                await HttpContext.SignInAsync(CookieAuthenticationDefaults.AuthenticationScheme,
                    new ClaimsPrincipal(claimsIdentity), authProperties);

                return RedirectToAction("Index", "Home");
            }

            ModelState.AddModelError(string.Empty, "Tên đăng nhập hoặc mật khẩu không đúng");
            return View(model);
        }

        [HttpGet]
        public IActionResult Register()
        {
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Register(RegisterViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return View(model);
            }

            var existingUser = await _context.TaiKhoans
                .FirstOrDefaultAsync(u => u.TenDangNhap == model.TenDangNhap);

            if (existingUser != null)
            {
                ModelState.AddModelError(nameof(model.TenDangNhap), "Tên đăng nhập đã tồn tại");
                return View(model);
            }

            var existingCustomer = await _context.KhachHangs
                .FirstOrDefaultAsync(k => k.SoCMND == model.SoCMND);

            if (existingCustomer != null)
            {
                ModelState.AddModelError(nameof(model.SoCMND), "Số CMND đã tồn tại trong hệ thống");
                return View(model);
            }

            try
            {
                var khachHang = new KhachHang
                {
                    HoTen = model.HoTen,
                    SoCMND = model.SoCMND,
                    SoHoKhau = model.SoHoKhau,
                    SoDienThoai = model.SoDienThoai,
                    DiaChiCoQuan = model.DiaChiCoQuan
                };

                _context.KhachHangs.Add(khachHang);
                await _context.SaveChangesAsync();

                var taiKhoan = new TaiKhoan
                {
                    TenDangNhap = model.TenDangNhap,
                    MatKhauHash = BCrypt.Net.BCrypt.HashPassword(model.MatKhau),
                    HoTen = model.HoTen,
                    Quyen = "Customer"
                };

                _context.TaiKhoans.Add(taiKhoan);
                await _context.SaveChangesAsync();

                TempData["SuccessMessage"] = "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.";
                return RedirectToAction("Login");
            }
            catch (Exception ex)
            {
                ModelState.AddModelError(string.Empty, "Có lỗi xảy ra khi đăng ký. Vui lòng thử lại.");
                return View(model);
            }
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Logout()
        {
            await HttpContext.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
            return RedirectToAction("Index", "Home");
        }
    }
}

