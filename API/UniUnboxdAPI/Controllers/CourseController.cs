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
    public class CourseController : ControllerBase
    {
        private readonly CourseService courseService;

        public CourseController(CourseService courseService)
        {
            this.courseService = courseService;
        }

        [Authorize(Roles = "University")]
        [HttpPost]
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

        [Authorize]
        [HttpGet("GetCourse/{id:int}")]
        public async Task<IActionResult> GetCourse([FromRoute] int id)
        {
            var model = await courseService.GetCourseRetrievalModelById(id);
            return Ok(model);
        }

        [Authorize]
        [HttpGet("getTenCoursesFromId/{id:int}")]

    public async Task<IActionResult> GetTenCoursesFromId([FromRoute] int id)
        {
            var model = await courseService.GetTenCoursesFromId(id);
            return Ok(model);
        }
    }
}