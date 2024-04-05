using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories
{
    /// <summary>
    /// Handles all calls to database in regard to VerificationApplication model.
    /// </summary>
    public class VerificationRepository(UniUnboxdDbContext dbContext)
    {
        public async Task<VerificationApplication[]> GetNextApplications(University user, int startID, int n)
            => await dbContext.Applications.Where(a => a.TargetUniversity == user && a.Id > startID).Take(n).ToArrayAsync();

        public async Task<VerificationApplication> AddApplication(VerificationApplication application)
        {
            dbContext.Applications.Add(application);
            await dbContext.SaveChangesAsync();
            return application;
        }

        public async Task SetUniversity(User user, int universityId)
        {
            user.UniversityId = universityId;
            await dbContext.SaveChangesAsync();
        }

        public async Task<bool> RemoveApplication(int userId)
        {
            var applications = await dbContext.Applications.Where(a => a.UserId == userId).ToArrayAsync();
            dbContext.Applications.RemoveRange(applications);
            await dbContext.SaveChangesAsync();
            return true;
        }
    }
}
