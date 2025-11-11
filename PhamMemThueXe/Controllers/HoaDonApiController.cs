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
    public class HoaDonApiController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public HoaDonApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/HoaDonApi
        [HttpGet]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> GetHoaDons()
        {
            try
            {
                var hoaDons = await _context.HoaDons
                    .OrderByDescending(hd => hd.NgayLap)
                    .Select(hd => new {
                        hd.MaHoaDon,
                        hd.NgayLap,
                        hd.SoTienThanhToan,
                        hd.LoaiThanhToan,
                        hd.MaHopDong,
                        hd.MaNhanVien,
                        HopDong = _context.HopDongs
                            .Where(h => h.MaHopDong == hd.MaHopDong)
                            .Select(h => new {
                                h.MaHopDong,
                                h.NgayLap,
                                h.TongTien,
                                h.TrangThai,
                                h.MaKH,
                                KhachHang = _context.KhachHangs
                                    .Where(kh => kh.MaKH == h.MaKH)
                                    .Select(kh => new {
                                        kh.MaKH,
                                        kh.HoTen,
                                        kh.SoDienThoai
                                    })
                                    .FirstOrDefault()
                            })
                            .FirstOrDefault(),
                        NhanVien = _context.TaiKhoans
                            .Where(tk => tk.MaNhanVien == hd.MaNhanVien)
                            .Select(tk => new {
                                tk.MaNhanVien,
                                tk.HoTen
                            })
                            .FirstOrDefault()
                    })
                    .ToListAsync();

                return Ok(new { success = true, data = hoaDons });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // GET: api/HoaDonApi/5
        [HttpGet("{id}")]
        [Authorize(Roles = "Admin,Ketoan,Customer")]
        public async Task<IActionResult> GetHoaDon(int id)
        {
            try
            {
                // Kiểm tra bảo mật: Customer chỉ có thể xem hóa đơn của chính mình
                var userRole = User.FindFirst(ClaimTypes.Role)?.Value;
                if (userRole == "Customer")
                {
                    var tenDangNhap = User.FindFirst("TenDangNhap")?.Value;
                    var user = await _context.TaiKhoans.FirstOrDefaultAsync(u => u.TenDangNhap == tenDangNhap);
                    if (user != null)
                    {
                        var khachHang = await _context.KhachHangs.FirstOrDefaultAsync(kh => kh.HoTen == user.HoTen);
                        if (khachHang != null)
                        {
                            // Kiểm tra hóa đơn này có thuộc về khách hàng không
                            var hoaDonExists = await _context.HoaDons
                                .AnyAsync(hd => hd.MaHoaDon == id && 
                                    _context.HopDongs.Any(h => h.MaHopDong == hd.MaHopDong && h.MaKH == khachHang.MaKH));
                            if (!hoaDonExists)
                            {
                                return BadRequest(new { success = false, message = "Bạn chỉ có thể xem hóa đơn của chính mình" });
                            }
                        }
                    }
                }

                var hoaDon = await _context.HoaDons
                    .Where(hd => hd.MaHoaDon == id)
                    .Select(hd => new {
                        hd.MaHoaDon,
                        hd.NgayLap,
                        hd.SoTienThanhToan,
                        hd.LoaiThanhToan,
                        hd.MaHopDong,
                        hd.MaNhanVien,
                        HopDong = _context.HopDongs
                            .Where(h => h.MaHopDong == hd.MaHopDong)
                            .Select(h => new {
                                h.MaHopDong,
                                h.NgayLap,
                                h.NgayNhanXe,
                                h.NgayTraXe,
                                h.TongTien,
                                h.TienDatCoc,
                                h.TrangThai,
                                h.MaKH,
                                KhachHang = _context.KhachHangs
                                    .Where(kh => kh.MaKH == h.MaKH)
                                    .Select(kh => new {
                                        kh.MaKH,
                                        kh.HoTen,
                                        kh.SoDienThoai,
                                        kh.SoCMND
                                    })
                                    .FirstOrDefault()
                            })
                            .FirstOrDefault(),
                        NhanVien = _context.TaiKhoans
                            .Where(tk => tk.MaNhanVien == hd.MaNhanVien)
                            .Select(tk => new {
                                tk.MaNhanVien,
                                tk.HoTen
                            })
                            .FirstOrDefault()
                    })
                    .FirstOrDefaultAsync();

                if (hoaDon == null)
                {
                    return NotFound(new { success = false, message = "Không tìm thấy hóa đơn" });
                }

                return Ok(new { success = true, data = hoaDon });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // POST: api/HoaDonApi
        [HttpPost]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> CreateHoaDon([FromBody] CreateHoaDonRequest request)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(new { success = false, message = "Dữ liệu không hợp lệ", errors = ModelState });
            }

            // Kiểm tra hợp đồng có tồn tại không
            var hopDong = await _context.HopDongs.FindAsync(request.MaHopDong);
            if (hopDong == null)
            {
                return BadRequest(new { success = false, message = "Hợp đồng không tồn tại" });
            }

            // Kiểm tra nhân viên có tồn tại không
            var nhanVien = await _context.TaiKhoans.FindAsync(request.MaNhanVien);
            if (nhanVien == null)
            {
                return BadRequest(new { success = false, message = "Nhân viên không tồn tại" });
            }

            // Kiểm tra số tiền thanh toán có hợp lệ không
            var totalPaid = await _context.HoaDons
                .Where(hd => hd.MaHopDong == request.MaHopDong)
                .SumAsync(hd => hd.SoTienThanhToan);

            if (totalPaid + request.SoTienThanhToan > hopDong.TongTien)
            {
                return BadRequest(new { success = false, message = "Số tiền thanh toán vượt quá tổng tiền hợp đồng" });
            }

            try
            {
                var hoaDon = new HoaDon
                {
                    MaHopDong = request.MaHopDong,
                    MaNhanVien = request.MaNhanVien,
                    HoTenNVLap = nhanVien.HoTen,
                    NgayLap = DateTime.Now,
                    SoTienThanhToan = request.SoTienThanhToan,
                    LoaiThanhToan = request.LoaiThanhToan
                };

                _context.HoaDons.Add(hoaDon);
                await _context.SaveChangesAsync();

                return Ok(new { 
                    success = true, 
                    message = "Tạo hóa đơn thành công!",
                    data = new {
                        hoaDon.MaHoaDon,
                        hoaDon.NgayLap,
                        hoaDon.SoTienThanhToan,
                        hoaDon.LoaiThanhToan
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi tạo hóa đơn. Vui lòng thử lại." });
            }
        }

        // GET: api/HoaDonApi/by-contract/{contractId}
        [HttpGet("by-contract/{contractId}")]
        [Authorize(Roles = "Admin,Ketoan")]
        public async Task<IActionResult> GetHoaDonsByContract(int contractId)
        {
            try
            {
                var hoaDons = await _context.HoaDons
                    .Where(hd => hd.MaHopDong == contractId)
                    .Select(hd => new {
                        hd.MaHoaDon,
                        hd.NgayLap,
                        hd.SoTienThanhToan,
                        hd.LoaiThanhToan,
                        hd.MaNhanVien,
                        NhanVien = _context.TaiKhoans
                            .Where(tk => tk.MaNhanVien == hd.MaNhanVien)
                            .Select(tk => new {
                                tk.MaNhanVien,
                                tk.HoTen
                            })
                            .FirstOrDefault()
                    })
                    .OrderByDescending(hd => hd.NgayLap)
                    .ToListAsync();

                return Ok(new { success = true, data = hoaDons });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // GET: api/HoaDonApi/by-customer/{customerId}
        [HttpGet("by-customer/{customerId}")]
        [Authorize(Roles = "Admin,Ketoan,Customer")]
        public async Task<IActionResult> GetHoaDonsByCustomer(int customerId)
        {
            try
            {
                // Kiểm tra bảo mật: Customer chỉ có thể xem hóa đơn của chính mình
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
                            return BadRequest(new { success = false, message = "Bạn chỉ có thể xem hóa đơn của chính mình" });
                        }
                    }
                }

                var hoaDons = await _context.HoaDons
                    .Where(hd => _context.HopDongs.Any(h => h.MaHopDong == hd.MaHopDong && h.MaKH == customerId))
                    .Select(hd => new {
                        hd.MaHoaDon,
                        hd.NgayLap,
                        hd.SoTienThanhToan,
                        hd.LoaiThanhToan,
                        hd.MaHopDong,
                        hd.MaNhanVien,
                        HopDong = _context.HopDongs
                            .Where(h => h.MaHopDong == hd.MaHopDong)
                            .Select(h => new {
                                h.MaHopDong,
                                h.NgayLap,
                                h.TongTien
                            })
                            .FirstOrDefault(),
                        NhanVien = _context.TaiKhoans
                            .Where(tk => tk.MaNhanVien == hd.MaNhanVien)
                            .Select(tk => new {
                                tk.MaNhanVien,
                                tk.HoTen
                            })
                            .FirstOrDefault()
                    })
                    .OrderByDescending(hd => hd.NgayLap)
                    .ToListAsync();

                return Ok(new { success = true, data = hoaDons });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = $"Có lỗi xảy ra: {ex.Message}" });
            }
        }

        // GET: api/HoaDonApi/statistics
        [HttpGet("statistics")]
        public async Task<IActionResult> GetHoaDonStatistics()
        {
            try
            {
                var totalInvoices = await _context.HoaDons.CountAsync();
                var totalAmount = await _context.HoaDons.SumAsync(hd => hd.SoTienThanhToan);
                
                var invoicesByType = await _context.HoaDons
                    .GroupBy(hd => hd.LoaiThanhToan)
                    .Select(g => new {
                        LoaiThanhToan = g.Key,
                        SoLuong = g.Count(),
                        TongTien = g.Sum(hd => hd.SoTienThanhToan)
                    })
                    .ToListAsync();

                var monthlyStats = await _context.HoaDons
                    .Where(hd => hd.NgayLap.Year == DateTime.Now.Year)
                    .GroupBy(hd => hd.NgayLap.Month)
                    .Select(g => new {
                        Thang = g.Key,
                        SoLuong = g.Count(),
                        TongTien = g.Sum(hd => hd.SoTienThanhToan)
                    })
                    .OrderBy(g => g.Thang)
                    .ToListAsync();

                return Ok(new { 
                    success = true, 
                    data = new {
                        totalInvoices,
                        totalAmount,
                        invoicesByType,
                        monthlyStats
                    }
                });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi lấy thống kê hóa đơn." });
            }
        }

        // GET: api/HoaDonApi/payment-summary/{contractId}
        [HttpGet("payment-summary/{contractId}")]
        public async Task<IActionResult> GetPaymentSummary(int contractId)
        {
            var hopDong = await _context.HopDongs.FindAsync(contractId);
            if (hopDong == null)
            {
                return NotFound(new { success = false, message = "Không tìm thấy hợp đồng" });
            }

            var totalPaid = await _context.HoaDons
                .Where(hd => hd.MaHopDong == contractId)
                .SumAsync(hd => hd.SoTienThanhToan);

            var remainingAmount = hopDong.TongTien - totalPaid;

            var invoices = await _context.HoaDons
                .Where(hd => hd.MaHopDong == contractId)
                .Select(hd => new {
                    hd.MaHoaDon,
                    hd.NgayLap,
                    hd.SoTienThanhToan,
                    hd.LoaiThanhToan,
                    hd.MaNhanVien,
                    NhanVien = _context.TaiKhoans
                        .Where(tk => tk.MaNhanVien == hd.MaNhanVien)
                        .Select(tk => new {
                            tk.MaNhanVien,
                            tk.HoTen
                        })
                        .FirstOrDefault()
                })
                .OrderByDescending(hd => hd.NgayLap)
                .ToListAsync();

            return Ok(new { 
                success = true, 
                data = new {
                    hopDong.MaHopDong,
                    hopDong.TongTien,
                    hopDong.TienDatCoc,
                    totalPaid,
                    remainingAmount,
                    invoices
                }
            });
        }

        private bool HoaDonExists(int id)
        {
            return _context.HoaDons.Any(e => e.MaHoaDon == id);
        }
    }

    public class CreateHoaDonRequest
    {
        public int MaHopDong { get; set; }
        public int MaNhanVien { get; set; }
        public decimal SoTienThanhToan { get; set; }
        public string LoaiThanhToan { get; set; } = string.Empty;
    }
}
