using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace UniUnboxdAPI.Utilities
{
    /// <summary>
    /// Tool to create JWT.
    /// </summary>
    public static class JWTConfiguration
    {
        private static string? JWTKey { get; set; }
        private const double JWTExpiringDate = 7;

        /// <summary>
        /// Provide secret key to singular instantiation.
        /// </summary>
        /// <param name="configuration"></param>
        public static void Init(IConfiguration configuration)
        {
            JWTKey = configuration["JWTKey"];
        }

        /// <summary>
        /// Generates JWT based on user information.
        /// </summary>
        /// <param name="user">Provided user model.</param>
        /// <returns>Generated JWT.</returns>
        public static string GenerateJWT(User user)
        {
            var token = new JwtSecurityToken(
                claims: CreateClaims(user),
                expires: DateTime.Now.AddDays(JWTExpiringDate),
                signingCredentials: CreateCredentials()
            );

            return new JwtSecurityTokenHandler().WriteToken(token);
        }

        /// <summary>
        /// Create list of claims for JWT based on user information.
        /// </summary>
        /// <param name="user">Provided user model.</param>
        /// <returns>Generated claims.</returns>
        private static List<Claim> CreateClaims(User user)
            => [
                new Claim(JwtRegisteredClaimNames.Jti, GenerateJTI()),
                new Claim(JwtRegisteredClaimNames.Sub, user.Id.ToString()),
                new Claim(JwtRegisteredClaimNames.Name, user.UserName),
                new Claim(JwtRegisteredClaimNames.Email, user.Email),
                new Claim(JwtRegisteredClaimNames.Typ, user.UserType.ToString()),
                new Claim(ClaimTypes.Role, user.UserType.ToString()),
                new Claim("verified", user.VerificationStatus.ToString()),
                user.UniversityId != 0 ? new Claim("university", user.UniversityId.ToString()) : null
            ];

        /// <summary>
        /// Generate JTI.
        /// </summary>
        /// <returns>Generated JTI.</returns>
        private static string GenerateJTI()
            => Guid.NewGuid().ToString();

        /// <summary>
        /// Create credentials for JWT.
        /// </summary>
        /// <returns>Generated credentials.</returns>
        private static SigningCredentials CreateCredentials()
            => new(new SymmetricSecurityKey(Encoding.UTF8.GetBytes(JWTKey)), SecurityAlgorithms.HmacSha256);
    }
}
