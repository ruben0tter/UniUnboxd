using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using UniUnboxdAPI.Services;
using UniUnboxdAPI.Utilities;

public class SearchOptions {
    public string Search { get; set; } = "";
    public int? Start { get; set; } = 0;
    public int? Count { get; set; } = 10;
    public int? UniversityId { get; set; } = null;
}

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class SearchController(SearchService searchService) : ControllerBase
    {
        [HttpGet]
        [Authorize]
        [Route("Course")]
        public async Task<IActionResult> SearchCourses([FromQuery] SearchOptions query)
        {
            Console.WriteLine("\n\n\n\n\nSearching for courses\n\n\n\n\n");
            if (query.Search == "") {
                return BadRequest("Search query cannot be empty");
            }

            string role = JWTValidation.GetRole(HttpContext.User.Identity as ClaimsIdentity);

            if (role == "University") {
                int uniUserID = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);
                query.UniversityId = uniUserID;
            }

            var courses = await searchService.GetCourses(query);

            if (courses == null) {
                return BadRequest();
            }

            return Ok(courses);
        }

        [HttpGet]
        [Authorize(Roles = "Student, Professor")]
        [Route("User")]
        public async Task<IActionResult> SearchUsers([FromQuery] SearchOptions query)
        {
            if (query.Search == "") {
                return BadRequest("Search query cannot be empty");
            }
            var users = await searchService.GetUsers(query);

            return Ok(users);
        }
    }
}
