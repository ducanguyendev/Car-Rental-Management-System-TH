using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PhamMemThueXe.Data;
using PhamMemThueXe.Models;

namespace PhamMemThueXe.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    [Authorize(Roles = "Admin,Ketoan,Customer")]
    public class BangGiaApiController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public BangGiaApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/BangGiaApi
        [HttpGet]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> GetBangGias()
        {
            try
            {
                var bangGias = await _context.BangGias
                    .Include(bg => bg.LoaiXe)
                    .Select(bg => new {
                        bg.MaBangGia,
                        bg.DonGiaTheoNgay,
                        bg.NgayApDung,
                        LoaiXe = bg.LoaiXe != null ? new {
                            bg.LoaiXe.MaLoaiXe,
                            bg.LoaiXe.TenLoaiXe
                        } : null
                    })
                    .OrderByDescending(bg => bg.NgayApDung)
                    .ToListAsync();

                return Ok(new { success = true, data = bangGias });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // GET: api/BangGiaApi/5
        [HttpGet("{id}")]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> GetBangGia(int id)
        {
            try
            {
                var bangGia = await _context.BangGias
                    .Include(bg => bg.LoaiXe)
                    .Where(bg => bg.MaBangGia == id)
                    .Select(bg => new {
                        bg.MaBangGia,
                        bg.MaLoaiXe,
                        bg.DonGiaTheoNgay,
                        bg.NgayApDung,
                        LoaiXe = bg.LoaiXe != null ? new {
                            bg.LoaiXe.MaLoaiXe,
                            bg.LoaiXe.TenLoaiXe,
                            bg.LoaiXe.MoTa
                        } : null
                    })
                    .FirstOrDefaultAsync();

                if (bangGia == null)
                {
                    return NotFound(new { success = false, message = "Không tìm thấy bảng giá" });
                }

                return Ok(new { 
                    success = true, 
                    data = new {
                        bangGia.MaBangGia,
                        bangGia.MaLoaiXe,
                        bangGia.DonGiaTheoNgay,
                        bangGia.NgayApDung,
                        LoaiXe = bangGia.LoaiXe != null ? new {
                            bangGia.LoaiXe.MaLoaiXe,
                            bangGia.LoaiXe.TenLoaiXe,
                            bangGia.LoaiXe.MoTa
                        } : null
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // POST: api/BangGiaApi
        [HttpPost]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> CreateBangGia([FromBody] BangGia bangGia)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            // Kiểm tra loại xe có tồn tại không
            var loaiXe = await _context.LoaiXes.FindAsync(bangGia.MaLoaiXe);
            if (loaiXe == null)
            {
                return BadRequest(new { success = false, message = "Loại xe không tồn tại" });
            }

            try
            {
                _context.BangGias.Add(bangGia);
                await _context.SaveChangesAsync();
                
                // Reload với navigation property để trả về đầy đủ thông tin
                var bangGiaCreated = await _context.BangGias
                    .Include(bg => bg.LoaiXe)
                    .FirstOrDefaultAsync(bg => bg.MaBangGia == bangGia.MaBangGia);

                return Ok(new { 
                    success = true, 
                    message = "Thêm bảng giá thành công!",
                    data = new {
                        bangGiaCreated.MaBangGia,
                        bangGiaCreated.DonGiaTheoNgay,
                        bangGiaCreated.NgayApDung,
                        bangGiaCreated.MaLoaiXe,
                        LoaiXe = bangGiaCreated.LoaiXe != null ? new {
                            bangGiaCreated.LoaiXe.MaLoaiXe,
                            bangGiaCreated.LoaiXe.TenLoaiXe
                        } : null
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi thêm bảng giá. Vui lòng thử lại." });
            }
        }

        // PUT: api/BangGiaApi/5
        [HttpPut("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> UpdateBangGia(int id, [FromBody] BangGia bangGia)
        {
            if (id != bangGia.MaBangGia)
            {
                return BadRequest(new { success = false, message = "ID không khớp" });
            }

            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            try
            {
                var existingBangGia = await _context.BangGias.FindAsync(id);
                if (existingBangGia == null)
                {
                    return NotFound(new { success = false, message = "Không tìm thấy bảng giá" });
                }

                existingBangGia.MaLoaiXe = bangGia.MaLoaiXe;
                existingBangGia.DonGiaTheoNgay = bangGia.DonGiaTheoNgay;
                existingBangGia.NgayApDung = bangGia.NgayApDung;

                _context.Update(existingBangGia);
                await _context.SaveChangesAsync();
                
                // Reload với navigation property để trả về đầy đủ thông tin
                var bangGiaUpdated = await _context.BangGias
                    .Include(bg => bg.LoaiXe)
                    .FirstOrDefaultAsync(bg => bg.MaBangGia == id);

                return Ok(new { 
                    success = true, 
                    message = "Cập nhật bảng giá thành công!",
                    data = new {
                        bangGiaUpdated.MaBangGia,
                        bangGiaUpdated.DonGiaTheoNgay,
                        bangGiaUpdated.NgayApDung,
                        bangGiaUpdated.MaLoaiXe,
                        LoaiXe = bangGiaUpdated.LoaiXe != null ? new {
                            bangGiaUpdated.LoaiXe.MaLoaiXe,
                            bangGiaUpdated.LoaiXe.TenLoaiXe
                        } : null
                    }
                });
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!BangGiaExists(bangGia.MaBangGia))
                {
                    return NotFound(new { success = false, message = "Bảng giá không tồn tại" });
                }
                else
                {
                    throw;
                }
            }
        }

        // DELETE: api/BangGiaApi/5
        [HttpDelete("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> DeleteBangGia(int id)
        {
            var bangGia = await _context.BangGias.FindAsync(id);
            if (bangGia == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy bảng giá" });
            }

            try
            {
                _context.BangGias.Remove(bangGia);
                await _context.SaveChangesAsync();

                return Ok(new { success = true, message = "Xóa bảng giá thành công!" });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi xóa bảng giá. Vui lòng thử lại." });
            }
        }

        // GET: api/BangGiaApi/current
        [HttpGet("current")]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> GetCurrentPrices()
        {
            try
            {
                var currentPrices = await _context.BangGias
                    .Include(bg => bg.LoaiXe)
                    .Where(bg => bg.NgayApDung <= DateTime.Today)
                    .GroupBy(bg => bg.MaLoaiXe)
                    .Select(g => g.OrderByDescending(bg => bg.NgayApDung).First())
                    .Select(bg => new {
                        bg.MaBangGia,
                        bg.DonGiaTheoNgay,
                        bg.NgayApDung,
                        LoaiXe = bg.LoaiXe != null ? new {
                            bg.LoaiXe.MaLoaiXe,
                            bg.LoaiXe.TenLoaiXe,
                            bg.LoaiXe.MoTa
                        } : null
                    })
                    .ToListAsync();

                return Ok(new { success = true, data = currentPrices });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // GET: api/BangGiaApi/by-type/{loaiXeId}
        [HttpGet("by-type/{loaiXeId}")]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> GetBangGiasByLoaiXe(int loaiXeId)
        {
            try
            {
                var bangGias = await _context.BangGias
                    .Include(bg => bg.LoaiXe)
                    .Where(bg => bg.MaLoaiXe == loaiXeId)
                    .Select(bg => new {
                        bg.MaBangGia,
                        bg.DonGiaTheoNgay,
                        bg.NgayApDung,
                        LoaiXe = bg.LoaiXe != null ? new {
                            bg.LoaiXe.MaLoaiXe,
                            bg.LoaiXe.TenLoaiXe
                        } : null
                    })
                    .OrderByDescending(bg => bg.NgayApDung)
                    .ToListAsync();

                return Ok(new { success = true, data = bangGias });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // GET: api/BangGiaApi/price-for-type/{loaiXeId}
        [HttpGet("price-for-type/{loaiXeId}")]
        [Authorize(Roles = "Admin,Ketoan,Customer")]
        public async Task<IActionResult> GetCurrentPriceForLoaiXe(int loaiXeId)
        {
            try
            {
                var currentPrice = await _context.BangGias
                    .Include(bg => bg.LoaiXe)
                    .Where(bg => bg.MaLoaiXe == loaiXeId && bg.NgayApDung <= DateTime.Today)
                    .OrderByDescending(bg => bg.NgayApDung)
                    .Select(bg => new {
                        bg.MaBangGia,
                        bg.DonGiaTheoNgay,
                        bg.NgayApDung,
                        LoaiXe = bg.LoaiXe != null ? new {
                            bg.LoaiXe.MaLoaiXe,
                            bg.LoaiXe.TenLoaiXe
                        } : null
                    })
                    .FirstOrDefaultAsync();

                if (currentPrice == null)
                {
                    return NotFound(new { success = false, message = "Không tìm thấy giá cho loại xe này" });
                }

                return Ok(new { success = true, data = currentPrice });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // POST: api/BangGiaApi/update-price
        [HttpPost("update-price")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> UpdatePriceForLoaiXe([FromBody] UpdatePriceRequest request)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            // Kiểm tra loại xe có tồn tại không
            var loaiXe = await _context.LoaiXes.FindAsync(request.MaLoaiXe);
            if (loaiXe == null)
            {
                return BadRequest(new { success = false, message = "Loại xe không tồn tại" });
            }

            try
            {
                var bangGia = new BangGia
                {
                    MaLoaiXe = request.MaLoaiXe,
                    DonGiaTheoNgay = request.DonGiaTheoNgay,
                    NgayApDung = request.NgayApDung ?? DateTime.Today
                };

                _context.BangGias.Add(bangGia);
                await _context.SaveChangesAsync();

                return Ok(new { 
                    success = true, 
                    message = "Cập nhật giá thành công!",
                    data = new {
                        bangGia.MaBangGia,
                        bangGia.DonGiaTheoNgay,
                        bangGia.NgayApDung,
                        bangGia.MaLoaiXe
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi cập nhật giá. Vui lòng thử lại." });
            }
        }

        private bool BangGiaExists(int id)
        {
            return _context.BangGias.Any(e => e.MaBangGia == id);
        }
    }

    public class UpdatePriceRequest
    {
        public int MaLoaiXe { get; set; }
        public decimal DonGiaTheoNgay { get; set; }
        public DateTime? NgayApDung { get; set; }
    }
}
