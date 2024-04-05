using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories
{
    /// <summary>
    /// Handles all calls to database in regard to Course model.
    /// </summary>
    public class CourseRepository(UniUnboxdDbContext dbContext)
    {
        /// <summary>
        /// Checks if a specific course exists.
        /// </summary>
        /// <param name="id">The ID of the course to check.</param>
        /// <returns>True if the course exists, false otherwise.</returns>
        public async Task<bool> DoesCourseExist(int id)
            => await dbContext.Courses.AnyAsync(c => c.Id == id);

        /// <summary>
        /// Retrieves a course by ID, including university and assigned professors.
        /// </summary>
        /// <param name="id">The ID of the course.</param>
        /// <returns>A course object if found; otherwise, null.</returns>
        public async Task<Course> GetCourse(int id)
            => await dbContext.Courses.Where(c => c.Id == id)
                .Include(c => c.University)
                .Include(c => c.AssignedProfessors)
                .FirstOrDefaultAsync();

        /// <summary>
        /// Retrieves a course and its connected data (university, reviews, students, and professors) by its ID.
        /// </summary>
        /// <param name="id">The ID of the course.</param>
        /// <returns>A course object with connected data if found; otherwise, null.</returns>
        public async Task<Course> GetCourseAndConnectedData(int id)
            => await dbContext.Courses.Where(c => c.Id == id)
                .Include(c => c.University)
                .Include(c => c.Reviews)
                    .ThenInclude(r => r.Student)
                .Include(c => c.AssignedProfessors)
                .FirstOrDefaultAsync();

        /// <summary>
        /// Adds a new course to the database.
        /// </summary>
        /// <param name="course">The course to add.</param>
        public async Task PostCourse(Course course)
        {
            dbContext.Courses.Add(course);
            await dbContext.SaveChangesAsync();
        }

        /// <summary>
        /// Updates the average rating of a course after posting a new review.
        /// </summary>
        /// <param name="id">The ID of the course.</param>
        /// <param name="addedRating">The rating of the added review.</param>
        /// <param name="isAnon">Indicates if the review is anonymous.</param>
        public async Task UpdateAverageRatingAfterPost(int id, double addedRating, bool isAnon)
        {
            var course = await GetCourse(id);
            var reviewAnonCount = await dbContext.Reviews.Where(i => i.Course.Id == id && i.IsAnonymous).CountAsync();
            var reviewNonanonCount = await dbContext.Reviews.Where(i => i.Course.Id == id && !i.IsAnonymous).CountAsync();
            if (isAnon)
                course.AnonymousRating = 
                    ((reviewAnonCount - 1) * course.AnonymousRating + addedRating) / reviewAnonCount;
            else
                course.NonanonymousRating = ((reviewNonanonCount - 1) * course.NonanonymousRating + addedRating) /
                                            reviewNonanonCount;
            await dbContext.SaveChangesAsync();
        }

        /// Updates the average rating of a course after modifying a review.
        /// </summary>
        /// <param name="id">The ID of the course.</param>
        /// <param name="addedRating">The new rating of the modified review.</param>
        /// <param name="removedRating">The old rating of the modified review.</param>
        public async Task UpdateAverageRatingAfterPut(int id, double addedRating, double removedRating)
        {
            var course = await GetCourse(id);
            double reviewAnonSum = 0;
            double reviewNonanonSum = 0;
            int reviewAnonCount = 0;
            int reviewNonanonCount = 0;

            foreach (var x in dbContext.Reviews.Include(i => i.Course))
            {
                if (x.Course.Id != id)
                    continue;
                if (x.IsAnonymous)
                {
                    reviewAnonCount++;
                    reviewAnonSum += x.Rating;
                    continue;
                }
                reviewNonanonCount++;
                reviewNonanonSum += x.Rating;
            }

            course.AnonymousRating = reviewAnonCount == 0 ? 0 : reviewAnonSum / reviewAnonCount;
            course.NonanonymousRating = reviewNonanonCount == 0 ? 0 : reviewNonanonSum / reviewNonanonCount;
            await dbContext.SaveChangesAsync();
        }

        /// <summary>
        /// Updates the average rating of a course after deleting a review.
        /// </summary>
        /// <param name="id">The ID of the course.</param>
        /// <param name="removedRating">The rating of the deleted review.</param>
        /// <param name="isAnon">Indicates if the review was anonymous.</param>
        public async Task UpdateAverageRatingAfterDelete(int id, double removedRating, bool isAnon)
        {
            var course = await GetCourse(id);
            var reviewAnonCount = await dbContext.Reviews.Where(i => i.Course.Id == id && i.IsAnonymous).CountAsync();
            var reviewNonanonCount = await dbContext.Reviews.Where(i => i.Course.Id == id && !i.IsAnonymous).CountAsync();
            if (isAnon)
                course.AnonymousRating = reviewAnonCount > 0
                    ? ((reviewAnonCount + 1) * course.AnonymousRating - removedRating) / reviewAnonCount
                    : 0.0;
            else
                course.NonanonymousRating = reviewNonanonCount > 0
                    ? ((reviewNonanonCount + 1) * course.NonanonymousRating - removedRating) / reviewNonanonCount
                    : 0.0;

            await dbContext.SaveChangesAsync();
        }

        /// <summary>
        /// Gets the top 10 most popular courses based on reviews in the last week.
        /// </summary>
        /// <returns>A collection of popular courses.</returns>
        public async Task<ICollection<Course>> GetPopularCoursesOfLastWeek()
        {
            var oneWeekAgo = DateTime.Now.AddDays(-7);
            return await dbContext.Courses
                .Where(c => c.Reviews.Any(r => r.LastModificationTime > oneWeekAgo))
                .OrderByDescending(c => c.Reviews.Count(r => r.LastModificationTime > oneWeekAgo))
                .Take(10)
                .ToListAsync();
        }

        public async Task<ICollection<Course>> GetPopularCourseOfLastWeekByUniversity(int id)
            => await dbContext.Courses.Where(i => i.Reviews.Any(i => i.LastModificationTime > DateTime.Now.AddDays(-7))
                && i.University.Id == id)
                .OrderByDescending(i => i.Reviews.Where(i => i.LastModificationTime > DateTime.Now.AddDays(-7)).Count())
                .Take(10).ToListAsync();

        public async Task<ICollection<Course>> GetPopularCourseOfLastWeekByFriends(int id)
            => await dbContext.Courses.Where(i => i.Reviews.Any(i => i.LastModificationTime > DateTime.Now.AddDays(-7)
                        && i.Student.Followers!.Any(i => i.FollowingStudentId == id)))
                        .OrderByDescending(i => i.Reviews.Where(i => i.LastModificationTime > DateTime.Now.AddDays(-7)
                        && i.Student.Followers!.Any(i => i.FollowingStudentId == id)).Count())
                        .Take(10).ToListAsync();

        public async Task<ICollection<Course>> GetLastEditedCoursesByUniversity(int id)
            => await dbContext.Courses.Where(i => i.University.Id == id)
                        .OrderByDescending(i => i.LastModificationTime)
                        .Take(4).ToListAsync();

        /// <summary>
        /// Updates a course in the database.
        /// </summary>
        /// <param name="course">The course to update.</param>
        public async Task PutCourse(Course course)
        {
            dbContext.Courses.Update(course);
            await dbContext.SaveChangesAsync();
        }

        /// <summary>
        /// Deletes a course from the database.
        /// </summary>
        /// <param name="course">The course to be deleted.</param>
        public async Task DeleteCourse(Course course)
        {
            dbContext.Courses.Remove(course);
            await dbContext.SaveChangesAsync();
        }

        /// <summary>
        /// Retrieves all courses assigned to a specific professor.
        /// </summary>
        /// <param name="professorId">The ID of the professor.</param>
        /// <returns>A list of assigned courses.</returns>
        public async Task<List<Course>> GetAssignedCourses(int professorId)
            => await dbContext.Courses
                .Where(c => c.AssignedProfessors.Any(p => p.ProfessorId == professorId))
                .ToListAsync();
    }
}
