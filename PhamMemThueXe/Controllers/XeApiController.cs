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
    public class XeApiController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public XeApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/XeApi
        [HttpGet]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> GetXes()
        {
            try
            {
                var xes = await _context.Xes
                    .Include(x => x.LoaiXe)
                    .Select(x => new {
                        x.MaXe,
                        x.BienSoXe,
                        x.TenXe,
                        x.TrangThai,
                        x.ImageURL,
                        LoaiXe = x.LoaiXe != null ? new {
                            x.LoaiXe.MaLoaiXe,
                            x.LoaiXe.TenLoaiXe,
                            x.LoaiXe.MoTa
                        } : null
                    })
                    .ToListAsync();

                return Ok(new { success = true, data = xes });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { 
                    success = false, 
                    message = $"Có lỗi xảy ra: {ex.Message}",
                    innerException = ex.InnerException?.Message
                });
            }
        }

        // GET: api/XeApi/5
        [HttpGet("{id}")]
        [Authorize(Roles = "Admin,Ketoan,Customer")]
        public async Task<IActionResult> GetXe(int id)
        {
            try
            {
                var xe = await _context.Xes
                    .Include(x => x.LoaiXe)
                    .Where(x => x.MaXe == id)
                    .Select(x => new {
                        x.MaXe,
                        x.BienSoXe,
                        x.TenXe,
                        x.TrangThai,
                        x.MaLoaiXe,
                        x.ImageURL,
                        LoaiXe = x.LoaiXe != null ? new {
                            x.LoaiXe.MaLoaiXe,
                            x.LoaiXe.TenLoaiXe,
                            x.LoaiXe.MoTa
                        } : null
                    })
                    .FirstOrDefaultAsync();

                if (xe == null)
                {
                    return NotFound(new { success = false, message = "Không tìm thấy xe" });
                }

                return Ok(new { 
                    success = true, 
                    data = new {
                        xe.MaXe,
                        xe.BienSoXe,
                        xe.TenXe,
                        xe.TrangThai,
                        xe.MaLoaiXe,
                        xe.ImageURL,
                        LoaiXe = xe.LoaiXe != null ? new {
                            xe.LoaiXe.MaLoaiXe,
                            xe.LoaiXe.TenLoaiXe,
                            xe.LoaiXe.MoTa
                        } : null
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // POST: api/XeApi
        [HttpPost]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> CreateXe([FromBody] Xe xe)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            // Kiểm tra biển số xe đã tồn tại chưa
            var existingXe = await _context.Xes
                .FirstOrDefaultAsync(x => x.BienSoXe == xe.BienSoXe);

            if (existingXe != null)
            {
                return BadRequest(new { success = false, message = "Biển số xe đã tồn tại" });
            }

            try
            {
                _context.Xes.Add(xe);
                await _context.SaveChangesAsync();
                
                // Reload với navigation property để trả về đầy đủ thông tin
                var xeCreated = await _context.Xes
                    .Include(x => x.LoaiXe)
                    .FirstOrDefaultAsync(x => x.MaXe == xe.MaXe);

                return Ok(new { 
                    success = true, 
                    message = "Thêm xe thành công!",
                    data = new {
                        xeCreated.MaXe,
                        xeCreated.BienSoXe,
                        xeCreated.TenXe,
                        xeCreated.TrangThai,
                        xeCreated.MaLoaiXe,
                        xeCreated.ImageURL,
                        LoaiXe = xeCreated.LoaiXe != null ? new {
                            xeCreated.LoaiXe.MaLoaiXe,
                            xeCreated.LoaiXe.TenLoaiXe,
                            xeCreated.LoaiXe.MoTa
                        } : null
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi thêm xe. Vui lòng thử lại." });
            }
        }

        // PUT: api/XeApi/5
        [HttpPut("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> UpdateXe(int id, [FromBody] Xe xe)
        {
            if (id != xe.MaXe)
            {
                return BadRequest(new { success = false, message = "ID không khớp" });
            }

            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            // Kiểm tra biển số xe đã tồn tại chưa (trừ xe hiện tại)
            var duplicateXe = await _context.Xes
                .FirstOrDefaultAsync(x => x.BienSoXe == xe.BienSoXe && x.MaXe != id);

            if (duplicateXe != null)
            {
                return BadRequest(new { success = false, message = "Biển số xe đã tồn tại" });
            }

            try
            {
                var existingXe = await _context.Xes.FindAsync(id);
                if (existingXe == null)
                {
                    return NotFound(new { success = false, message = "Không tìm thấy xe" });
                }

                existingXe.BienSoXe = xe.BienSoXe;
                existingXe.TenXe = xe.TenXe;
                existingXe.TrangThai = xe.TrangThai;
                existingXe.MaLoaiXe = xe.MaLoaiXe;
                existingXe.ImageURL = xe.ImageURL;

                _context.Update(existingXe);
                await _context.SaveChangesAsync();
                
                // Reload với navigation property để trả về đầy đủ thông tin
                var xeUpdated = await _context.Xes
                    .Include(x => x.LoaiXe)
                    .FirstOrDefaultAsync(x => x.MaXe == id);

                return Ok(new { 
                    success = true, 
                    message = "Cập nhật xe thành công!",
                    data = new {
                        xeUpdated.MaXe,
                        xeUpdated.BienSoXe,
                        xeUpdated.TenXe,
                        xeUpdated.TrangThai,
                        xeUpdated.MaLoaiXe,
                        xeUpdated.ImageURL,
                        LoaiXe = xeUpdated.LoaiXe != null ? new {
                            xeUpdated.LoaiXe.MaLoaiXe,
                            xeUpdated.LoaiXe.TenLoaiXe,
                            xeUpdated.LoaiXe.MoTa
                        } : null
                    }
                });
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!XeExists(xe.MaXe))
                {
                    return NotFound(new { success = false, message = "Xe không tồn tại" });
                }
                else
                {
                    throw;
                }
            }
        }

        // DELETE: api/XeApi/5
        [HttpDelete("{id}")]
        [Authorize(Roles = "Admin")]
        public async Task<IActionResult> DeleteXe(int id)
        {
            var xe = await _context.Xes.FindAsync(id);
            if (xe == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy xe" });
            }

            try
            {
                _context.Xes.Remove(xe);
                await _context.SaveChangesAsync();

                return Ok(new { success = true, message = "Xóa xe thành công!" });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi xóa xe. Vui lòng thử lại." });
            }
        }

        // GET: api/XeApi/available
        [HttpGet("available")]
        [Authorize(Roles = "Admin,Ketoan,Customer")]
        public async Task<IActionResult> GetAvailableXes()
        {
            try
            {
                var availableXes = await _context.Xes
                    .Include(x => x.LoaiXe)
                    .Where(x => x.TrangThai == "Sẵn sàng")
                    .Select(x => new {
                        x.MaXe,
                        x.BienSoXe,
                        x.TenXe,
                        x.TrangThai,
                        x.ImageURL,
                        LoaiXe = x.LoaiXe != null ? new {
                            x.LoaiXe.MaLoaiXe,
                            x.LoaiXe.TenLoaiXe,
                            x.LoaiXe.MoTa
                        } : null
                    })
                    .ToListAsync();

                return Ok(new { success = true, data = availableXes });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // GET: api/XeApi/by-type/{loaiXeId}
        [HttpGet("by-type/{loaiXeId}")]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> GetXesByType(int loaiXeId)
        {
            try
            {
                var xes = await _context.Xes
                    .Include(x => x.LoaiXe)
                    .Where(x => x.MaLoaiXe == loaiXeId)
                    .Select(x => new {
                        x.MaXe,
                        x.BienSoXe,
                        x.TenXe,
                        x.TrangThai,
                        x.ImageURL,
                        LoaiXe = x.LoaiXe != null ? new {
                            x.LoaiXe.MaLoaiXe,
                            x.LoaiXe.TenLoaiXe,
                            x.LoaiXe.MoTa
                        } : null
                    })
                    .ToListAsync();

                return Ok(new { success = true, data = xes });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // PUT: api/XeApi/{id}/status
        [HttpPut("{id}/status")]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> UpdateXeStatus(int id, [FromBody] UpdateXeStatusRequest request)
        {
            var xe = await _context.Xes.FindAsync(id);
            if (xe == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy xe" });
            }

            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            try
            {
                xe.TrangThai = request.TrangThai;
                _context.Update(xe);
                await _context.SaveChangesAsync();

                return Ok(new { 
                    success = true, 
                    message = "Cập nhật trạng thái xe thành công!",
                    data = new {
                        xe.MaXe,
                        xe.BienSoXe,
                        xe.TenXe,
                        xe.TrangThai,
                        xe.ImageURL
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi cập nhật trạng thái xe." });
            }
        }

        private bool XeExists(int id)
        {
            return _context.Xes.Any(e => e.MaXe == id);
        }
    }

    public class UpdateXeStatusRequest
    {
        public string TrangThai { get; set; } = string.Empty;
    }
}
