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
    [ApiController]
    [Route("api/[controller]")]
    public class AuthApiController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public AuthApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginRequestModel request)
        {
            // Xử lý cả hai format: username/password (mobile) và TenDangNhap/MatKhau (web)
            string tenDangNhap = !string.IsNullOrEmpty(request.TenDangNhap) ? request.TenDangNhap : request.Username ?? string.Empty;
            string matKhau = !string.IsNullOrEmpty(request.MatKhau) ? request.MatKhau : request.Password ?? string.Empty;
            bool rememberMe = request.RememberMe;

            if (string.IsNullOrEmpty(tenDangNhap) || string.IsNullOrEmpty(matKhau))
            {
                return BadRequest(new { success = false, message = "Tên đăng nhập và mật khẩu là bắt buộc" });
            }

            var user = await _context.TaiKhoans
                .FirstOrDefaultAsync(u => u.TenDangNhap == tenDangNhap);

            if (user != null && BCrypt.Net.BCrypt.Verify(matKhau, user.MatKhauHash))
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
                    IsPersistent = rememberMe,
                    ExpiresUtc = rememberMe ? DateTimeOffset.UtcNow.AddDays(7) : DateTimeOffset.UtcNow.AddHours(1)
                };

                await HttpContext.SignInAsync(CookieAuthenticationDefaults.AuthenticationScheme,
                    new ClaimsPrincipal(claimsIdentity), authProperties);

                // Lấy maKH cho Customer
                int? maKH = null;
                if (user.Quyen == "Customer")
                {
                    var khachHang = await _context.KhachHangs
                        .FirstOrDefaultAsync(kh => kh.HoTen == user.HoTen);
                    if (khachHang != null)
                    {
                        maKH = khachHang.MaKH;
                    }
                }

                // Tạo token/session ID cho mobile app
                HttpContext.Session.SetString("UserId", user.MaNhanVien.ToString());
                var sessionToken = HttpContext.Session.Id;

                // Trả về response format cho mobile app
                return Ok(new { 
                    success = true, 
                    message = "Đăng nhập thành công",
                    token = sessionToken, // Token cho mobile app
                    username = user.TenDangNhap,
                    role = user.Quyen,
                    maKH = maKH ?? 0,
                    // Giữ format cũ cho web
                    user = new {
                        id = user.MaNhanVien,
                        username = user.TenDangNhap,
                        fullName = user.HoTen,
                        role = user.Quyen
                    }
                });
            }

            return BadRequest(new { success = false, message = "Tên đăng nhập hoặc mật khẩu không đúng" });
        }

        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody] RegisterViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            // Kiểm tra tên đăng nhập đã tồn tại chưa
            var existingUser = await _context.TaiKhoans
                .FirstOrDefaultAsync(u => u.TenDangNhap == model.TenDangNhap);

            if (existingUser != null)
            {
                return BadRequest(new { success = false, message = "Tên đăng nhập đã tồn tại" });
            }

            // Kiểm tra CMND đã tồn tại chưa
            var existingCustomer = await _context.KhachHangs
                .FirstOrDefaultAsync(k => k.SoCMND == model.SoCMND);

            if (existingCustomer != null)
            {
                return BadRequest(new { success = false, message = "Số CMND đã tồn tại trong hệ thống" });
            }

            try
            {
                // Tạo khách hàng mới
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

                // Tạo tài khoản cho khách hàng
                var taiKhoan = new TaiKhoan
                {
                    TenDangNhap = model.TenDangNhap,
                    MatKhauHash = BCrypt.Net.BCrypt.HashPassword(model.MatKhau),
                    HoTen = model.HoTen,
                    Quyen = "Customer"
                };

                _context.TaiKhoans.Add(taiKhoan);
                await _context.SaveChangesAsync();

                return Ok(new { 
                    success = true, 
                    message = "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.",
                    customer = new {
                        id = khachHang.MaKH,
                        fullName = khachHang.HoTen,
                        phone = khachHang.SoDienThoai
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi đăng ký. Vui lòng thử lại." });
            }
        }

        [HttpPost("logout")]
        [Authorize]
        public async Task<IActionResult> Logout()
        {
            await HttpContext.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
            return Ok(new { success = true, message = "Đăng xuất thành công" });
        }

        [HttpGet("me")]
        [Authorize]
        public async Task<IActionResult> GetCurrentUser()
        {
            var tenDangNhap = User.FindFirst("TenDangNhap")?.Value;
            var user = await _context.TaiKhoans.FirstOrDefaultAsync(u => u.TenDangNhap == tenDangNhap);
            if (user == null)
                return Unauthorized(new { success = false, message = "Chưa đăng nhập hoặc session hết hạn" });

            // Với Customer, tìm thêm thông tin KhachHang để lấy maKH
            int? maKH = null;
            if (user.Quyen == "Customer")
            {
                var khachHang = await _context.KhachHangs
                    .FirstOrDefaultAsync(kh => kh.HoTen == user.HoTen);
                if (khachHang != null)
                {
                    maKH = khachHang.MaKH;
                }
            }

            var result = new {
                id = user.MaNhanVien,
                username = user.TenDangNhap,
                fullName = user.HoTen,
                role = user.Quyen,
                maKH = maKH
            };

            return Ok(new {
                success = true,
                data = result
            });
        }

        [HttpGet("check-auth")]
        public IActionResult CheckAuth()
        {
            var isAuthenticated = User.Identity?.IsAuthenticated ?? false;
            
            if (isAuthenticated)
            {
                var userId = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
                var userName = User.FindFirst(ClaimTypes.Name)?.Value;
                var userRole = User.FindFirst(ClaimTypes.Role)?.Value;
                var tenDangNhap = User.FindFirst("TenDangNhap")?.Value;

                return Ok(new {
                    success = true,
                    isAuthenticated = true,
                    user = new {
                        id = userId,
                        username = tenDangNhap,
                        fullName = userName,
                        role = userRole
                    }
                });
            }

            return Ok(new {
                success = true,
                isAuthenticated = false,
                user = (object?)null
            });
        }
    }
}
