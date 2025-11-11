using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PhamMemThueXe.Data;
using PhamMemThueXe.Models;

namespace PhamMemThueXe.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    [Authorize(Roles = "Admin,Ketoan")]
    public class KhachHangApiController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public KhachHangApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/KhachHangApi
        [HttpGet]
        public async Task<IActionResult> GetKhachHangs()
        {
            var khachHangs = await _context.KhachHangs
                .Select(kh => new {
                    kh.MaKH,
                    kh.HoTen,
                    kh.SoCMND,
                    kh.SoHoKhau,
                    kh.SoDienThoai,
                    kh.DiaChiCoQuan,
                    SoHopDong = _context.HopDongs.Count(hd => hd.MaKH == kh.MaKH)
                })
                .ToListAsync();

            return Ok(new { success = true, data = khachHangs });
        }

        // GET: api/KhachHangApi/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetKhachHang(int id)
        {
            var khachHang = await _context.KhachHangs
                .Where(kh => kh.MaKH == id)
                .Select(kh => new {
                    kh.MaKH,
                    kh.HoTen,
                    kh.SoCMND,
                    kh.SoHoKhau,
                    kh.SoDienThoai,
                    kh.DiaChiCoQuan,
                    SoHopDong = _context.HopDongs.Count(hd => hd.MaKH == kh.MaKH)
                })
                .FirstOrDefaultAsync();

            if (khachHang == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy khách hàng" });
            }

            return Ok(new { success = true, data = khachHang });
        }

        // POST: api/KhachHangApi
        [HttpPost]
        public async Task<IActionResult> CreateKhachHang([FromBody] KhachHang khachHang)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            // Kiểm tra CMND đã tồn tại chưa
            var existingCustomer = await _context.KhachHangs
                .FirstOrDefaultAsync(k => k.SoCMND == khachHang.SoCMND);

            if (existingCustomer != null)
            {
                return BadRequest(new { success = false, message = "Số CMND đã tồn tại trong hệ thống" });
            }

            try
            {
                _context.KhachHangs.Add(khachHang);
                await _context.SaveChangesAsync();

                return Ok(new { 
                    success = true, 
                    message = "Thêm khách hàng thành công!",
                    data = new {
                        khachHang.MaKH,
                        khachHang.HoTen,
                        khachHang.SoCMND,
                        khachHang.SoHoKhau,
                        khachHang.SoDienThoai,
                        khachHang.DiaChiCoQuan
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi thêm khách hàng. Vui lòng thử lại." });
            }
        }

        // PUT: api/KhachHangApi/5
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateKhachHang(int id, [FromBody] KhachHang khachHang)
        {
            if (id != khachHang.MaKH)
            {
                return BadRequest(new { success = false, message = "ID không khớp" });
            }

            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            // Kiểm tra CMND đã tồn tại chưa (trừ khách hàng hiện tại)
            var existingCustomer = await _context.KhachHangs
                .FirstOrDefaultAsync(k => k.SoCMND == khachHang.SoCMND && k.MaKH != id);

            if (existingCustomer != null)
            {
                return BadRequest(new { success = false, message = "Số CMND đã tồn tại trong hệ thống" });
            }

            try
            {
                _context.Update(khachHang);
                await _context.SaveChangesAsync();

                return Ok(new { 
                    success = true, 
                    message = "Cập nhật khách hàng thành công!",
                    data = new {
                        khachHang.MaKH,
                        khachHang.HoTen,
                        khachHang.SoCMND,
                        khachHang.SoHoKhau,
                        khachHang.SoDienThoai,
                        khachHang.DiaChiCoQuan
                    }
                });
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!KhachHangExists(khachHang.MaKH))
                {
                    return NotFound(new { success = false, message = "Khách hàng không tồn tại" });
                }
                else
                {
                    throw;
                }
            }
        }

        // DELETE: api/KhachHangApi/5
        [HttpDelete("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> DeleteKhachHang(int id)
        {
            var khachHang = await _context.KhachHangs.FindAsync(id);
            if (khachHang == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy khách hàng" });
            }

            // Kiểm tra xem có hợp đồng nào của khách hàng này không
            var hasContracts = await _context.HopDongs.AnyAsync(hd => hd.MaKH == id);
            if (hasContracts)
            {
                return BadRequest(new { success = false, message = "Không thể xóa khách hàng này vì còn có hợp đồng liên quan" });
            }

            try
            {
                _context.KhachHangs.Remove(khachHang);
                await _context.SaveChangesAsync();

                return Ok(new { success = true, message = "Xóa khách hàng thành công!" });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi xóa khách hàng. Vui lòng thử lại." });
            }
        }

        // GET: api/KhachHangApi/search
        [HttpGet("search")]
        public async Task<IActionResult> SearchKhachHangs([FromQuery] string? keyword)
        {
            if (string.IsNullOrEmpty(keyword))
            {
                return await GetKhachHangs();
            }

            var khachHangs = await _context.KhachHangs
                .Where(kh => kh.HoTen.Contains(keyword) || 
                            kh.SoCMND.Contains(keyword) || 
                            kh.SoDienThoai.Contains(keyword))
                .Select(kh => new {
                    kh.MaKH,
                    kh.HoTen,
                    kh.SoCMND,
                    kh.SoHoKhau,
                    kh.SoDienThoai,
                    kh.DiaChiCoQuan,
                    SoHopDong = _context.HopDongs.Count(hd => hd.MaKH == kh.MaKH)
                })
                .ToListAsync();

            return Ok(new { success = true, data = khachHangs });
        }

        // GET: api/KhachHangApi/{id}/contracts
        [HttpGet("{id}/contracts")]
        public async Task<IActionResult> GetKhachHangContracts(int id)
        {
            var contracts = await _context.HopDongs
                .Include(hd => hd.NhanVien)
                .Where(hd => hd.MaKH == id)
                .Select(hd => new {
                    hd.MaHopDong,
                    hd.NgayLap,
                    hd.NgayNhanXe,
                    hd.NgayTraXe,
                    hd.TongTien,
                    hd.TienDatCoc,
                    hd.TrangThai,
                    NhanVien = new {
                        hd.NhanVien.HoTen
                    }
                })
                .OrderByDescending(hd => hd.NgayLap)
                .ToListAsync();

            return Ok(new { success = true, data = contracts });
        }

        // GET: api/KhachHangApi/by-cmnd/{cmnd}
        [HttpGet("by-cmnd/{cmnd}")]
        public async Task<IActionResult> GetKhachHangByCMND(string cmnd)
        {
            var khachHang = await _context.KhachHangs
                .Where(kh => kh.SoCMND == cmnd)
                .Select(kh => new {
                    kh.MaKH,
                    kh.HoTen,
                    kh.SoCMND,
                    kh.SoHoKhau,
                    kh.SoDienThoai,
                    kh.DiaChiCoQuan,
                    SoHopDong = _context.HopDongs.Count(hd => hd.MaKH == kh.MaKH)
                })
                .FirstOrDefaultAsync();

            if (khachHang == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy khách hàng với CMND này" });
            }

            return Ok(new { success = true, data = khachHang });
        }

        private bool KhachHangExists(int id)
        {
            return _context.KhachHangs.Any(e => e.MaKH == id);
        }
    }
}
