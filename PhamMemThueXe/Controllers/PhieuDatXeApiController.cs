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
    public class PhieuDatXeApiController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public PhieuDatXeApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/PhieuDatXeApi
        [HttpGet]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> GetPhieuDatXes()
        {
            try
            {
                var phieuDatXes = await _context.PhieuDatXes
                    .Select(pdx => new {
                        pdx.MaDatXe,
                        pdx.NgayDat,
                        pdx.NgayDuKienNhan,
                        pdx.TrangThai,
                        pdx.MaKH,
                        pdx.MaLoaiXe,
                        KhachHang = _context.KhachHangs
                            .Where(kh => kh.MaKH == pdx.MaKH)
                            .Select(kh => new {
                                kh.MaKH,
                                kh.HoTen,
                                kh.SoDienThoai
                            })
                            .FirstOrDefault(),
                        LoaiXe = _context.LoaiXes
                            .Where(lx => lx.MaLoaiXe == pdx.MaLoaiXe)
                            .Select(lx => new {
                                lx.MaLoaiXe,
                                lx.TenLoaiXe
                            })
                            .FirstOrDefault()
                    })
                    .OrderByDescending(pdx => pdx.NgayDat)
                    .ToListAsync();

                return Ok(new { success = true, data = phieuDatXes });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // GET: api/PhieuDatXeApi/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetPhieuDatXe(int id)
        {
            try
            {
                var phieuDatXe = await _context.PhieuDatXes
                    .Include(pdx => pdx.KhachHang)
                    .Include(pdx => pdx.LoaiXe)
                    .Where(pdx => pdx.MaDatXe == id)
                    .Select(pdx => new {
                        pdx.MaDatXe,
                        pdx.NgayDat,
                        pdx.NgayDuKienNhan,
                        pdx.TrangThai,
                        KhachHang = pdx.KhachHang != null ? new {
                            pdx.KhachHang.MaKH,
                            pdx.KhachHang.HoTen,
                            pdx.KhachHang.SoDienThoai,
                            pdx.KhachHang.SoCMND
                        } : null,
                        LoaiXe = pdx.LoaiXe != null ? new {
                            pdx.LoaiXe.MaLoaiXe,
                            pdx.LoaiXe.TenLoaiXe,
                            pdx.LoaiXe.MoTa
                        } : null
                    })
                    .FirstOrDefaultAsync();

                if (phieuDatXe == null)
                {
                    return NotFound(new { success = false, message = "Không tìm thấy phiếu đặt xe" });
                }

                return Ok(new { success = true, data = phieuDatXe });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // POST: api/PhieuDatXeApi
        [HttpPost]
        public async Task<IActionResult> CreatePhieuDatXe([FromBody] CreatePhieuDatXeRequest request)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            // Kiểm tra khách hàng có tồn tại không
            var khachHang = await _context.KhachHangs.FindAsync(request.MaKH);
            if (khachHang == null)
            {
                return BadRequest(new { success = false, message = "Khách hàng không tồn tại" });
            }

            // Kiểm tra loại xe có tồn tại không
            var loaiXe = await _context.LoaiXes.FindAsync(request.MaLoaiXe);
            if (loaiXe == null)
            {
                return BadRequest(new { success = false, message = "Loại xe không tồn tại" });
            }

            try
            {
                var phieuDatXe = new PhieuDatXe
                {
                    MaKH = request.MaKH,
                    MaLoaiXe = request.MaLoaiXe,
                    HoTenKH = khachHang.HoTen,
                    TenLoaiXeDat = loaiXe.TenLoaiXe,
                    NgayDat = DateTime.Now,
                    NgayDuKienNhan = request.NgayDuKienNhan,
                    TrangThai = "Đang chờ"
                };

                _context.PhieuDatXes.Add(phieuDatXe);
                await _context.SaveChangesAsync();

                return Ok(new { 
                    success = true, 
                    message = "Tạo phiếu đặt xe thành công!",
                    data = new {
                        phieuDatXe.MaDatXe,
                        phieuDatXe.NgayDat,
                        phieuDatXe.NgayDuKienNhan,
                        phieuDatXe.TrangThai,
                        KhachHang = new {
                            khachHang.MaKH,
                            khachHang.HoTen,
                            khachHang.SoDienThoai
                        },
                        LoaiXe = new {
                            loaiXe.MaLoaiXe,
                            loaiXe.TenLoaiXe
                        }
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi tạo phiếu đặt xe. Vui lòng thử lại." });
            }
        }

        // PUT: api/PhieuDatXeApi/5/status
        [HttpPut("{id}/status")]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> UpdatePhieuDatXeStatus(int id, [FromBody] UpdatePhieuDatXeStatusRequest request)
        {
            var phieuDatXe = await _context.PhieuDatXes.FindAsync(id);
            if (phieuDatXe == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy phiếu đặt xe" });
            }

            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            try
            {
                phieuDatXe.TrangThai = request.TrangThai;
                _context.Update(phieuDatXe);
                await _context.SaveChangesAsync();

                return Ok(new { 
                    success = true, 
                    message = "Cập nhật trạng thái phiếu đặt xe thành công!",
                    data = new {
                        phieuDatXe.MaDatXe,
                        phieuDatXe.TrangThai
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi cập nhật trạng thái phiếu đặt xe." });
            }
        }

        // DELETE: api/PhieuDatXeApi/5
        [HttpDelete("{id}")]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> DeletePhieuDatXe(int id)
        {
            var phieuDatXe = await _context.PhieuDatXes.FindAsync(id);
            if (phieuDatXe == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy phiếu đặt xe" });
            }

            // Kiểm tra xem có hợp đồng nào liên quan không
            var hasContract = await _context.HopDongs.AnyAsync(hd => hd.MaDatXe == id);
            if (hasContract)
            {
                return BadRequest(new { success = false, message = "Không thể xóa phiếu đặt xe này vì đã có hợp đồng liên quan" });
            }

            try
            {
                _context.PhieuDatXes.Remove(phieuDatXe);
                await _context.SaveChangesAsync();

                return Ok(new { success = true, message = "Xóa phiếu đặt xe thành công!" });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi xóa phiếu đặt xe. Vui lòng thử lại." });
            }
        }

        // GET: api/PhieuDatXeApi/by-customer/{customerId}
        [HttpGet("by-customer/{customerId}")]
        public async Task<IActionResult> GetPhieuDatXesByCustomer(int customerId)
        {
            try
            {
                var phieuDatXes = await _context.PhieuDatXes
                    .Include(pdx => pdx.KhachHang)
                    .Include(pdx => pdx.LoaiXe)
                    .Where(pdx => pdx.MaKH == customerId)
                    .Select(pdx => new {
                        pdx.MaDatXe,
                        pdx.NgayDat,
                        pdx.NgayDuKienNhan,
                        pdx.TrangThai,
                        KhachHang = pdx.KhachHang != null ? new {
                            pdx.KhachHang.MaKH,
                            pdx.KhachHang.HoTen,
                            pdx.KhachHang.SoDienThoai
                        } : null,
                        LoaiXe = pdx.LoaiXe != null ? new {
                            pdx.LoaiXe.MaLoaiXe,
                            pdx.LoaiXe.TenLoaiXe
                        } : null
                    })
                    .OrderByDescending(pdx => pdx.NgayDat)
                    .ToListAsync();

                return Ok(new { success = true, data = phieuDatXes });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // GET: api/PhieuDatXeApi/by-status/{status}
        [HttpGet("by-status/{status}")]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> GetPhieuDatXesByStatus(string status)
        {
            try
            {
                var phieuDatXes = await _context.PhieuDatXes
                    .Include(pdx => pdx.KhachHang)
                    .Include(pdx => pdx.LoaiXe)
                    .Where(pdx => pdx.TrangThai == status)
                    .Select(pdx => new {
                        pdx.MaDatXe,
                        pdx.NgayDat,
                        pdx.NgayDuKienNhan,
                        pdx.TrangThai,
                        KhachHang = pdx.KhachHang != null ? new {
                            pdx.KhachHang.MaKH,
                            pdx.KhachHang.HoTen,
                            pdx.KhachHang.SoDienThoai
                        } : null,
                        LoaiXe = pdx.LoaiXe != null ? new {
                            pdx.LoaiXe.MaLoaiXe,
                            pdx.LoaiXe.TenLoaiXe
                        } : null
                    })
                    .OrderByDescending(pdx => pdx.NgayDat)
                    .ToListAsync();

                return Ok(new { success = true, data = phieuDatXes });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // GET: api/PhieuDatXeApi/by-username/{username}
        [HttpGet("by-username/{username}")]
        [Authorize(Roles = "Customer")]
        public async Task<IActionResult> GetPhieuDatXesByUsername(string username)
        {
            try
            {
                // Tìm tài khoản
                var tk = await _context.TaiKhoans.FirstOrDefaultAsync(t => t.TenDangNhap == username);
                if (tk == null)
                {
                    return NotFound(new { success = false, message = "Không tìm thấy tài khoản người dùng" });
                }
                // Mapping basic: lấy KhachHang khớp HoTen và SoDienThoai nếu có (vì bảng không lưu TenDangNhap)
                var kh = await _context.KhachHangs.FirstOrDefaultAsync(kh => kh.HoTen == tk.HoTen);
                if (kh == null)
                {
                    return NotFound(new { success = false, message = "Không tìm thấy hồ sơ khách hàng ứng với tài khoản này" });
                }
                var phieuDatXes = await _context.PhieuDatXes
                    .Where(pdx => pdx.MaKH == kh.MaKH)
                    .Include(pdx => pdx.LoaiXe)
                    .OrderByDescending(pdx => pdx.NgayDat)
                    .Select(pdx => new {
                        pdx.MaDatXe,
                        pdx.NgayDat,
                        pdx.NgayDuKienNhan,
                        pdx.TrangThai,
                        LoaiXe = pdx.LoaiXe != null ? new {
                            pdx.LoaiXe.MaLoaiXe,
                            pdx.LoaiXe.TenLoaiXe
                        } : null
                    })
                    .ToListAsync();
                return Ok(new { success = true, data = phieuDatXes });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        private bool PhieuDatXeExists(int id)
        {
            return _context.PhieuDatXes.Any(e => e.MaDatXe == id);
        }
    }

    public class CreatePhieuDatXeRequest
    {
        public int MaKH { get; set; }
        public int MaLoaiXe { get; set; }
        public DateTime NgayDuKienNhan { get; set; }
    }

    public class UpdatePhieuDatXeStatusRequest
    {
        public string TrangThai { get; set; } = string.Empty;
    }
}
