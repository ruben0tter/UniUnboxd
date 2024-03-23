using Microsoft.EntityFrameworkCore;
using System.Drawing;
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

        public async Task<String> GetImageOf(int id, UserType type)
        {
            if(type == UserType.Student)
            {
                Student result = await dbContext.Students.Where(i => i.Id == id).FirstAsync();
                return result.Image;
            } 
            else
            {
                Professor result = await dbContext.Professors.Where(i => i.Id == id).FirstAsync();
                return result.Image;
            }
            
        }

        public async Task<Course> GetCourse(int id)
            => await dbContext.Courses.Where(i => i.Id == id).FirstAsync();
    }
}
