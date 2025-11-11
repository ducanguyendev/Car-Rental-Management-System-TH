using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace PhamMemThueXe.Migrations
{
    /// <inheritdoc />
    public partial class AddImageURLToXe : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            // COMMENT TẤT CẢ CÁC CreateTable VÌ CÁC BẢNG ĐÃ TỒN TẠI
            /*
            migrationBuilder.CreateTable(
                name: "KhachHangs",
                columns: table => new
                {
                    MaKH = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    HoTen = table.Column<string>(type: "nvarchar(100)", maxLength: 100, nullable: false),
                    SoCMND = table.Column<string>(type: "nvarchar(20)", maxLength: 20, nullable: false),
                    SoHoKhau = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: false),
                    SoDienThoai = table.Column<string>(type: "nvarchar(15)", maxLength: 15, nullable: false),
                    DiaChiCoQuan = table.Column<string>(type: "nvarchar(max)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_KhachHangs", x => x.MaKH);
                });

            migrationBuilder.CreateTable(
                name: "LoaiXes",
                columns: table => new
                {
                    MaLoaiXe = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    TenLoaiXe = table.Column<string>(type: "nvarchar(100)", maxLength: 100, nullable: false),
                    MoTa = table.Column<string>(type: "nvarchar(max)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_LoaiXes", x => x.MaLoaiXe);
                });

            migrationBuilder.CreateTable(
                name: "TaiKhoans",
                columns: table => new
                {
                    MaNhanVien = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    TenDangNhap = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: false),
                    MatKhauHash = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    HoTen = table.Column<string>(type: "nvarchar(100)", maxLength: 100, nullable: false),
                    Quyen = table.Column<string>(type: "nvarchar(max)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_TaiKhoans", x => x.MaNhanVien);
                });

            migrationBuilder.CreateTable(
                name: "BangGias",
                columns: table => new
                {
                    MaBangGia = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    MaLoaiXe = table.Column<int>(type: "int", nullable: false),
                    DonGiaTheoNgay = table.Column<decimal>(type: "decimal(18,2)", nullable: false),
                    NgayApDung = table.Column<DateTime>(type: "datetime2", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_BangGias", x => x.MaBangGia);
                    table.ForeignKey(
                        name: "FK_BangGias_LoaiXes_MaLoaiXe",
                        column: x => x.MaLoaiXe,
                        principalTable: "LoaiXes",
                        principalColumn: "MaLoaiXe",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "PhieuDatXes",
                columns: table => new
                {
                    MaDatXe = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    MaKH = table.Column<int>(type: "int", nullable: false),
                    MaLoaiXe = table.Column<int>(type: "int", nullable: false),
                    HoTenKH = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    TenLoaiXeDat = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    NgayDat = table.Column<DateTime>(type: "datetime2", nullable: false),
                    NgayDuKienNhan = table.Column<DateTime>(type: "datetime2", nullable: false),
                    TrangThai = table.Column<string>(type: "nvarchar(max)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_PhieuDatXes", x => x.MaDatXe);
                    table.ForeignKey(
                        name: "FK_PhieuDatXes_KhachHangs_MaKH",
                        column: x => x.MaKH,
                        principalTable: "KhachHangs",
                        principalColumn: "MaKH",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_PhieuDatXes_LoaiXes_MaLoaiXe",
                        column: x => x.MaLoaiXe,
                        principalTable: "LoaiXes",
                        principalColumn: "MaLoaiXe",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "Xes",
                columns: table => new
                {
                    MaXe = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    MaLoaiXe = table.Column<int>(type: "int", nullable: false),
                    BienSoXe = table.Column<string>(type: "nvarchar(15)", maxLength: 15, nullable: false),
                    TenXe = table.Column<string>(type: "nvarchar(100)", maxLength: 100, nullable: false),
                    TrangThai = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    ImageURL = table.Column<string>(type: "nvarchar(500)", maxLength: 500, nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Xes", x => x.MaXe);
                    table.ForeignKey(
                        name: "FK_Xes_LoaiXes_MaLoaiXe",
                        column: x => x.MaLoaiXe,
                        principalTable: "LoaiXes",
                        principalColumn: "MaLoaiXe",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "HopDongs",
                columns: table => new
                {
                    MaHopDong = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    MaKH = table.Column<int>(type: "int", nullable: false),
                    MaNhanVien = table.Column<int>(type: "int", nullable: false),
                    MaDatXe = table.Column<int>(type: "int", nullable: true),
                    HoTenKH = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    SoDienThoaiKH = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    HoTenNVLap = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    NgayLap = table.Column<DateTime>(type: "datetime2", nullable: false),
                    NgayNhanXe = table.Column<DateTime>(type: "datetime2", nullable: false),
                    DiaDiemNhan = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: false),
                    NgayTraXe = table.Column<DateTime>(type: "datetime2", nullable: false),
                    DiaDiemTra = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: false),
                    TongTien = table.Column<decimal>(type: "decimal(18,2)", nullable: false),
                    TienDatCoc = table.Column<decimal>(type: "decimal(18,2)", nullable: false),
                    TrangThai = table.Column<string>(type: "nvarchar(max)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_HopDongs", x => x.MaHopDong);
                    table.ForeignKey(
                        name: "FK_HopDongs_KhachHangs_MaKH",
                        column: x => x.MaKH,
                        principalTable: "KhachHangs",
                        principalColumn: "MaKH",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_HopDongs_PhieuDatXes_MaDatXe",
                        column: x => x.MaDatXe,
                        principalTable: "PhieuDatXes",
                        principalColumn: "MaDatXe",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_HopDongs_TaiKhoans_MaNhanVien",
                        column: x => x.MaNhanVien,
                        principalTable: "TaiKhoans",
                        principalColumn: "MaNhanVien",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "ChiTietHopDongs",
                columns: table => new
                {
                    MaHopDong = table.Column<int>(type: "int", nullable: false),
                    MaXe = table.Column<int>(type: "int", nullable: false),
                    BienSoXe = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    TenXe = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    DonGiaTaiThoiDiemThue = table.Column<decimal>(type: "decimal(18,2)", nullable: false),
                    GhiChu = table.Column<string>(type: "nvarchar(max)", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_ChiTietHopDongs", x => new { x.MaHopDong, x.MaXe });
                    table.ForeignKey(
                        name: "FK_ChiTietHopDongs_HopDongs_MaHopDong",
                        column: x => x.MaHopDong,
                        principalTable: "HopDongs",
                        principalColumn: "MaHopDong",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_ChiTietHopDongs_Xes_MaXe",
                        column: x => x.MaXe,
                        principalTable: "Xes",
                        principalColumn: "MaXe",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateTable(
                name: "HoaDons",
                columns: table => new
                {
                    MaHoaDon = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    MaHopDong = table.Column<int>(type: "int", nullable: false),
                    MaNhanVien = table.Column<int>(type: "int", nullable: false),
                    HoTenNVLap = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    NgayLap = table.Column<DateTime>(type: "datetime2", nullable: false),
                    SoTienThanhToan = table.Column<decimal>(type: "decimal(18,2)", nullable: false),
                    LoaiThanhToan = table.Column<string>(type: "nvarchar(100)", maxLength: 100, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_HoaDons", x => x.MaHoaDon);
                    table.ForeignKey(
                        name: "FK_HoaDons_HopDongs_MaHopDong",
                        column: x => x.MaHopDong,
                        principalTable: "HopDongs",
                        principalColumn: "MaHopDong",
                        onDelete: ReferentialAction.Restrict);
                    table.ForeignKey(
                        name: "FK_HoaDons_TaiKhoans_MaNhanVien",
                        column: x => x.MaNhanVien,
                        principalTable: "TaiKhoans",
                        principalColumn: "MaNhanVien",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.InsertData(
                table: "LoaiXes",
                columns: new[] { "MaLoaiXe", "MoTa", "TenLoaiXe" },
                values: new object[,]
                {
                    { 1, "Xe sedan, hatchback 4 chỗ ngồi", "Xe 4 chỗ" },
                    { 2, "Xe SUV, crossover 7 chỗ ngồi", "Xe 7 chỗ" },
                    { 3, "Xe minibus 16 chỗ ngồi", "Xe 16 chỗ" }
                });

            migrationBuilder.InsertData(
                table: "TaiKhoans",
                columns: new[] { "MaNhanVien", "HoTen", "MatKhauHash", "Quyen", "TenDangNhap" },
                values: new object[] { 1, "Quản trị viên hệ thống", "$2a$11$IDk4pG8Tsk2zJvfc5wDUHOsdYNSsK4N7zCgRoLnGs2H4GfOW2FEu2", "Admin", "admin" });

            migrationBuilder.InsertData(
                table: "BangGias",
                columns: new[] { "MaBangGia", "DonGiaTheoNgay", "MaLoaiXe", "NgayApDung" },
                values: new object[,]
                {
                    { 1, 500000m, 1, new DateTime(2025, 10, 31, 0, 0, 0, 0, DateTimeKind.Local) },
                    { 2, 800000m, 2, new DateTime(2025, 10, 31, 0, 0, 0, 0, DateTimeKind.Local) },
                    { 3, 1200000m, 3, new DateTime(2025, 10, 31, 0, 0, 0, 0, DateTimeKind.Local) }
                });

            migrationBuilder.CreateIndex(
                name: "IX_BangGias_MaLoaiXe",
                table: "BangGias",
                column: "MaLoaiXe");

            migrationBuilder.CreateIndex(
                name: "IX_ChiTietHopDongs_MaXe",
                table: "ChiTietHopDongs",
                column: "MaXe");

            migrationBuilder.CreateIndex(
                name: "IX_HoaDons_MaHopDong",
                table: "HoaDons",
                column: "MaHopDong");

            migrationBuilder.CreateIndex(
                name: "IX_HoaDons_MaNhanVien",
                table: "HoaDons",
                column: "MaNhanVien");

            migrationBuilder.CreateIndex(
                name: "IX_HopDongs_MaDatXe",
                table: "HopDongs",
                column: "MaDatXe");

            migrationBuilder.CreateIndex(
                name: "IX_HopDongs_MaKH",
                table: "HopDongs",
                column: "MaKH");

            migrationBuilder.CreateIndex(
                name: "IX_HopDongs_MaNhanVien",
                table: "HopDongs",
                column: "MaNhanVien");

            migrationBuilder.CreateIndex(
                name: "IX_KhachHangs_SoCMND",
                table: "KhachHangs",
                column: "SoCMND",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_PhieuDatXes_MaKH",
                table: "PhieuDatXes",
                column: "MaKH");

            migrationBuilder.CreateIndex(
                name: "IX_PhieuDatXes_MaLoaiXe",
                table: "PhieuDatXes",
                column: "MaLoaiXe");

            migrationBuilder.CreateIndex(
                name: "IX_TaiKhoans_TenDangNhap",
                table: "TaiKhoans",
                column: "TenDangNhap",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_Xes_BienSoXe",
                table: "Xes",
                column: "BienSoXe",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_Xes_MaLoaiXe",
                table: "Xes",
                column: "MaLoaiXe");
            */

            // CHỈ THÊM CỘT ImageURL VÀO BẢNG Xes (đã tồn tại)
            // Kiểm tra xem cột đã tồn tại chưa, nếu chưa thì thêm
            migrationBuilder.Sql(@"
                IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[Xes]') AND name = 'ImageURL')
                BEGIN
                    ALTER TABLE [Xes] ADD [ImageURL] NVARCHAR(MAX) NULL
                END
            ");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            // COMMENT TẤT CẢ CÁC DropTable VÌ KHÔNG MUỐN XÓA CÁC BẢNG
            /*
            migrationBuilder.DropTable(
                name: "BangGias");

            migrationBuilder.DropTable(
                name: "ChiTietHopDongs");

            migrationBuilder.DropTable(
                name: "HoaDons");

            migrationBuilder.DropTable(
                name: "Xes");

            migrationBuilder.DropTable(
                name: "HopDongs");

            migrationBuilder.DropTable(
                name: "PhieuDatXes");

            migrationBuilder.DropTable(
                name: "TaiKhoans");

            migrationBuilder.DropTable(
                name: "KhachHangs");

            migrationBuilder.DropTable(
                name: "LoaiXes");
            */

            // CHỈ XÓA CỘT ImageURL NẾU CẦN ROLLBACK
            migrationBuilder.Sql(@"
                IF EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[Xes]') AND name = 'ImageURL')
                BEGIN
                    ALTER TABLE [Xes] DROP COLUMN [ImageURL]
                END
            ");
        }
    }
}
