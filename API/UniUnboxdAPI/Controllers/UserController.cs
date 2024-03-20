using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Services;
using UniUnboxdAPI.Utilities;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class UserController(UserService userService) : ControllerBase
    {
        [HttpGet]
        [Authorize]
        public async Task<IActionResult> GetMyUser()
        {
            int id = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);
    
            StudentProfileModel student = await userService.GetStudentAndConnectedData(id);
            
            return Ok(student);
        }
        
    }
}