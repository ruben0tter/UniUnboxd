using System.Security.Claims;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;

namespace UniUnboxdAPI.Services
{
    /// <summary>
    /// Service for the ReviewController.
    /// Handles the review logic.
    /// </summary>
    public class ReviewService
    {
        private readonly ReviewRepository reviewRepository;
        private readonly CourseRepository courseRepository;
        private readonly UserRepository userRepository;

        public ReviewService(ReviewRepository reviewRepository, CourseRepository courseRepository, UserRepository userRepository)
        {
            this.reviewRepository = reviewRepository;
            this.courseRepository = courseRepository;
            this.userRepository = userRepository;
        }

        /// <summary>
        /// Checks whether the id contained in the JWT ligns up with the provided id.
        /// </summary>
        /// <param name="identity">Claims contained in the JWT</param>
        /// <param name="id">Provided id</param>
        /// <returns>Whether the id contained in the JWT is equal to the provided id.</returns>
        public bool IsUserValidated(ClaimsIdentity? identity, int id)
        {
            if (identity == null) return false;

            var claim = identity.FindFirst(ClaimTypes.NameIdentifier);

            if (claim == null) return false;

            var userId = claim.Value;

            return userId.Equals(id.ToString());
        }

        /// <summary>
        /// Check whether there exists a student with the provided id.
        /// </summary>
        /// <param name="studentId">Provided student id.</param>
        /// <returns>Whether there exists a student with the provided id.</returns>
        public async Task<bool> DoesStudentExist(int studentId)
            => await userRepository.DoesStudentExist(studentId);

        
        //TODO: Change the implementations to use the method in the course service.
        /// <summary>
        /// Check whether there exists a course with the provided id.
        /// </summary>
        /// <param name="courseId">Provided course id.</param>
        /// <returns>Whether there exists a course with the provided id.</returns>
        public async Task<bool> DoesCourseExist(int courseId) 
            => await courseRepository.DoesCourseExist(courseId);

        /// <summary>
        /// Checks whether the student, of the provided student id,
        /// already reviewed the course, of the provided course id.
        /// </summary>
        /// <param name="studentId">Provided student id.</param>
        /// <param name="courseId">Provided course id.</param>
        /// <returns>Whether the student already reviewed the course.</returns>
        public async Task<bool> HasStudentAlreadyReviewedCourse(int studentId, int courseId)
            => await reviewRepository.HasStudentAlreadyReviewedCourse(studentId, courseId);

        /// <summary>
        /// Creates a Review object with the given ReviewModel.
        /// </summary>
        /// <param name="model">Review information.</param>
        /// <param name="model">Id of attached student.</param>
        /// <returns>Created Review object.</returns>
        public async Task<Review> CreateReview(ReviewModel model, int studentId)
            => new()
            {
                CreationTime = DateTime.Now,
                LastModificationTime = DateTime.Now,
                Rating = model.Rating,
                Comment = model.Comment,
                IsAnonymous = model.IsAnonymous,
                Course = await courseRepository.GetCourse(model.CourseId),
                Student = await userRepository.GetStudent(studentId)
            };

        /// <summary>
        /// Posts the provided review.
        /// </summary>
        /// <param name="review">Provided review.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task PostReview(Review review)
            => await reviewRepository.PostReview(review);

        /// <summary>
        /// Updates the average rating of the course with the newly added rating.
        /// </summary>
        /// <param name="courseId">Provided course id.</param>
        /// <param name="rating">Provided new rating.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task UpdateAverageRating(int courseId, double rating)
            => await courseRepository.UpdateAverageRating(courseId, rating);

        public async Task<bool> DoesReviewExist(int id)
            => await reviewRepository.DoesReviewExist(id);

        /// <summary>
        /// Get next n reviews for a course.
        /// </summary>
        /// <param name="id">id after which to get the reviews.</param>
        /// <param name="n">the number of reviews to get</param>
        /// <returns>A list of CourseReviewModels</returns>
        public async Task<List<CourseReviewModel>> GetNextReviewsForCourse(int id, int n)
        {
            List<Review> reviews = await reviewRepository.GetNextReviewsForCourse(id, n);
            var models = new List<CourseReviewModel>();
            foreach (var x in reviews)
            {
                models.Add(new CourseReviewModel
                {
                    Id = x.Id,
                    Rating = x.Rating,
                    IsAnonymous = x.IsAnonymous,
                    CourseId = x.Course.Id
                });
            }

            return models;
        }
    }
}
