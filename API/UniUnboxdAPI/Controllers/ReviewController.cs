using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.IdentityModel.Tokens;
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

            var review = await reviewService.GetReviewPageModel(id);

            if (review == null)
                return BadRequest("Something went wrong.");

            return Ok(review);
        }

        [HttpGet("get-next-reviews")]
        public async Task<IActionResult> GetNextReviewsForCourse([FromQuery(Name = "id")] int id, [FromQuery(Name="courseId")] int courseId, [FromQuery(Name="numReviews")] int n)
        {
            var models = await reviewService.GetNextReviewsForCourse(id, courseId, n);
            
            if (models.IsNullOrEmpty()) 
                return BadRequest($"No review with id bigger than {id} exists");
            
            return Ok(models);
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
                return BadRequest("Student has already reviewed provided course.");

            try
            {
                Review review = await reviewService.CreateReview(model, studentId);

                await reviewService.PostReview(review);

                await reviewService.UpdateAverageRatingAfterPost(review.Course.Id, review.Rating);

                return Ok("Succesfully created review.");
            } 
            catch (Exception ex) 
            {
                return BadRequest("Something went wrong when creating a review.\nThe following exception was thrown:\n" + ex.Message);
            }
        }

        [HttpPut]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> PutReview([FromQuery(Name = "id")] int id, [FromBody] ReviewModel model)
        {
            if (!ModelState.IsValid)
                return BadRequest("Not all required fields have been filled in.");

            int studentId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            if (!await reviewService.DoesStudentExist(studentId))
                return BadRequest("Given student does not exist.");

            if (!await reviewService.DoesCourseExist(model.CourseId))
                return BadRequest("Given course does not exist.");

            Review? review = await reviewService.GetReview(id);

            if (review == null)
                return BadRequest("No review exists with provided id.");

            if (studentId != review.Student.Id)
                return BadRequest("Review was not written by user.");

            try
            {
                double oldRating = review.Rating;

                reviewService.UpdateReview(review, model);

                await reviewService.PutReview(review);

                await reviewService.UpdateAverageRatingAfterPut(review.Course.Id, review.Rating, oldRating);

                return Ok("Succesfully updated review.");
            }
            catch (Exception ex)
            {
                return BadRequest("Something went wrong when updating a review.\nThe following exception was thrown:\n" + ex.Message);
            }
        }

        [HttpDelete]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> DeleteReview([FromQuery(Name = "id")] int id)
        {
            int studentId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            if (!await reviewService.DoesStudentExist(studentId))
                return BadRequest("Given student does not exist.");

            Review? review = await reviewService.GetReview(id);

            if (review == null)
                return BadRequest("No review exists with provided id.");

            if (studentId != review.Student.Id)
                return BadRequest("Review was not written by user.");

            try
            {
                await reviewService.DeleteReview(review);

                await reviewService.UpdateAverageRatingAfterDelete(review.Course.Id, review.Rating);

                return Ok("Succesfully deleted review.");
            }
            catch (Exception ex)
            {
                return BadRequest("Something went wrong when deleting a review.\nThe following exception was thrown:\n" + ex.Message);
            }
        }
    }
}
