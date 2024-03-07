using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories
{
    /// <summary>
    /// Handles all calls to database in regard to Review model.
    /// </summary>
    public class VerificationRepository
    {
        private readonly UniUnboxdDbContext dbContext;

        public VerificationRepository(UniUnboxdDbContext dbContext)
        {
            this.dbContext = dbContext;
        }

        public async Task<VerificationApplication[]> GetNextApplications(University user, int startID, int n)
            => await dbContext.Applications.Where(a => a.TargetUniversity == user && a.Id > startID).Take(n).ToArrayAsync();

        public async Task<VerificationApplication> AddApplication(VerificationApplication application)
        {
            dbContext.Applications.Add(application);
            await dbContext.SaveChangesAsync();
            return application;
        }

        public async Task<bool> SetVerificationStatus(User user, VerificationStatus status)
        {
            user.VerificationStatus = status;
            await dbContext.SaveChangesAsync();
            return true;
        }
    }
}
