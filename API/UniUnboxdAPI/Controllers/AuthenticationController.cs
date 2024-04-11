using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Services;
using UniUnboxdAPI.Utilities;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthenticationController(AuthenticationService authenticationService) : ControllerBase
    {

        /// <summary>
        /// Authenticates a user based on email and password.
        /// </summary>
        /// <param name="model">The authentication model containing the email and password.</param>
        /// <returns>A JWT token if authentication is successful; otherwise, an error message.</returns>
        [HttpPost]
        [AllowAnonymous]
        public async Task<IActionResult> Authenticate([FromBody] AuthenticationModel model)
        {
            if (!ModelState.IsValid)
                return BadRequest("Not all required fields have been filled in.");

            if (!await authenticationService.DoesEmailExist(model.Email))
                return BadRequest("User not found.");

            var token = await authenticationService.Authenticate(model);

            if (token == null)
                return BadRequest("The email and password do not align.");

            return Ok(new { Token = token });
        }

        /// <summary>
        /// Updates the JWT token for an authenticated user.
        /// </summary>
        /// <returns>A new JWT token if the user exists; otherwise, an error message.</returns>
        [HttpGet("update")]
        [Authorize]
        public async Task<IActionResult> UpdateToken()
        {
            int id = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            var user = await authenticationService.GetUser(id);

            if (user == null)
                return BadRequest("User does not exist.");

            var token = authenticationService.UpdateToken(user);

            return Ok(new { Token = token });
        }
    }
}
