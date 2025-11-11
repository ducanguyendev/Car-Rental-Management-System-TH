using Microsoft.EntityFrameworkCore;
using PhamMemThueXe.Data;
using PhamMemThueXe.Services;
using Microsoft.AspNetCore.Authentication.Cookies;

namespace PhamMemThueXe
{
    public class Program
    {
        public static async Task Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            // Add services to the container.
            builder.Services.AddControllersWithViews()
                .AddJsonOptions(options =>
                {
                    options.JsonSerializerOptions.PropertyNamingPolicy = System.Text.Json.JsonNamingPolicy.CamelCase;
                });

            // Add Entity Framework
            builder.Services.AddDbContext<ApplicationDbContext>(options =>
                options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

            // Add Session
            builder.Services.AddDistributedMemoryCache();
            builder.Services.AddSession(options =>
            {
                options.IdleTimeout = TimeSpan.FromMinutes(30);
                options.Cookie.HttpOnly = true;
                options.Cookie.IsEssential = true;
            });

            // Add CORS for mobile app
            // Lưu ý: AllowCredentials() và AllowAnyOrigin() không thể dùng cùng nhau
            // Với mobile app, cookie sẽ được tự động gửi kèm request
            builder.Services.AddCors(options =>
            {
                options.AddPolicy("AllowMobileApp", policy =>
                {
                    policy.AllowAnyOrigin()
                          .AllowAnyMethod()
                          .AllowAnyHeader()
                          .WithExposedHeaders("Set-Cookie");
                });
            });

            // Add Authentication
            builder.Services.AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
                .AddCookie(options =>
                {
                    options.LoginPath = "/Auth/Login";
                    options.LogoutPath = "/Auth/Logout";
                    options.AccessDeniedPath = "/Home/AccessDenied";
                    options.ExpireTimeSpan = TimeSpan.FromDays(7);
                    options.SlidingExpiration = true;
                    // Cookie settings cho web - chỉ set SameSite.None khi HTTPS hoặc trong development
                    var isDevelopment = builder.Environment.IsDevelopment();
                    if (isDevelopment)
                    {
                        // Development: cho phép cookie hoạt động với cả HTTP và HTTPS
                        options.Cookie.SameSite = Microsoft.AspNetCore.Http.SameSiteMode.Lax;
                        options.Cookie.SecurePolicy = Microsoft.AspNetCore.Http.CookieSecurePolicy.None;
                    }
                    else
                    {
                        // Production: SameSite.None yêu cầu Secure=true
                        options.Cookie.SameSite = Microsoft.AspNetCore.Http.SameSiteMode.None;
                        options.Cookie.SecurePolicy = Microsoft.AspNetCore.Http.CookieSecurePolicy.Always;
                    }
                    options.Cookie.HttpOnly = true;
                    options.Cookie.IsEssential = true;
                });

            // Add Authorization
            builder.Services.AddAuthorization();

            // Add Database Initializer
            builder.Services.AddScoped<DatabaseInitializer>();

            var app = builder.Build();

            // Initialize database
            using (var scope = app.Services.CreateScope())
            {
                var initializer = scope.ServiceProvider.GetRequiredService<DatabaseInitializer>();
                await initializer.InitializeAsync();
            }

            // Configure the HTTP request pipeline.
            if (!app.Environment.IsDevelopment())
            {
                app.UseExceptionHandler("/Error");
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                app.UseHsts();
            }

            app.UseHttpsRedirection();
            app.UseStaticFiles();

            app.UseRouting();

            // Enable CORS
            app.UseCors("AllowMobileApp");

            // Enable Session
            app.UseSession();

            app.UseAuthentication();
            app.UseAuthorization();

            // Map API controllers
            app.MapControllers();

            // Map MVC controllers
            app.MapControllerRoute(
                name: "default",
                pattern: "{controller=Home}/{action=Index}/{id?}");

            app.Run();
        }
    }
}
