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
    [Authorize(Roles = "Admin,Ketoan,Customer")]
    public class HopDongApiController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public HopDongApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/HopDongApi
        [HttpGet]
        public async Task<IActionResult> GetHopDongs()
        {
            try
            {
                var hopDongs = await _context.HopDongs
                    .Include(hd => hd.KhachHang)
                    .Include(hd => hd.NhanVien)
                    .Include(hd => hd.ChiTietHopDongs)
                        .ThenInclude(cthd => cthd.Xe)
                    .Select(hd => new {
                        hd.MaHopDong,
                        hd.NgayLap,
                        hd.NgayNhanXe,
                        hd.NgayTraXe,
                        hd.DiaDiemNhan,
                        hd.DiaDiemTra,
                        hd.TongTien,
                        hd.TienDatCoc,
                        hd.TrangThai,
                        KhachHang = hd.KhachHang != null ? new {
                            hd.KhachHang.MaKH,
                            hd.KhachHang.HoTen,
                            hd.KhachHang.SoDienThoai
                        } : null,
                        NhanVien = hd.NhanVien != null ? new {
                            hd.NhanVien.HoTen
                        } : null,
                        ChiTietHopDongs = hd.ChiTietHopDongs.Select(cthd => new {
                            cthd.MaXe,
                            cthd.BienSoXe,
                            cthd.TenXe,
                            cthd.DonGiaTaiThoiDiemThue,
                            cthd.GhiChu
                        })
                    })
                    .OrderByDescending(hd => hd.NgayLap)
                    .ToListAsync();

                return Ok(new { success = true, data = hopDongs });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // GET: api/HopDongApi/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetHopDong(int id)
        {
            var hopDong = await _context.HopDongs
                .Include(hd => hd.KhachHang)
                .Include(hd => hd.NhanVien)
                .Include(hd => hd.ChiTietHopDongs)
                    .ThenInclude(cthd => cthd.Xe)
                .Where(hd => hd.MaHopDong == id)
                .Select(hd => new {
                    hd.MaHopDong,
                    hd.NgayLap,
                    hd.NgayNhanXe,
                    hd.NgayTraXe,
                    hd.DiaDiemNhan,
                    hd.DiaDiemTra,
                    hd.TongTien,
                    hd.TienDatCoc,
                    hd.TrangThai,
                    KhachHang = new {
                        hd.KhachHang.MaKH,
                        hd.KhachHang.HoTen,
                        hd.KhachHang.SoDienThoai,
                        hd.KhachHang.SoCMND
                    },
                    NhanVien = new {
                        hd.NhanVien.HoTen
                    },
                    ChiTietHopDongs = hd.ChiTietHopDongs.Select(cthd => new {
                        cthd.MaXe,
                        cthd.BienSoXe,
                        cthd.TenXe,
                        cthd.DonGiaTaiThoiDiemThue,
                        cthd.GhiChu
                    })
                })
                .FirstOrDefaultAsync();

            if (hopDong == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy hợp đồng" });
            }

            return Ok(new { success = true, data = hopDong });
        }

        // POST: api/HopDongApi
        [HttpPost]
        public async Task<IActionResult> CreateHopDong([FromBody] CreateHopDongRequest request)
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

            // Kiểm tra nhân viên có tồn tại không
            var nhanVien = await _context.TaiKhoans.FindAsync(request.MaNhanVien);
            if (nhanVien == null)
            {
                return BadRequest(new { success = false, message = "Nhân viên không tồn tại" });
            }

            // Kiểm tra các xe có sẵn sàng không
            foreach (var xeRequest in request.ChiTietHopDongs)
            {
                var xe = await _context.Xes.FindAsync(xeRequest.MaXe);
                if (xe == null)
                {
                    return BadRequest(new { success = false, message = $"Xe với ID {xeRequest.MaXe} không tồn tại" });
                }
                if (xe.TrangThai != "Sẵn sàng")
                {
                    return BadRequest(new { success = false, message = $"Xe {xe.BienSoXe} không sẵn sàng để cho thuê" });
                }
            }

            try
            {
                using var transaction = await _context.Database.BeginTransactionAsync();

                // Tạo hợp đồng
                var hopDong = new HopDong
                {
                    MaKH = request.MaKH,
                    MaNhanVien = request.MaNhanVien,
                    MaDatXe = request.MaDatXe,
                    HoTenKH = khachHang.HoTen,
                    SoDienThoaiKH = khachHang.SoDienThoai,
                    HoTenNVLap = nhanVien.HoTen,
                    NgayLap = DateTime.Now,
                    NgayNhanXe = request.NgayNhanXe,
                    DiaDiemNhan = request.DiaDiemNhan,
                    NgayTraXe = request.NgayTraXe,
                    DiaDiemTra = request.DiaDiemTra,
                    TongTien = request.TongTien,
                    TienDatCoc = request.TienDatCoc,
                    TrangThai = "Đang thuê"
                };

                _context.HopDongs.Add(hopDong);
                await _context.SaveChangesAsync();

                // Tạo chi tiết hợp đồng và cập nhật trạng thái xe
                foreach (var xeRequest in request.ChiTietHopDongs)
                {
                    var xe = await _context.Xes.FindAsync(xeRequest.MaXe);
                    
                    var chiTietHopDong = new ChiTietHopDong
                    {
                        MaHopDong = hopDong.MaHopDong,
                        MaXe = xeRequest.MaXe,
                        BienSoXe = xe.BienSoXe,
                        TenXe = xe.TenXe,
                        DonGiaTaiThoiDiemThue = xeRequest.DonGiaTaiThoiDiemThue,
                        GhiChu = xeRequest.GhiChu
                    };

                    _context.ChiTietHopDongs.Add(chiTietHopDong);

                    // Cập nhật trạng thái xe
                    xe.TrangThai = "Đang cho thuê";
                    _context.Update(xe);
                }

                // Cập nhật trạng thái phiếu đặt xe nếu có
                if (request.MaDatXe.HasValue)
                {
                    var phieuDatXe = await _context.PhieuDatXes.FindAsync(request.MaDatXe.Value);
                    if (phieuDatXe != null)
                    {
                        phieuDatXe.TrangThai = "Đã thành hợp đồng";
                        _context.Update(phieuDatXe);
                    }
                }

                await _context.SaveChangesAsync();
                await transaction.CommitAsync();

                return Ok(new { 
                    success = true, 
                    message = "Tạo hợp đồng thành công!",
                    data = new {
                        hopDong.MaHopDong,
                        hopDong.NgayLap,
                        hopDong.TongTien,
                        hopDong.TrangThai
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi tạo hợp đồng. Vui lòng thử lại." });
            }
        }

        // PUT: api/HopDongApi/5/status
        [HttpPut("{id}/status")]
        public async Task<IActionResult> UpdateHopDongStatus(int id, [FromBody] UpdateHopDongStatusRequest request)
        {
            var hopDong = await _context.HopDongs
                .Include(hd => hd.ChiTietHopDongs)
                .FirstOrDefaultAsync(hd => hd.MaHopDong == id);

            if (hopDong == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy hợp đồng" });
            }

            try
            {
                using var transaction = await _context.Database.BeginTransactionAsync();

                hopDong.TrangThai = request.TrangThai;

                // Nếu hoàn thành hợp đồng, cập nhật trạng thái xe về "Sẵn sàng"
                if (request.TrangThai == "Đã hoàn thành")
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
                await transaction.CommitAsync();

                return Ok(new { 
                    success = true, 
                    message = "Cập nhật trạng thái hợp đồng thành công!",
                    data = new {
                        hopDong.MaHopDong,
                        hopDong.TrangThai
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi cập nhật trạng thái hợp đồng." });
            }
        }

        // GET: api/HopDongApi/by-customer/{customerId}
        [HttpGet("by-customer/{customerId}")]
        [Authorize(Roles = "Admin,Ketoan,Customer")]
        public async Task<IActionResult> GetHopDongsByCustomer(int customerId)
        {
            try
            {
                // Kiểm tra bảo mật: Customer chỉ có thể xem hợp đồng của chính mình
                var userRole = User.FindFirst(ClaimTypes.Role)?.Value;
                if (userRole == "Customer")
                {
                    var tenDangNhap = User.FindFirst("TenDangNhap")?.Value;
                    var user = await _context.TaiKhoans.FirstOrDefaultAsync(u => u.TenDangNhap == tenDangNhap);
                    if (user != null)
                    {
                        var khachHang = await _context.KhachHangs.FirstOrDefaultAsync(kh => kh.HoTen == user.HoTen);
                        if (khachHang == null || khachHang.MaKH != customerId)
                        {
                            return BadRequest(new { success = false, message = "Bạn chỉ có thể xem hợp đồng của chính mình" });
                        }
                    }
                }

                var hopDongs = await _context.HopDongs
                    .Include(hd => hd.KhachHang)
                    .Include(hd => hd.NhanVien)
                    .Include(hd => hd.ChiTietHopDongs)
                    .Where(hd => hd.MaKH == customerId)
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
                        },
                        ChiTietHopDongs = hd.ChiTietHopDongs.Select(cthd => new {
                            cthd.BienSoXe,
                            cthd.TenXe,
                            cthd.DonGiaTaiThoiDiemThue
                        })
                    })
                    .OrderByDescending(hd => hd.NgayLap)
                    .ToListAsync();

                return Ok(new { success = true, data = hopDongs });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // GET: api/HopDongApi/by-status/{status}
        [HttpGet("by-status/{status}")]
        public async Task<IActionResult> GetHopDongsByStatus(string status)
        {
            var hopDongs = await _context.HopDongs
                .Include(hd => hd.KhachHang)
                .Include(hd => hd.NhanVien)
                .Where(hd => hd.TrangThai == status)
                .Select(hd => new {
                    hd.MaHopDong,
                    hd.NgayLap,
                    hd.NgayNhanXe,
                    hd.NgayTraXe,
                    hd.TongTien,
                    hd.TienDatCoc,
                    hd.TrangThai,
                    KhachHang = new {
                        hd.KhachHang.HoTen,
                        hd.KhachHang.SoDienThoai
                    },
                    NhanVien = new {
                        hd.NhanVien.HoTen
                    }
                })
                .OrderByDescending(hd => hd.NgayLap)
                .ToListAsync();

            return Ok(new { success = true, data = hopDongs });
        }

        // GET: api/HopDongApi/statistics
        [HttpGet("statistics")]
        public async Task<IActionResult> GetHopDongStatistics()
        {
            try
            {
                var totalContracts = await _context.HopDongs.CountAsync();
                var activeContracts = await _context.HopDongs.CountAsync(hd => hd.TrangThai == "Đang thuê");
                var completedContracts = await _context.HopDongs.CountAsync(hd => hd.TrangThai == "Đã hoàn thành");
                var totalRevenue = await _context.HopDongs.SumAsync(hd => hd.TongTien);
                var totalDeposit = await _context.HopDongs.SumAsync(hd => hd.TienDatCoc);

                return Ok(new { 
                    success = true, 
                    data = new {
                        totalContracts,
                        activeContracts,
                        completedContracts,
                        totalRevenue,
                        totalDeposit
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi lấy thống kê hợp đồng." });
            }
        }

        // GET: api/HopDongApi/by-username/{username}
        [HttpGet("by-username/{username}")]
        [Authorize(Roles = "Customer")]
        public async Task<IActionResult> GetHopDongsByUsername(string username)
        {
            try
            {
                var tk = await _context.TaiKhoans.FirstOrDefaultAsync(x => x.TenDangNhap == username);
                if (tk == null)
                    return NotFound(new { success = false, message = "Không tìm thấy tài khoản người dùng" });

                // Mapping best effort: lấy KhachHang cùng HoTen (giả định duy nhất)
                var kh = await _context.KhachHangs.FirstOrDefaultAsync(h => h.HoTen == tk.HoTen);
                if (kh == null)
                    return NotFound(new { success = false, message = "Không tìm thấy hồ sơ khách hàng ứng với tài khoản này" });

                var ds = await _context.HopDongs
                    .Where(hd => hd.MaKH == kh.MaKH)
                    .OrderByDescending(hd => hd.NgayLap)
                    .Select(hd => new {
                        hd.MaHopDong,
                        hd.NgayLap,
                        hd.NgayNhanXe,
                        hd.NgayTraXe,
                        hd.TongTien,
                        hd.TrangThai
                    })
                    .ToListAsync();
                return Ok(new { success = true, data = ds });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        private bool HopDongExists(int id)
        {
            return _context.HopDongs.Any(e => e.MaHopDong == id);
        }
    }

    public class CreateHopDongRequest
    {
        public int MaKH { get; set; }
        public int MaNhanVien { get; set; }
        public int? MaDatXe { get; set; }
        public DateTime NgayNhanXe { get; set; }
        public string DiaDiemNhan { get; set; } = string.Empty;
        public DateTime NgayTraXe { get; set; }
        public string DiaDiemTra { get; set; } = string.Empty;
        public decimal TongTien { get; set; }
        public decimal TienDatCoc { get; set; }
        public List<ChiTietHopDongRequest> ChiTietHopDongs { get; set; } = new List<ChiTietHopDongRequest>();
    }

    public class ChiTietHopDongRequest
    {
        public int MaXe { get; set; }
        public decimal DonGiaTaiThoiDiemThue { get; set; }
        public string? GhiChu { get; set; }
    }

    public class UpdateHopDongStatusRequest
    {
        public string TrangThai { get; set; } = string.Empty;
    }
}
