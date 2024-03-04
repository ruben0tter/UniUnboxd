using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories
{
    /// <summary>
    /// Handles all calls to database in regard to User model.
    /// </summary>
    public class UserRepository
    {
        private readonly UniUnboxdDbContext dbContext;

        public UserRepository(UniUnboxdDbContext dbContext)
        {
            this.dbContext = dbContext;
        }

        public async Task<bool> DoesStudentExist(int id)
            => await dbContext.Students.AnyAsync(c => c.Id == id);

        public async Task<Student> GetStudent(int id)
            => await dbContext.Students.Where(i => i.Id == id).FirstAsync();

        
        public async Task<bool> DoesUniversityExist(int id)
            => await dbContext.Universities.AnyAsync(c => c.Id == id);
        public async Task<University> GetUniversity(int id)
            => await dbContext.Universities.Where(i => i.Id == id).FirstAsync();
    }
}
