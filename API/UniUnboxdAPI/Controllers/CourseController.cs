using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using UniUnboxdAPI.Models;
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
        
        [HttpGet("assigned-courses")]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> GetAssignedCourses([FromQuery(Name = "professorId")] int professorId)
        {
            if (!await courseService.DoesProfessorExist(professorId))
                return BadRequest("Professor does not exist.");

            var courses = await courseService.GetAssignedCourses(professorId);
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

        [HttpPut]
        [Authorize(Roles = "University, Professor")]
        public async Task<IActionResult> PutCourse([FromBody] CourseEditModel model)
        {
            if (!ModelState.IsValid)
                return BadRequest("Model state is invalid.");
            //TODO: update for professor users.
            int userId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            if (!(await courseService.DoesUniversityExist(userId) || await courseService.DoesProfessorExist(userId)))
                return BadRequest("User does not exist.");

            if (!await courseService.DoesCourseExist(model.Id))
                return BadRequest("Course does not exist.");

            Course course = await courseService.GetCourse(model.Id);
            try
            {
                await courseService.UpdateCourse(course, model);
                await courseService.PutCourse(course);

                return Ok("Succesfully updated course.");
            }
            catch (Exception e)
            {
                return BadRequest("Something went wrong when updating a course.\nThe following exception was thrown:\n" + e.Message);
            }
        }
        
        [HttpDelete]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> DeleteCourse([FromQuery(Name = "id")] int id)
        {
            if (!await courseService.DoesCourseExist(id))
                return BadRequest($"Course with id {id} does not exist.");

            Course course = await courseService.GetCourse(id);
            
            if (!JWTValidation.IsUserValidated(HttpContext.User.Identity as ClaimsIdentity, course.University.Id))
                return BadRequest($"User id did not match id of the university of the course.");

            if (!await courseService.DoesCourseExist(id))
                return BadRequest($"Course does not exist.");
            await courseService.DeleteCourse(course);
            return Ok();
        }
    }
}