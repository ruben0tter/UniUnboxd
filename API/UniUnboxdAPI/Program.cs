using FirebaseAdmin;
using Google.Apis.Auth.OAuth2;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using Newtonsoft.Json;
using System.Text;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Repositories;
using UniUnboxdAPI.Services;
using UniUnboxdAPI.Utilities;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers().AddNewtonsoftJson(options =>
{
    options.SerializerSettings.ReferenceLoopHandling = ReferenceLoopHandling.Ignore;
});

// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();

// Add swagger,
// to test API
builder.Services.AddSwaggerGen(opt =>
{
    // Swagger version
    opt.SwaggerDoc("v1", new OpenApiInfo { Title = "UniUnboxd", Version = "v1" });
    // Add authorization for Swagger
    opt.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
    {
        In = ParameterLocation.Header,
        Description = "Please enter token",
        Name = "Authorization",
        Type = SecuritySchemeType.Http,
        BearerFormat = "JWT",
        Scheme = "bearer"
    });

    // Add security requirements
    opt.AddSecurityRequirement(new OpenApiSecurityRequirement
    {
        {
            new OpenApiSecurityScheme
            {
                Reference = new OpenApiReference
                {
                    Type=ReferenceType.SecurityScheme,
                    Id="Bearer"
                }
            },
            Array.Empty<string>()
        }
    });
});

// Setup database
/// <summary>
/// Retrieves the connection string from the configuration file.
/// </summary>
var connectionString = builder.Configuration.GetConnectionString("DatabaseConnection");
builder.Services.AddDbContext<UniUnboxdDbContext>(options =>
{
    options.UseMySql(connectionString, ServerVersion.AutoDetect(connectionString));
});

// Add Microsoft Identity
builder.Services.AddIdentity<User, IdentityRole<int>>(options =>
{
    // Set password requirements
    options.Password.RequiredLength = 1;
    options.Password.RequiredUniqueChars = 0;
    options.Password.RequireDigit = false;
    options.Password.RequireNonAlphanumeric = false;
    options.Password.RequireLowercase = false;
    options.Password.RequireUppercase = false;
    options.Lockout.AllowedForNewUsers = false;
})
    .AddEntityFrameworkStores<UniUnboxdDbContext>()
    .AddDefaultTokenProviders();

// JWT setup
builder.Services.AddAuthentication(x => {
    x.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    x.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
}).AddJwtBearer(x => {
    x.RequireHttpsMetadata = false;
    x.SaveToken = true;
    x.TokenValidationParameters = new TokenValidationParameters
    {
        ValidateIssuerSigningKey = true,
        IssuerSigningKey = new SymmetricSecurityKey(Encoding.ASCII.GetBytes(builder.Configuration["JWTKey"])),
        ValidateIssuer = false,
        ValidateAudience = false
    };
});

// JWT Configuration
JWTConfiguration.Init(builder.Configuration);

// Services
// Add Verification Service
builder.Services.AddTransient<VerificationService>();
// Add Registration Service
builder.Services.AddTransient<RegistrationService>();
// Add Authentication Service
builder.Services.AddTransient<AuthenticationService>();
// Add Review Service
builder.Services.AddTransient<ReviewService>();
// Add Course Service
builder.Services.AddTransient<CourseService>();
// Add User Service
builder.Services.AddTransient<UserService>();
// Add Reply Service
builder.Services.AddTransient<ReplyService>();
// Add Search Service
builder.Services.AddTransient<SearchService>();
// Add Search Service
builder.Services.AddTransient<MailService>();
// Add Search Service
builder.Services.AddTransient<PushNotificationService>();

// Repositories
// Add Verification Repository
builder.Services.AddTransient<VerificationRepository>();
// Add Registration Repository
builder.Services.AddTransient<ReviewRepository>();
// Add Course Repository
builder.Services.AddTransient<CourseRepository>();
// Add User Repository
builder.Services.AddTransient<UserRepository>();
// Add Reply Repository
builder.Services.AddTransient<ReplyRepository>();
// Add Search Repository
builder.Services.AddTransient<SearchRepository>();

// Push Notifications Dependency
FirebaseApp.Create(new AppOptions()
{
    Credential = GoogleCredential.FromFile(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "firebasesettings.json")),
});

// Build app
var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

// Allows emulator usage
#if !DEBUG
app.UseHttpsRedirection();
#endif

// Add authentication
app.UseAuthentication();

// Add authorization
app.UseAuthorization();

//Map the Controller routes
app.MapControllers();

// Run the API
app.Run();
