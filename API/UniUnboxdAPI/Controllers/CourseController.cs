using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Services;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class CourseController(CourseService courseService) : ControllerBase
    {
        [HttpPost]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> PostCourse([FromBody] CourseCreationModel creationModel)
        {
            if (!ModelState.IsValid)
                return BadRequest("Model state is invalid.");

            if (!courseService.IsUserValidated(HttpContext.User.Identity as ClaimsIdentity, creationModel.UniversityId))
                return BadRequest("Invalid user.");

            if (!await courseService.DoesUniversityExist(creationModel.UniversityId))
                return BadRequest("University does not exist.");

            var course = await courseService.CreateCourse(creationModel);

            await courseService.PostCourse(course);

            return Ok("Course was created successfully.");
        }

        [HttpGet]
        [Authorize]
        public async Task<IActionResult> GetCourse([FromQuery(Name = "id")] int id)
        {
            if (! await courseService.DoesCourseExist(id)) return BadRequest($"A course with id {id} does not exist.");
            var model = await courseService.GetCourseRetrievalModelById(id);

            return Ok(model);
        }
    }
}