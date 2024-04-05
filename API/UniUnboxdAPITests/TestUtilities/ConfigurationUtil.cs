using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using System.Security.Claims;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPITests.TestUtilities
{
    internal class ConfigurationUtil
    {
        public static IConfiguration CreateConfiguration()
        {
            var config = new List<KeyValuePair<string, string?>>
            {
                new("JWTKey", Guid.NewGuid().ToString())
            };

            return new ConfigurationBuilder()
                .AddInMemoryCollection(config)
                .Build();
        }

        public static void SetHttpContext(ControllerBase controller, int id, UserType role)
        {
            var claims = new List<Claim>
            {
                new(ClaimTypes.NameIdentifier, id.ToString()),
                new(ClaimTypes.Role, role.ToString())
            };
            var identity = new ClaimsIdentity(claims);
            var claimsPrincipal = new ClaimsPrincipal(identity);

            controller.ControllerContext = new ControllerContext { HttpContext = new DefaultHttpContext { User = claimsPrincipal } };
        }
    }
}
