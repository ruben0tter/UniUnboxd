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
        public async Task<List<Course>> GetCourses(String search, int start, int count)
            => await dbContext.Courses.Where(i => i.Name.Contains(search) || i.Code == search)
                                    .Skip(start)
                                    .Take(count)
                                    .ToListAsync();

        public async Task<List<Course>> GetAllCourses(String search)
            => await dbContext.Courses.Where(i => i.Name.Contains(search) || i.Code == search)
                                    .ToListAsync();

        public async Task<List<User>> GetUsers(String search, int start, int count)
            => await dbContext.Users.Where(i => i.UserName.Contains(search))
                                    .Skip(start)
                                    .Take(count)
                                    .ToListAsync();

        public async Task<List<User>> GetAllUsers(String search)
            => await dbContext.Users.Where(i => i.UserName.Contains(search))
                                    .ToListAsync();
    }
}
