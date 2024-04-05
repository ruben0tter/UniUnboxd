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
        /// <summary>
        /// Searches for courses based on the provided search options.
        /// </summary>
        /// <param name="query">Search options including keywords and filters.</param>
        /// <returns>A list of courses that match the search criteria.</returns>
        [HttpGet]
        [Authorize]
        [Route("Course")]
        public async Task<IActionResult> SearchCourses([FromQuery] SearchOptions query)
        {
            if (query.Search == string.Empty)
                return BadRequest("Search query cannot be empty");

            string role = JWTValidation.GetRole(HttpContext.User.Identity as ClaimsIdentity);

            if (role == "University") {
                int uniUserID = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);
                query.UniversityId = uniUserID;
            }

            var courses = await searchService.GetCourses(query);

            return Ok(courses);
        }

        /// <summary>
        /// Searches for users based on the provided search options. Only accessible by users with Student or Professor roles.
        /// </summary>
        /// <param name="query">Search options including keywords and filters.</param>
        /// <returns>A list of users that match the search criteria.</returns>
        [HttpGet]
        [Authorize(Roles = "Student, Professor")]
        [Route("User")]
        public async Task<IActionResult> SearchUsers([FromQuery] SearchOptions query)
        {
            if (query.Search == string.Empty)
                return BadRequest("Search query cannot be empty");

            var users = await searchService.GetUsers(query);

            return Ok(users);
        }
    }
}
