using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Services;
using UniUnboxdAPI.Utilities;
using UniUnboxdAPI.Models.DataTransferObjects.ReviewPage;

namespace UniUnboxdAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class ReviewController(ReviewService reviewService) : ControllerBase
    {
        /// <summary>
        /// Retrieves a review by its ID.
        /// </summary>
        /// <param name="id">The ID of the review to retrieve.</param>
        /// <returns>An IActionResult containing the review if found, or an error message.</returns>
        [HttpGet]
        [Authorize(Roles = "Student, Professor")]
        public async Task<IActionResult> GetReview([FromQuery(Name = "id")] int id)
        {
            if (!await reviewService.DoesReviewExist(id))
                return BadRequest("Given review does not exist.");

            var review = await reviewService.GetReviewPageModel(id);

            if (review == null)
                return BadRequest("Something went wrong.");

            return Ok(review);
        }

        /// <summary>
        /// Fetches the next set of reviews for a course, starting from a specified review ID.
        /// </summary>
        /// <param name="id">The starting review ID.</param>
        /// <param name="courseId">The ID of the course.</param>
        /// <param name="n">The number of reviews to retrieve.</param>
        /// <returns>An IActionResult containing the list of reviews or an error message.</returns>
        [HttpGet("get-next-reviews")]
        public async Task<IActionResult> GetNextReviewsForCourse([FromQuery(Name = "id")] int id, [FromQuery(Name = "courseId")] int courseId, [FromQuery(Name = "numReviews")] int n)
        {
            var models = await reviewService.GetNextReviewsForCourse(id, courseId, n);

            if (models.IsNullOrEmpty())
                return BadRequest($"No review with id bigger than {id} exists");

            return Ok(models);
        }

        /// <summary>
        /// Retrieves the latest reviews made by friends.
        /// </summary>
        /// <returns>An IActionResult containing the reviews or an error message.</returns>
        [HttpGet("latest-by-friends")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> GetLatestReviewsByFriends()
        {
            var id = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            var reviews = await reviewService.GetLatestReviewsByFriends(id);

            return Ok(reviews);
        }

        /// <summary>
        /// Submits a new review.
        /// </summary>
        /// <param name="model">The review model to post.</param>
        /// <returns>An IActionResult indicating success or failure.</returns>
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

                await reviewService.UpdateAverageRatingAfterPost(review.Course.Id, review.Rating, review.IsAnonymous);

                reviewService.NotifyFollowers(review);

                return Ok("Successfully created review.");
            }
            catch (Exception ex)
            {
                return BadRequest("Something went wrong when creating a review.\nThe following exception was thrown:\n" + ex.Message);
            }
        }

        /// <summary>
        /// Flags a review based on the provided model.
        /// </summary>
        /// <param name="model">The model containing the review ID to flag.</param>
        /// <returns>An IActionResult indicating success or failure.</returns>
        [HttpPost("flag-review")]
        [Authorize(Roles = "Student, Professor")]
        public async Task<IActionResult> FlagReview([FromBody] FlagReviewModel model)
        {
            int userId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            if (!await reviewService.DoesReviewExist(model.ReviewId))
                return BadRequest("Given review does not exist.");

            reviewService.FlagReview(model, userId);
            return Ok("Successfully flagged review");
        }

        /// <summary>
        /// Updates an existing review.
        /// </summary>
        /// <param name="id">The ID of the review to update.</param>
        /// <param name="model">The updated review model.</param>
        /// <returns>An IActionResult indicating success or failure.</returns>
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
                bool oldIsAnon = review.IsAnonymous;

                reviewService.UpdateReview(review, model);

                await reviewService.PutReview(review);

                await reviewService.UpdateAverageRatingAfterPut(review.Course.Id, review.Rating, oldRating, review.IsAnonymous, oldIsAnon);

                return Ok("Successfully updated review.");
            }
            catch (Exception ex)
            {
                return BadRequest("Something went wrong when updating a review.\nThe following exception was thrown:\n" + ex.Message);
            }
        }

        /// <summary>
        /// Likes a specified review.
        /// </summary>
        /// <param name="reviewId">The ID of the review to like.</param>
        /// <returns>An IActionResult indicating success or failure.</returns>
        [HttpPut("like")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> LikeReview([FromQuery(Name = "review")] int reviewId)
        {
            int studentId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            if (!await reviewService.DoesReviewExist(reviewId))
                return BadRequest("Given review does not exist.");

            if (!await reviewService.DoesStudentExist(studentId))
                return BadRequest("Given student does not exist.");

            if (await reviewService.IsReviewWrittenByStudent(reviewId, studentId))
                return BadRequest("You can not like your own review.");

            if (await reviewService.DoesStudentLikeReview(reviewId, studentId))
                return BadRequest("Given review is already liked.");

            await reviewService.LikeReview(reviewId, studentId);

            return Ok();
        }

        /// <summary>
        /// Unlikes a specified review.
        /// </summary>
        /// <param name="reviewId">The ID of the review to unlike.</param>
        /// <returns>An IActionResult indicating success or failure.</returns>
        [HttpPut("unlike")]
        [Authorize(Roles = "Student")]
        public async Task<IActionResult> UnlikeReview([FromQuery(Name = "review")] int reviewId)
        {
            int studentId = JWTValidation.GetUserId(HttpContext.User.Identity as ClaimsIdentity);

            if (!await reviewService.DoesReviewExist(reviewId))
                return BadRequest("Given review does not exist.");

            if (!await reviewService.DoesStudentExist(studentId))
                return BadRequest("Given student does not exist.");

            if (await reviewService.IsReviewWrittenByStudent(reviewId, studentId))
                return BadRequest("You can not unlike your own review.");

            if (!await reviewService.DoesStudentLikeReview(reviewId, studentId))
                return BadRequest("Given review is not liked.");

            await reviewService.UnlikeReview(reviewId, studentId);

            return Ok();
        }

        /// <summary>
        /// Deletes a review based on its ID.
        /// </summary>
        /// <param name="id">The ID of the review to delete.</param>
        /// <returns>An IActionResult indicating success or failure.</returns>
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
                bool oldIsAnon = review.IsAnonymous;
                await reviewService.DeleteReview(review);

                await reviewService.UpdateAverageRatingAfterDelete(review.Course.Id, review.Rating, oldIsAnon);

                return Ok("Successfully deleted review.");
            }
            catch (Exception ex)
            {
                return BadRequest("Something went wrong when deleting a review.\nThe following exception was thrown:\n" + ex.Message);
            }
        }
    }
}
