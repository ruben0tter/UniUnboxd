using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
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
        /// <summary>
        /// Retrieves a specific course by its ID, including a limited number of reviews based on the user's role.
        /// </summary>
        /// <param name="courseId">The ID of the course to retrieve.</param>
        /// <param name="numOfReviews">The number of reviews to include in the response.</param>
        /// <returns>An IActionResult containing either the course details or an error message.</returns>
        [HttpGet]
        [Authorize]
        public async Task<IActionResult> GetCourse([FromQuery(Name = "id")] int courseId, [FromQuery(Name = "numReviews")] int numOfReviews)
        {
            string role = JWTValidation.GetRole(HttpContext.User.Identity as ClaimsIdentity);

            if (! await courseService.DoesCourseExist(courseId))
                return BadRequest($"A course with id {courseId} does not exist.");

            CourseRetrievalModel model = await courseService.GetCourseRetrievalModelById(courseId);
            
            if (role == "Student")
            {
                int userId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);
                model.FriendReviews = await courseService.GetAllFriendsThatReviewed(userId, courseId);
                model.YourReview = await courseService.GetCourseReviewByStudent(courseId, userId);
            }

            return Ok(model);
        }

        /// <summary>
        /// Retrieves a list of popular courses over the last week.
        /// </summary>
        /// <returns>An IActionResult containing a list of popular courses.</returns>
        [HttpGet("popular")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> GetPopularCoursesOfLastWeek()
        {
            var courses = await courseService.GetPopularCoursesOfLastWeek();

            return Ok(courses);
        }

        /// <summary>
        /// Retrieves a list of popular courses over the last week filtered by a specific university.
        /// </summary>
        /// <param name="id">The university ID for which to retrieve popular courses.</param>
        /// <returns>An IActionResult containing a list of popular courses for the specified university.</returns>
        [HttpGet("popular-by-university")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> GetPopularCoursesOfLastWeekByUniversity([FromQuery(Name = "id")] int id)
        {
            var courses = await courseService.GetPopularCoursesOfLastWeekByUniversity(id);

            return Ok(courses);
        }

        /// <summary>
        /// Retrieves a list of courses assigned to a specific professor.
        /// </summary>
        /// <param name="professorId">The ID of the professor whose courses to retrieve.</param>
        /// <returns>An IActionResult containing a list of assigned courses or an error message.</returns>
        [HttpGet("assigned-courses")]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> GetAssignedCourses([FromQuery(Name = "professorId")] int professorId)
        {
            if (!await courseService.DoesProfessorExist(professorId))
                return BadRequest("Professor does not exist.");

            var courses = await courseService.GetAssignedCourses(professorId);
            return Ok(courses);
        }

        /// <summary>
        /// Retrieves a list of popular courses among the user's friends over the last week.
        /// </summary>
        /// <returns>An IActionResult containing a list of courses or an error message.</returns>
        [HttpGet("popular-by-friends")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> GetPopularCoursesOfLastWeekByFriends()
        {
            var id = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            var courses = await courseService.GetPopularCoursesOfLastWeekByFriends(id);

            return Ok(courses);
        }

        /// <summary>
        /// Retrieves a list of the last edited courses by the university associated with the current user.
        /// </summary>
        /// <returns>An IActionResult containing a list of last edited courses.</returns>
        [HttpGet("last-edited")]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> GetLastEditedCoursesByUniversity()
        {
            var id = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            var courses = await courseService.GetLastEditedCoursesByUniversity(id);

            return Ok(courses);
        }

        /// <summary>
        /// Creates a new course with the provided details.
        /// </summary>
        /// <param name="creationModel">The course creation model containing the details of the new course.</param>
        /// <returns>An IActionResult indicating success or failure.</returns>
        /// <remarks>
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

            foreach (var x in creationModel.AssignedProfessors)
            {
                if (await courseService.DoesProfessorAssignmentExist(course.Id, x.Id))
                    continue;
                var result = await AssignProfessor(x.Id, course.Id);
                if (result.Equals(BadRequest()))
                    return BadRequest("Could not make professor assignment.");
            }
            return Ok("Course was created successfully.");
        }

        /// <summary>
        /// Updates an existing course with new information.
        /// </summary>
        /// <param name="model">The course edit model containing the updated course details.</param>
        /// <returns>An IActionResult indicating success or failure.</returns>
        [HttpPut]
        [Authorize(Roles = "University, Professor")]
        public async Task<IActionResult> PutCourse([FromBody] CourseEditModel model)
        {
            if (!ModelState.IsValid)
                return BadRequest("Model state is invalid.");
            
            int userId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            if (!(await courseService.DoesUniversityExist(userId) || await courseService.DoesProfessorExist(userId)))
                return BadRequest("User does not exist.");

            if (!await courseService.DoesCourseExist(model.Id))
                return BadRequest("Course does not exist.");

            Course course = await courseService.GetCourse(model.Id);
            try
            {
                if(course.AssignedProfessors != null)
                    foreach (var x in course.AssignedProfessors)
                    {
                        if (model.AssignedProfessors.All(i => i.Id != x.ProfessorId))
                            await DismissProfessor(x.ProfessorId, x.CourseId);
                    }
                
                courseService.UpdateCourse(course, model);
                await courseService.PutCourse(course);
                foreach (var x in model.AssignedProfessors)
                {
                    if (!await courseService.DoesProfessorAssignmentExist(course.Id, x.Id))
                        await AssignProfessor(x.Id, course.Id);
                }
                
                return Ok("Successfully updated course.");
            }
            catch (Exception e)
            {
                return BadRequest("Something went wrong when updating a course.\nThe following exception was thrown:\n" + e.Message);
            }
        }

        /// <summary>
        /// Assigns a professor to a course.
        /// </summary>
        /// <param name="professorId">The ID of the professor to assign.</param>
        /// <param name="courseId">The ID of the course to which the professor will be assigned.</param>
        /// <returns>An IActionResult indicating success or failure.</returns>
        [HttpPut("assign-professor")]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> AssignProfessor([FromQuery(Name = "professor")] int professorId, [FromQuery(Name="course")] int courseId)
        {
            
            if (!await courseService.DoesCourseExist(courseId))
                return BadRequest("Course does not exist.");

            Course course = await courseService.GetCourse(courseId);
            
            if (!await courseService.DoesProfessorExist(professorId))
                return BadRequest("Professor does not exist.");
        
            Professor professor = await courseService.GetProfessor(professorId);

            if (await courseService.DoesProfessorAssignmentExist(courseId, professorId))
                return BadRequest("Professor is already assigned to this course.");
            
            await courseService.AssignProfessor(professor, course);

            return Ok();
        }

        /// <summary>
        /// Dismisses a professor from a course.
        /// </summary>
        /// <param name="professorId">The ID of the professor to dismiss.</param>
        /// <param name="courseId">The ID of the course from which the professor will be dismissed.</param>
        /// <returns>An IActionResult indicating success or failure.</returns>
        [HttpPut("dismiss-professor")]
        [Authorize(Roles = "University")]
        public async Task<IActionResult> DismissProfessor([FromQuery(Name = "professor")] int professorId, [FromQuery(Name="course")] int courseId)
        {
            
            if (!await courseService.DoesCourseExist(courseId))
                return BadRequest("Course does not exist.");

            Course course = await courseService.GetCourse(courseId);
            
            if (!await courseService.DoesProfessorExist(professorId))
                return BadRequest("Professor does not exist.");
        
            Professor professor = await courseService.GetProfessor(professorId);

            if (!await courseService.DoesProfessorAssignmentExist(courseId, professorId))
                return BadRequest("Professor is not assigned to this course.");
            
            await courseService.DismissProfessor(professor, course);

            return Ok();
        }

        /// <summary>
        /// Deletes a course by its ID.
        /// </summary>
        /// <param name="id">The ID of the course to delete.</param>
        /// <returns>An IActionResult indicating success or failure.</returns>
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