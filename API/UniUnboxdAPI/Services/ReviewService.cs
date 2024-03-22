﻿using System.Security.Claims;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Models.DataTransferObjects.ReviewPage;
using UniUnboxdAPI.Models.DataTransferObjects.StudentHomePage;
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
        /// Check whether there exists a review with the provided id.
        /// </summary>
        /// <param name="reviewId">Provided review id.</param>
        /// <returns>Whether there exists a review with the provided id.</returns>
        public async Task<bool> DoesReviewExist(int reviewId)
            => await reviewRepository.DoesReviewExist(reviewId);

        /// <summary>
        /// Gets a Review object as a DTO that is attached to the provided id.
        /// </summary>
        /// <param name="id">Provided review id.</param>
        /// <returns>The review</returns>
        public async Task<ReviewPageModel?> GetReviewPageModel(int id)
        {
            var review = await reviewRepository.GetReviewAndConnectedData(id);

            if (review == null)
                return null;

            return CreateReviewPageModel(review);
        }

        /// <summary>
        /// Gets the latest reviews by friends of the student attached to the provided id.
        /// </summary>
        /// <param name="id">Provided student id.</param>
        /// <returns>The latest reviews by friends of the provided student.</returns>
        public async Task<ICollection<ReviewGridModel>> GetLatestReviewsByFriends(int id)
        {
            ICollection<Review> reviews = await reviewRepository.GetLatestReviewsByFriends(id);
            return CreateReviewGridModelCollection(reviews);
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
                Student = await userRepository.GetStudent(studentId),
                Replies = new List<Reply>()
            };

        /// <summary>
        /// Posts the provided review.
        /// </summary>
        /// <param name="review">Provided review.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task PostReview(Review review)
            => await reviewRepository.PostReview(review);

        /// <summary>
        /// Get next n reviews for a course.
        /// </summary>
        /// <param name="id">id after which to get the reviews.</param>
        /// <param name="n">the number of reviews to get</param>
        /// <returns>A list of CourseReviewModels</returns>
        public async Task<List<CourseReviewModel>> GetNextReviewsForCourse(int id, int courseId, int n)
        {
            List<Review> reviews = await reviewRepository.GetNextReviewsForCourse(id, courseId, n);
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

        /// <summary>
        /// Updates the average rating of the course with the newly added rating.
        /// </summary>
        /// <param name="courseId">Provided course id.</param>
        /// <param name="rating">Provided new rating.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task UpdateAverageRatingAfterPost(int courseId, double rating)
            => await courseRepository.UpdateAverageRatingAfterPost(courseId, rating);

        /// <summary>
        /// Gets a Review object that is attached to the provided id.
        /// </summary>
        /// <param name="id">Provided review id.</param>
        /// <returns>The review</returns>
        public async Task<Review?> GetReview(int id)
            => await reviewRepository.GetReviewAndConnectedData(id);

        /// <summary>
        /// Updates the given Review object with the provided data in the ReviewModel object.
        /// </summary>
        /// <param name="review">Provided Review.</param>
        /// <param name="model">Provided ReviewModel.</param>
        public void UpdateReview(Review review, ReviewModel model)
        {
            review.Rating = model.Rating;
            review.Comment = model.Comment;
            review.IsAnonymous = model.IsAnonymous;
        }

        /// <summary>
        /// Puts the provided review.
        /// </summary>
        /// <param name="review">Provided review.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task PutReview(Review review)
            => await reviewRepository.PutReview(review);

        /// <summary>
        /// Updates the average rating of the course with the newly changed rating.
        /// </summary>
        /// <param name="courseId">Provided course id.</param>
        /// <param name="addedRating">Provided new rating.</param>
        /// <param name="removedRating">Provided old rating.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task UpdateAverageRatingAfterPut(int courseId, double addedRating, double removedRating)
            => await courseRepository.UpdateAverageRatingAfterPut(courseId, addedRating, removedRating);

        /// <summary>
        /// Deletes the provided review.
        /// </summary>
        /// <param name="review">Provided review.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task DeleteReview(Review review)
            => await reviewRepository.DeleteReview(review);

        /// <summary>
        /// Updates the average rating of the course with the newly removed rating.
        /// </summary>
        /// <param name="courseId">Provided course id.</param>
        /// <param name="removedRating">Provided removed rating.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task UpdateAverageRatingAfterDelete(int courseId, double removedRating)
            => await courseRepository.UpdateAverageRatingAfterDelete(courseId, removedRating);

        private static ReviewPageModel CreateReviewPageModel(Review model)
            => new()
            {
                Id = model.Id,
                Date = model.LastModificationTime,
                Rating = model.Rating,
                Comment = model.Comment,
                IsAnonymous = model.IsAnonymous,
                CourseHeader = new()
                {
                    Id = model.Course.Id,
                    Name = model.Course.Name,
                    Code = model.Course.Code,
                    Image = model.Course.Image!,
                    Banner = model.Course.Banner!
                },
                StudentHeader = new()
                {
                    Id = model.Student.Id,
                    Name = model.Student.UserName!,
                    Image = model.Student.Image!
                },
                Replies = model.Replies.Select(i => new ReviewReplyModel()
                {
                    Text = i.Text,
                    UserHeader = new UserHeaderModel()
                    {
                        Id = i.User.Id,
                        Name = i.User.UserName!,
                        Image = i.User is Student ? (i.User as Student)!.Image! :
                            (i.User as Professor)!.Image!
                    }
                }).ToList()
            };

        private static ICollection<ReviewGridModel> CreateReviewGridModelCollection(ICollection<Review> reviews)
            => reviews.Select(i => new ReviewGridModel()
            {
                Id = i.Id,
                CourseName = i.Course.Name,
                CourseImage = i.Course.Image!,
                StudentId = i.Student.Id,
                StudentName = i.Student.UserName!,
                StudentImage = i.Student.Image!,
                Rating = i.Rating
            }).ToList();
    }
}
