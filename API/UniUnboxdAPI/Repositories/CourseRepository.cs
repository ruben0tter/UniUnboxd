using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories
{
    /// <summary>
    /// Handles all calls to database in regard to Course model.
    /// </summary>
    public class CourseRepository
    {
        private readonly UniUnboxdDbContext dbContext;

        public CourseRepository(UniUnboxdDbContext dbContext)
        {
            this.dbContext = dbContext;
        }

        public async Task<bool> DoesCourseExist(int id)
            => await dbContext.Courses.AnyAsync(c => c.Id == id);

        public async Task<Course> GetCourse(int id)
            => await dbContext.Courses.Where(i => i.Id == id).Include(i => i.University).FirstAsync();

        public async Task<Course> GetCourseAndConnectedData(int id, int numOfReviews)
            => await dbContext.Courses.Where(i => i.Id == id)
                                    .Include(i => i.University)
                                    .Include(i => i.Reviews.Take(numOfReviews))
                                    .ThenInclude(i => i.Student)
                                    .FirstAsync();

        public async Task PostCourse(Course course)
        {
            await dbContext.Courses.AddAsync(course);
            await dbContext.SaveChangesAsync();
        }

        public async Task UpdateAverageRatingAfterPost(int id, double addedRating)
        {
            var course = await GetCourse(id);
            var reviewCount = await dbContext.Reviews.Where(i => i.Course.Id == id).CountAsync();
            course.AverageRating = ((reviewCount - 1) * course.AverageRating + addedRating) / reviewCount;
            await dbContext.SaveChangesAsync();
        }

        public async Task UpdateAverageRatingAfterPut(int id, double addedRating, double removedRating)
        {
            var course = await GetCourse(id);
            var reviewCount = await dbContext.Reviews.Where(i => i.Course.Id == id).CountAsync();
            course.AverageRating = (reviewCount * course.AverageRating - removedRating + addedRating) / reviewCount;
            await dbContext.SaveChangesAsync();
        }
        public async Task UpdateAverageRatingAfterDelete(int id, double removedRating)
        {
            var course = await GetCourse(id);
            var reviewCount = await dbContext.Reviews.Where(i => i.Course.Id == id).CountAsync();
            course.AverageRating = reviewCount > 0 ? ((reviewCount + 1) * course.AverageRating - removedRating) / reviewCount : 0.0;
            await dbContext.SaveChangesAsync();
        }

        public async Task<ICollection<Course>> GetPopularCourseOfLastWeek()
            => await dbContext.Courses.OrderByDescending(i => i.Reviews.Where(i => i.LastModificationTime > DateTime.Now.AddDays(-7)).Count())
                                    .Take(10).ToListAsync();

        public async Task<ICollection<Course>> GetPopularCourseOfLastWeekByUniversity(int id)
            => await dbContext.Courses.Where(i => i.University.Id == id)
                                .OrderByDescending(i => i.Reviews.Where(i => i.LastModificationTime > DateTime.Now.AddDays(-7)).Count())
                                .Take(10).ToListAsync();

        public async Task PutCourse(Course course)
        {
            dbContext.Courses.Update(course);
            await dbContext.SaveChangesAsync();
        }
        public async Task DeleteCourse(Course course)
        {
            dbContext.Courses.Remove(course);
            await dbContext.SaveChangesAsync();
        }

    }
}
