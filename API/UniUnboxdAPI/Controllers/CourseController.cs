using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Services;
using UniUnboxdAPI.Utilities;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class CourseController(CourseService courseService) : ControllerBase
    {
        [HttpGet]
        [Authorize]
        public async Task<IActionResult> GetCourse([FromQuery(Name = "id")] int id, [FromQuery(Name = "numReviews")] int numOfReviews)
        {
            if (! await courseService.DoesCourseExist(id))
                return BadRequest($"A course with id {id} does not exist.");

            var model = await courseService.GetCourseRetrievalModelById(id, numOfReviews);

            return Ok(model);
        }

        [HttpGet("popular")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> GetPopularCoursesOfLastWeek()
        {
            var courses = await courseService.GetPopularCoursesOfLastWeek();

            return Ok(courses);
        }

        [HttpGet("popular-by-university")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> GetPopularCoursesOfLastWeekByUniversity([FromQuery(Name = "id")] int id)
        {
            var courses = await courseService.GetPopularCoursesOfLastWeekByUniversity(id);

            return Ok(courses);
        }

        [HttpGet("popular-by-friends")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> GetPopularCoursesOfLastWeekByFriends()
        {
            var id = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            var courses = await courseService.GetPopularCoursesOfLastWeekByFriends(id);

            return Ok(courses);
        }

        [HttpGet("last-edited")]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> GetLastEditedCoursesByUniversity()
        {
            var id = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            var courses = await courseService.GetLastEditedCoursesByUniversity(id);

            return Ok(courses);
        }

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
    }
}