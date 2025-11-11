using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PhamMemThueXe.Data;
using PhamMemThueXe.Models;

namespace PhamMemThueXe.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class LoaiXeApiController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public LoaiXeApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/LoaiXeApi
        [HttpGet]
        [Authorize] // Cho mọi user đã đăng nhập
        public async Task<IActionResult> GetLoaiXes()
        {
            var loaiXes = await _context.LoaiXes
                .Select(lx => new {
                    lx.MaLoaiXe,
                    lx.TenLoaiXe,
                    lx.MoTa,
                    SoLuongXe = _context.Xes.Count(x => x.MaLoaiXe == lx.MaLoaiXe)
                })
                .ToListAsync();

            return Ok(new { success = true, data = loaiXes });
        }

        // GET: api/LoaiXeApi/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetLoaiXe(int id)
        {
            var loaiXe = await _context.LoaiXes
                .Where(lx => lx.MaLoaiXe == id)
                .Select(lx => new {
                    lx.MaLoaiXe,
                    lx.TenLoaiXe,
                    lx.MoTa,
                    SoLuongXe = _context.Xes.Count(x => x.MaLoaiXe == lx.MaLoaiXe)
                })
                .FirstOrDefaultAsync();

            if (loaiXe == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy loại xe" });
            }

            return Ok(new { success = true, data = loaiXe });
        }

        // POST: api/LoaiXeApi
        [HttpPost]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> CreateLoaiXe([FromBody] LoaiXe loaiXe)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            try
            {
                _context.LoaiXes.Add(loaiXe);
                await _context.SaveChangesAsync();

                return Ok(new { 
                    success = true, 
                    message = "Thêm loại xe thành công!",
                    data = new {
                        loaiXe.MaLoaiXe,
                        loaiXe.TenLoaiXe,
                        loaiXe.MoTa
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi thêm loại xe. Vui lòng thử lại." });
            }
        }

        // PUT: api/LoaiXeApi/5
        [HttpPut("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> UpdateLoaiXe(int id, [FromBody] LoaiXe loaiXe)
        {
            if (id != loaiXe.MaLoaiXe)
            {
                return BadRequest(new { success = false, message = "ID không khớp" });
            }

            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            try
            {
                _context.Update(loaiXe);
                await _context.SaveChangesAsync();

                return Ok(new { 
                    success = true, 
                    message = "Cập nhật loại xe thành công!",
                    data = new {
                        loaiXe.MaLoaiXe,
                        loaiXe.TenLoaiXe,
                        loaiXe.MoTa
                    }
                });
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!LoaiXeExists(loaiXe.MaLoaiXe))
                {
                    return NotFound(new { success = false, message = "Loại xe không tồn tại" });
                }
                else
                {
                    throw;
                }
            }
        }

        // DELETE: api/LoaiXeApi/5
        [HttpDelete("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> DeleteLoaiXe(int id)
        {
            var loaiXe = await _context.LoaiXes.FindAsync(id);
            if (loaiXe == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy loại xe" });
            }

            // Kiểm tra xem có xe nào thuộc loại này không
            var hasCars = await _context.Xes.AnyAsync(x => x.MaLoaiXe == id);
            if (hasCars)
            {
                return BadRequest(new { success = false, message = "Không thể xóa loại xe này vì còn có xe thuộc loại này" });
            }

            try
            {
                _context.LoaiXes.Remove(loaiXe);
                await _context.SaveChangesAsync();

                return Ok(new { success = true, message = "Xóa loại xe thành công!" });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi xóa loại xe. Vui lòng thử lại." });
            }
        }

        // GET: api/LoaiXeApi/{id}/xes
        [HttpGet("{id}/xes")]
        public async Task<IActionResult> GetXesByLoaiXe(int id)
        {
            var xes = await _context.Xes
                .Include(x => x.LoaiXe)
                .Where(x => x.MaLoaiXe == id)
                .Select(x => new {
                    x.MaXe,
                    x.BienSoXe,
                    x.TenXe,
                    x.TrangThai,
                    LoaiXe = new {
                        x.LoaiXe.MaLoaiXe,
                        x.LoaiXe.TenLoaiXe,
                        x.LoaiXe.MoTa
                    }
                })
                .ToListAsync();

            return Ok(new { success = true, data = xes });
        }

        // GET: api/LoaiXeApi/{id}/prices
        [HttpGet("{id}/prices")]
        public async Task<IActionResult> GetPricesByLoaiXe(int id)
        {
            var prices = await _context.BangGias
                .Include(bg => bg.LoaiXe)
                .Where(bg => bg.MaLoaiXe == id)
                .OrderByDescending(bg => bg.NgayApDung)
                .Select(bg => new {
                    bg.MaBangGia,
                    bg.DonGiaTheoNgay,
                    bg.NgayApDung,
                    LoaiXe = new {
                        bg.LoaiXe.MaLoaiXe,
                        bg.LoaiXe.TenLoaiXe
                    }
                })
                .ToListAsync();

            return Ok(new { success = true, data = prices });
        }

        private bool LoaiXeExists(int id)
        {
            return _context.LoaiXes.Any(e => e.MaLoaiXe == id);
        }
    }
}
