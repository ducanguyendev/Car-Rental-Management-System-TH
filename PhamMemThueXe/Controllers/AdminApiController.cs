using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PhamMemThueXe.Data;
using PhamMemThueXe.Models;

namespace PhamMemThueXe.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    [Authorize(Roles = "Admin")]
    public class AdminApiController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public AdminApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/AdminApi/accountants
        [HttpGet("accountants")]
        public async Task<IActionResult> GetAccountants()
        {
            var accountants = await _context.TaiKhoans
                .Where(t => t.Quyen == "Ketoan")
                .Select(t => new {
                    t.MaNhanVien,
                    t.TenDangNhap,
                    t.HoTen,
                    t.Quyen
                })
                .ToListAsync();

            return Ok(new { success = true, data = accountants });
        }

        // GET: api/AdminApi/accountants/5
        [HttpGet("accountants/{id}")]
        public async Task<IActionResult> GetAccountant(int id)
        {
            var accountant = await _context.TaiKhoans
                .Where(t => t.MaNhanVien == id && t.Quyen == "Ketoan")
                .Select(t => new {
                    t.MaNhanVien,
                    t.TenDangNhap,
                    t.HoTen,
                    t.Quyen
                })
                .FirstOrDefaultAsync();

            if (accountant == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy tài khoản kế toán" });
            }

            return Ok(new { success = true, data = accountant });
        }

        // POST: api/AdminApi/accountants
        [HttpPost("accountants")]
        public async Task<IActionResult> CreateAccountant([FromBody] CreateAccountantViewModel model)
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

            try
            {
                var taiKhoan = new TaiKhoan
                {
                    TenDangNhap = model.TenDangNhap,
                    MatKhauHash = BCrypt.Net.BCrypt.HashPassword(model.MatKhau),
                    HoTen = model.HoTen,
                    Quyen = "Ketoan"
                };

                _context.TaiKhoans.Add(taiKhoan);
                await _context.SaveChangesAsync();

                return Ok(new { 
                    success = true, 
                    message = "Tạo tài khoản kế toán thành công!",
                    data = new {
                        taiKhoan.MaNhanVien,
                        taiKhoan.TenDangNhap,
                        taiKhoan.HoTen,
                        taiKhoan.Quyen
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi tạo tài khoản. Vui lòng thử lại." });
            }
        }

        // PUT: api/AdminApi/accountants/5
        [HttpPut("accountants/{id}")]
        public async Task<IActionResult> UpdateAccountant(int id, [FromBody] CreateAccountantViewModel model)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            var taiKhoan = await _context.TaiKhoans.FindAsync(id);
            if (taiKhoan == null || taiKhoan.Quyen != "Ketoan")
            {
                return NotFound(new { success = false, message = "Không tìm thấy tài khoản kế toán" });
            }

            // Kiểm tra tên đăng nhập đã tồn tại chưa (trừ tài khoản hiện tại)
            var existingUser = await _context.TaiKhoans
                .FirstOrDefaultAsync(u => u.TenDangNhap == model.TenDangNhap && u.MaNhanVien != id);

            if (existingUser != null)
            {
                return BadRequest(new { success = false, message = "Tên đăng nhập đã tồn tại" });
            }

            try
            {
                taiKhoan.TenDangNhap = model.TenDangNhap;
                taiKhoan.HoTen = model.HoTen;

                // Chỉ cập nhật mật khẩu nếu có nhập mới
                if (!string.IsNullOrEmpty(model.MatKhau))
                {
                    taiKhoan.MatKhauHash = BCrypt.Net.BCrypt.HashPassword(model.MatKhau);
                }

                _context.Update(taiKhoan);
                await _context.SaveChangesAsync();

                return Ok(new { 
                    success = true, 
                    message = "Cập nhật tài khoản kế toán thành công!",
                    data = new {
                        taiKhoan.MaNhanVien,
                        taiKhoan.TenDangNhap,
                        taiKhoan.HoTen,
                        taiKhoan.Quyen
                    }
                });
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!TaiKhoanExists(taiKhoan.MaNhanVien))
                {
                    return NotFound(new { success = false, message = "Tài khoản không tồn tại" });
                }
                else
                {
                    throw;
                }
            }
        }

        // DELETE: api/AdminApi/accountants/5
        [HttpDelete("accountants/{id}")]
        public async Task<IActionResult> DeleteAccountant(int id)
        {
            var taiKhoan = await _context.TaiKhoans.FindAsync(id);
            if (taiKhoan == null || taiKhoan.Quyen != "Ketoan")
            {
                return NotFound(new { success = false, message = "Không tìm thấy tài khoản kế toán" });
            }

            try
            {
                _context.TaiKhoans.Remove(taiKhoan);
                await _context.SaveChangesAsync();

                return Ok(new { success = true, message = "Xóa tài khoản kế toán thành công!" });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi xóa tài khoản. Vui lòng thử lại." });
            }
        }

        // GET: api/AdminApi/statistics
        [HttpGet("statistics")]
        public async Task<IActionResult> GetStatistics()
        {
            try
            {
                var totalCustomers = await _context.KhachHangs.CountAsync();
                var totalAccountants = await _context.TaiKhoans.CountAsync(t => t.Quyen == "Ketoan");
                var totalCars = await _context.Xes.CountAsync();
                var totalContracts = await _context.HopDongs.CountAsync();
                var totalInvoices = await _context.HoaDons.CountAsync();

                return Ok(new { 
                    success = true, 
                    data = new {
                        totalCustomers,
                        totalAccountants,
                        totalCars,
                        totalContracts,
                        totalInvoices
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi lấy thống kê." });
            }
        }

        private bool TaiKhoanExists(int id)
        {
            return _context.TaiKhoans.Any(e => e.MaNhanVien == id);
        }
    }
}
