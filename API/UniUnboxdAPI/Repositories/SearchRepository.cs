using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories
{
    /// <summary>
    /// Handles all calls to database in regard to Course model.
    /// </summary>
    public class SearchRepository(UniUnboxdDbContext dbContext)
    {
        public async Task<List<Course>> GetCourses(SearchOptions options)
            => await dbContext.Courses.Where(i => i.Name.Contains(options.Search) || i.Code == options.Search)
                                    .Include(i => i.University)
                                    .Skip(options.Start ?? throw new ArgumentNullException(nameof(options.Start)))
                                    .Take(options.Count ?? throw new ArgumentNullException(nameof(options.Count)))
                                    .ToListAsync();

        public async Task<List<Course>> GetCoursesFromUni(SearchOptions options)
            => await dbContext.Courses.Where(i => i.University.Id == options.UniversityId && (i.Name.Contains(options.Search) || i.Code == options.Search))
                                    .Include(i => i.University)
                                    .Skip(options.Start ?? throw new ArgumentNullException(nameof(options.Start)))
                                    .Take(options.Count ?? throw new ArgumentNullException(nameof(options.Count)))
                                    .ToListAsync();

        public async Task<List<User>> GetUsers(SearchOptions options)
            => await dbContext.Users.Where(i => i.UserName.Contains(options.Search) && i.UserType != UserType.University)
                                    .Skip(options.Start ?? throw new ArgumentNullException(nameof(options.Start)))
                                    .Take(options.Count ?? throw new ArgumentNullException(nameof(options.Count)))
                                    .ToListAsync();
    }
}
