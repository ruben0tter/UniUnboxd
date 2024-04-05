using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Services;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [AllowAnonymous]
    public class RegistrationController(RegistrationService registrationService) : ControllerBase
    {
        /// <summary>
        /// Registers a new user with the provided details.
        /// </summary>
        /// <param name="model">The registration details.</param>
        /// <returns>A response indicating the outcome of the registration process.</returns>
        [HttpPost]
        public async Task<IActionResult> Registrate([FromBody] RegisterModel model)
        {
            if (!ModelState.IsValid)
                return BadRequest("Not all required fields have been filled in.");

            if (!registrationService.IsEmailValid(model.Email))
                return BadRequest("A non-valid email was submitted.");

            if (await registrationService.DoesEmailExist(model.Email))
                return BadRequest("An account with the same email already exists.");

            User user = registrationService.CreateUser(model);

            if (await registrationService.CreateAccount(user, model.Password))
                return Ok("Account created successfully.");

            return BadRequest("Failed to create user. Please try again later.");
        }
    }
}