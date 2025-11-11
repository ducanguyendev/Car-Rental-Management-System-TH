using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PhamMemThueXe.Data;

namespace PhamMemThueXe.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class SystemApiController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public SystemApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/SystemApi/info
        [HttpGet("info")]
        public IActionResult GetSystemInfo()
        {
            var systemInfo = new
            {
                name = "Phần mềm quản lý thuê xe",
                version = "1.0.0",
                description = "Hệ thống quản lý cho thuê xe chuyên nghiệp",
                features = new[]
                {
                    "Quản lý xe và loại xe",
                    "Đặt xe và tạo hợp đồng thuê",
                    "Quản lý khách hàng",
                    "Tạo hóa đơn và theo dõi thanh toán",
                    "Báo cáo thống kê"
                },
                userRoles = new[]
                {
                    new { role = "Admin", description = "Quản lý hệ thống và tài khoản" },
                    new { role = "Ketoan", description = "Xử lý nghiệp vụ thuê xe" },
                    new { role = "Customer", description = "Đặt xe và theo dõi hợp đồng" }
                }
            };

            return Ok(new { success = true, data = systemInfo });
        }

        // GET: api/SystemApi/health
        [HttpGet("health")]
        public async Task<IActionResult> GetSystemHealth()
        {
            try
            {
                // Kiểm tra kết nối database
                await _context.Database.CanConnectAsync();
                
                var healthInfo = new
                {
                    status = "healthy",
                    database = "connected",
                    timestamp = DateTime.Now,
                    uptime = Environment.TickCount64
                };

                return Ok(new { success = true, data = healthInfo });
            }
            catch (Exception ex)
            {
                var healthInfo = new
                {
                    status = "unhealthy",
                    database = "disconnected",
                    timestamp = DateTime.Now,
                    error = ex.Message
                };

                return StatusCode(500, new { success = false, data = healthInfo });
            }
        }

        // GET: api/SystemApi/stats
        [HttpGet("stats")]
        public async Task<IActionResult> GetSystemStats()
        {
            try
            {
                var stats = new
                {
                    totalCustomers = await _context.KhachHangs.CountAsync(),
                    totalCars = await _context.Xes.CountAsync(),
                    totalCarTypes = await _context.LoaiXes.CountAsync(),
                    totalContracts = await _context.HopDongs.CountAsync(),
                    totalInvoices = await _context.HoaDons.CountAsync(),
                    totalBookings = await _context.PhieuDatXes.CountAsync(),
                    totalUsers = await _context.TaiKhoans.CountAsync(),
                    totalRevenue = await _context.HopDongs.SumAsync(hd => hd.TongTien)
                };

                return Ok(new { success = true, data = stats });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { success = false, message = "Có lỗi xảy ra khi lấy thống kê hệ thống." });
            }
        }
    }
}
