using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
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
            => await dbContext.Courses.Where(i => i.Id == id)
                .Include(i => i.University)
                .Include(i => i.AssignedProfessors).FirstAsync();

        public async Task<Course> GetCourseAndConnectedData(int id, int numOfReviews)
            => await dbContext.Courses.Where(i => i.Id == id)
                                    .Include(i => i.University)
                                    .Include(i => i.Reviews)
                                    .ThenInclude(i => i.Student)
                                    .Include(i => i.AssignedProfessors)
                                    .FirstAsync();

        public async Task PostCourse(Course course)
        {
            await dbContext.Courses.AddAsync(course);
            await dbContext.SaveChangesAsync();
            
        }

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

        public async Task<ICollection<Course>> GetPopularCourseOfLastWeek()
            => await dbContext.Courses.Where(i => i.Reviews.Any(i => i.LastModificationTime > DateTime.Now.AddDays(-7)))
                        .OrderByDescending(i => i.Reviews.Where(i => i.LastModificationTime > DateTime.Now.AddDays(-7)).Count())
                        .Take(10).ToListAsync();

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

        public async Task<List<Course>> GetAssignedCourses(int professorId)
            => await dbContext.Courses.Where(i => i.AssignedProfessors
                .Any(i => i.Professor.Id == professorId))
                .ToListAsync();

        public async Task<bool> DoesProfessorAssignmentExist(int courseId, int professorId)
            => await dbContext.CourseProfessorAssignments.AnyAsync(i => i.ProfessorId == professorId 
                                                                        && i.CourseId == courseId);
    }
}
