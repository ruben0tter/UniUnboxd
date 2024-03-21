using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using UniUnboxdAPI.Services;

public class SearchOptions {
    public string Search { get; set; } = "";
    public int? Start { get; set; }
    public int? Count { get; set; }
}

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class SearchController(SearchService searchService) : ControllerBase
    {
        [HttpGet]
        [Authorize(Roles = "Student, University")]
        [Route("Course")]
        public async Task<IActionResult> SearchCourses([FromQuery] SearchOptions query)
        {
            Console.WriteLine("\n\n\n\n\nSearching for courses\n\n\n\n\n");
            if (query.Search == "") {
                return BadRequest("Search query cannot be empty");
            }
            var courses = await searchService.GetCourses(query);

            return Ok(courses);
        }

        [HttpGet]
        [Authorize]
        [Route("User")]
        public async Task<IActionResult> SearchUsers([FromQuery] SearchOptions query)
        {
            if (query.Search == "") {
                return BadRequest("Search query cannot be empty");
            }
            var courses = await searchService.GetUsers(query);

            return Ok(courses);
        }
    }
}