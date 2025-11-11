using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PhamMemThueXe.Data;
using PhamMemThueXe.Models;

namespace PhamMemThueXe.Controllers
{
    [Authorize(Roles = "Admin")]
    public class AdminController : Controller
    {
        private readonly ApplicationDbContext _context;

        public AdminController(ApplicationDbContext context)
        {
            _context = context;
        }

        public IActionResult Index()
        {
            return View();
        }

        // ========== QUẢN LÝ TÀI KHOẢN KẾ TOÁN ==========
        // View này sử dụng API, không cần load data trước
        public IActionResult Accountants()
        {
            return View();
        }

        // ========== QUẢN LÝ LOẠI XE ==========
        // View này sử dụng API, không cần load data trước
        public IActionResult LoaiXes()
        {
            return View();
        }

        // ========== QUẢN LÝ XE ==========
        // View này sử dụng API, không cần load data trước
        public IActionResult Xes()
        {
            return View();
        }

        // ========== QUẢN LÝ BẢNG GIÁ ==========
        // View này sử dụng API, không cần load data trước
        public IActionResult BangGias()
        {
            return View();
        }

        // ========== THỐNG KÊ ==========
        // View này sử dụng API, không cần load data trước
        public IActionResult Statistics()
        {
            return View();
        }
    }
}
