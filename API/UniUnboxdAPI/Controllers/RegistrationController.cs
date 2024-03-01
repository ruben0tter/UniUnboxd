using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Services;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [AllowAnonymous]
    public class RegistrationController : ControllerBase
    {
        private readonly RegistrationService registrationService;

        public RegistrationController(RegistrationService registrationService)
        {
            this.registrationService = registrationService;
        }

        [HttpPost]
        public async Task<IActionResult> Registrate([FromBody] RegisterModel model)
        {
            if (ModelState.IsValid)
            {
                if (!registrationService.IsEmailValid(model.Email))
                    return BadRequest("A non-valid email was submitted.");

                User user = registrationService.CreateUser(model);

                if (await registrationService.DoesEmailExist(user.Email))
                    return BadRequest("An account with the same email already exists.");

                if(await registrationService.CreateAccount(user, model.Password))
                    return Ok("Account created succesfully.");

                return BadRequest("Failed to create user. Please try again later.");
            }

            return BadRequest("Not all required fields have been filled in.");
        }
    }
}
