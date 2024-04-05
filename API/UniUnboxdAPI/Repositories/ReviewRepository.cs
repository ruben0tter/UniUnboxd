using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories
{
    /// <summary>
    /// Handles all calls to database in regard to Review model.
    /// </summary>
    public class ReviewRepository(UniUnboxdDbContext dbContext)
    {
        /// <summary>
        /// Checks if a review exists.
        /// </summary>
        /// <param name="id">The review ID.</param>
        /// <returns>True if the review exists; otherwise, false.</returns>
        public async Task<bool> DoesReviewExist(int id) => await dbContext.Reviews.AnyAsync(i => i.Id == id);

        /// <summary>
        /// Determines if a student has already reviewed a specific course.
        /// </summary>
        /// <param name="studentId">The student ID.</param>
        /// <param name="courseId">The course ID.</param>
        /// <returns>True if the review exists; otherwise, false.</returns>
        public async Task<bool> HasStudentAlreadyReviewedCourse(int studentId, int courseId)
            => await dbContext.Reviews.AnyAsync(i => i.Student.Id == studentId && i.Course.Id == courseId);

        /// <summary>
        /// Retrieves a review by its ID.
        /// </summary>
        /// <param name="id">The review ID.</param>
        /// <returns>The review.</returns>
        public async Task<Review> GetReview(int id) => await dbContext.Reviews.Where(i => i.Id == id).FirstAsync();

        /// <summary>
        /// Gets all reviews made by friends on a specific course.
        /// </summary>
        /// <param name="studentId">The student ID.</param>
        /// <param name="courseId">The course ID.</param>
        /// <returns>A collection of reviews.</returns>
        public async Task<ICollection<Review>> GetAllFriendsThatReviewed(int studentId, int courseId)
            => await dbContext.Reviews
                .Where(i => i.Course.Id == courseId && i.Student.Followers != null && i.Student.Followers!.Any(i => i.FollowingStudentId == studentId) && !i.IsAnonymous)
                .Include(i => i.Student).ToListAsync();

        /// <summary>
        /// Retrieves a review and its connected data by ID.
        /// </summary>
        /// <param name="id">The review ID.</param>
        /// <returns>The review with related data or null if not found.</returns>
        public async Task<Review?> GetReviewAndConnectedData(int id)
            => await dbContext.Reviews.Where(i => i.Id == id)
                .Include(i => i.Student)
                .ThenInclude(i => i.NotificationSettings)
                .Include(i => i.Course)
                .Include(i => i.Replies)
                .ThenInclude(i => i.User)
                .Include(i => i.Likes)
                .ThenInclude(i => i.Student)
                .FirstOrDefaultAsync();

        /// <summary>
        /// Retrieves a review for a course made by a specific student.
        /// </summary>
        /// <param name="courseId">The course ID.</param>
        /// <param name="studentId">The student ID.</param>
        /// <returns>The review or null if not found.</returns>
        public async Task<Review?> GetCourseReviewByStudent(int courseId, int studentId)
            => await dbContext.Reviews.Where(i => i.Course.Id == courseId && i.Student.Id == studentId)
                .Include(i => i.Student).FirstOrDefaultAsync();

        /// <summary>
        /// Gets the latest reviews made by friends.
        /// </summary>
        /// <param name="id">The student ID for whom the friends are associated.</param>
        /// <returns>A collection of the latest reviews by friends.</returns>
        public async Task<ICollection<Review>> GetLatestReviewsByFriends(int id)
            => await dbContext.Reviews.Where(i => i.Student.Followers!.Any(i => i.FollowingStudentId == id) 
                && i == i.Student.Reviews!.Where(i => !i.IsAnonymous)
                .OrderBy(i => i.CreationTime).Last())
                .Include(i => i.Course).Include(i => i.Student)
                .OrderByDescending(i => i.CreationTime)
                .Take(10).ToListAsync();

        /// <summary>
        /// Posts a new review to the database.
        /// </summary>
        /// <param name="review">The review to add.</param>
        public async Task PostReview(Review review)
        {
            await dbContext.Reviews.AddAsync(review);
            await dbContext.SaveChangesAsync();
        }

        /// <summary>
        /// Gets a list of reviews for a course starting after a specific ID.
        /// </summary>
        /// <param name="id">The review ID to start after.</param>
        /// <param name="courseId">The course ID.</param>
        /// <param name="n">The number of reviews to return.</param>
        /// <returns>A list of reviews.</returns>
        public async Task<List<Review>> GetNextReviewsForCourse(int id, int courseId, int n)
            => await dbContext.Reviews.Where(i => i.Id > id && i.Course.Id == courseId).Take(n).Include(i => i.Course).ToListAsync();

        /// <summary>
        /// Updates an existing review.
        /// </summary>
        /// <param name="review">The review to update.</param>
        public async Task PutReview(Review review)
        {
            dbContext.Reviews.Update(review);
            await dbContext.SaveChangesAsync();
        }

        /// <summary>
        /// Checks if a review was written by a specific student.
        /// </summary>
        /// <param name="reviewId">The review ID.</param>
        /// <param name="studentId">The student ID.</param>
        /// <returns>True if the student wrote the review; otherwise, false.</returns>
        public async Task<bool> IsReviewWrittenByStudent(int reviewId, int studentId)
        {
            var review = await dbContext.Reviews.Where(i => i.Id == reviewId).Include(i => i.Student).FirstAsync();
            return review.Student.Id == studentId;
        }

        /// <summary>
        /// Determines if a student has liked a specific review.
        /// </summary>
        /// <param name="reviewId">The review ID.</param>
        /// <param name="studentId">The student ID.</param>
        /// <returns>True if the student has liked the review; otherwise, false.</returns>
        public async Task<bool> DoesStudentLikeReview(int reviewId, int studentId)
        {
            var review = await dbContext.Reviews.Where(i => i.Id == reviewId).Include(i => i.Likes).FirstAsync();
            return review.Likes.Any(i => i.StudentId == studentId);
        }

        /// <summary>
        /// Adds a like to a review by a student.
        /// </summary>
        /// <param name="reviewId">The review ID.</param>
        /// <param name="studentId">The student ID.</param>
        public async Task LikeReview(int reviewId, int studentId)
        {
            var review = await dbContext.Reviews.FindAsync(reviewId);
            var student = await dbContext.Students.FindAsync(studentId);
            var like = new Like() { Review = review!, Student = student! };

            await dbContext.Likes.AddAsync(like);
            await dbContext.SaveChangesAsync();
        }

        /// <summary>
        /// Removes a like from a review by a student.
        /// </summary>
        /// <param name="reviewId">The review ID.</param>
        /// <param name="studentId">The student ID.</param>
        public async Task UnlikeReview(int reviewId, int studentId)
        {
            var like = await dbContext.Likes.Where(i => i.ReviewId == reviewId && i.StudentId == studentId).FirstAsync();
            dbContext.Likes.Remove(like);
            await dbContext.SaveChangesAsync();
        }

        /// <summary>
        /// Deletes a review from the database.
        /// </summary>
        /// <param name="review">The review to delete.</param>
        public async Task DeleteReview(Review review)
        {
            dbContext.Reviews.Remove(review);
            await dbContext.SaveChangesAsync();
        }
    }
}
