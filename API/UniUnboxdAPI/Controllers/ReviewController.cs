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
            System.Console.WriteLine("I am here!");
            if (ModelState.IsValid)
            {
                if (!reviewService.IsUserValidated(HttpContext.User.Identity as ClaimsIdentity, model.StudentId))
                    return BadRequest("Invalid user.");

                if (!await reviewService.DoesStudentExist(model.StudentId))
                    return BadRequest("Given student does not exist.");

                if (!await reviewService.DoesCourseExist(model.CourseId))
                    return BadRequest("Given course does not exist.");

                if (await reviewService.HasStudentAlreadyReviewedCourse(model.StudentId, model.CourseId))
                    return BadRequest("StudentModel has already reviewed course.");

                Review review = await reviewService.CreateReview(model);

                await reviewService.PostReview(review);

                return Ok("Succesfully created review.");
            }

            return BadRequest("Not all required fields have been filled in.");
        }
    }
}
