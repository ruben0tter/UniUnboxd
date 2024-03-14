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
    public class ReviewController(ReviewService reviewService) : ControllerBase
    {
        [HttpGet]
        [Authorize(Roles = "Student, Professor")]
        public async Task<IActionResult> GetReview([FromQuery(Name = "id")] int id)
        {
            if (!await reviewService.DoesReviewExist(id))
                return BadRequest("Given review does nto exist.");

            var review = await reviewService.GetReview(id);

            if (review == null)
                return BadRequest("Something went wrong.");

            return Ok(review);
        }


        [HttpPost]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> PostReview([FromBody] ReviewModel model)
        {
            if (!ModelState.IsValid)
                return BadRequest("Not all required fields have been filled in.");

            int studentId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            if (!await reviewService.DoesStudentExist(studentId))
                return BadRequest("Given student does not exist.");

            if (!await reviewService.DoesCourseExist(model.CourseId))
                return BadRequest("Given course does not exist.");

            if (await reviewService.HasStudentAlreadyReviewedCourse(studentId, model.CourseId))
                return BadRequest("Student has already reviewed course.");

            try
            {
                Review review = await reviewService.CreateReview(model, studentId);

                await reviewService.PostReview(review);

                await reviewService.UpdateAverageRating(review.Course.Id, review.Rating);

                return Ok("Succesfully created review.");
            } 
            catch (Exception ex) 
            {
                return BadRequest("Something went wrong when creating a review.\nThe following exception was thrown:\n" + ex.Message);
            }
        }
    }
}
