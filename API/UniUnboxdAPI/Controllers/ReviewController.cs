using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Services;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class ReviewController : ControllerBase
    {
        private readonly ReviewService reviewService;

        public ReviewController(ReviewService reviewService)
        {
            this.reviewService = reviewService;
        }


        [Authorize(Roles = "Student")]
        [HttpPost]
        public async Task<IActionResult> PostReview([FromBody] ReviewModel model)
        {
            if (!ModelState.IsValid)
                return BadRequest("Not all required fields have been filled in.");

            if (!reviewService.IsUserValidated(HttpContext.User.Identity as ClaimsIdentity, model.StudentId))
                return BadRequest("Invalid user.");

            if (!await reviewService.DoesStudentExist(model.StudentId))
                return BadRequest("Given student does not exist.");

            if (!await reviewService.DoesCourseExist(model.CourseId))
                return BadRequest("Given course does not exist.");

            if (await reviewService.HasStudentAlreadyReviewedCourse(model.StudentId, model.CourseId))
                return BadRequest("Student has already reviewed course.");

            try
            {
                Review review = await reviewService.CreateReview(model);

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
