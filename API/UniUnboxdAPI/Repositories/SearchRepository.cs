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
        /// <summary>
        /// Retrieves a list of courses matching the search criteria.
        /// </summary>
        /// <param name="options">Search parameters.</param>
        /// <returns>A list of courses.</returns>
        public async Task<List<Course>> GetCourses(SearchOptions options)
            => await dbContext.Courses.Where(i => i.Name.Contains(options.Search) || i.Code == options.Search)
                                      .Include(i => i.University)
                                      .Skip(options.Start ?? throw new ArgumentNullException(nameof(options.Start)))
                                      .Take(options.Count ?? throw new ArgumentNullException(nameof(options.Count)))
                                      .ToListAsync();

        /// <summary>
        /// Retrieves a list of courses from a specific university matching the search criteria.
        /// </summary>
        /// <param name="options">Search parameters, including the university ID.</param>
        /// <returns>A list of courses from the specified university.</returns>
        public async Task<List<Course>> GetCoursesFromUni(SearchOptions options)
            => await dbContext.Courses.Where(i => i.University.Id == options.UniversityId && (i.Name.Contains(options.Search) || i.Code == options.Search))
                                      .Include(i => i.University)
                                      .Skip(options.Start ?? throw new ArgumentNullException(nameof(options.Start)))
                                      .Take(options.Count ?? throw new ArgumentNullException(nameof(options.Count)))
                                      .ToListAsync();

        /// <summary>
        /// Retrieves a list of users matching the search criteria, excluding universities.
        /// </summary>
        /// <param name="options">Search parameters.</param>
        /// <returns>A list of users.</returns>
        public async Task<List<User>> GetUsers(SearchOptions options)
            => await dbContext.Users.Where(i => i.UserName.Contains(options.Search) && i.UserType != UserType.University)
                                    .Skip(options.Start ?? throw new ArgumentNullException(nameof(options.Start)))
                                    .Take(options.Count ?? throw new ArgumentNullException(nameof(options.Count)))
                                    .ToListAsync();
    }
}
