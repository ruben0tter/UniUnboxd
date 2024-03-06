using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Services;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [AllowAnonymous]
    public class AuthenticationController : ControllerBase
    {
        private readonly AuthenticationService authenticationService;

        public AuthenticationController(AuthenticationService authenticationService)
        {
            this.authenticationService = authenticationService;
        }

        [HttpPost]
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
    }
}
