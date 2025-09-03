using Ocelot.DependencyInjection;
using Ocelot.Middleware;

var builder = WebApplication.CreateBuilder(args);

// Thêm file cấu hình ocelot.json
builder.Configuration.AddJsonFile("ocelot.json", optional: false, reloadOnChange: true);

// Đăng ký dịch vụ Ocelot
builder.Services.AddOcelot();

var app = builder.Build();

app.UseRouting();

// Dùng Ocelot làm gateway
app.UseOcelot().Wait();

app.Run();
