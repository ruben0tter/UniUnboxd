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
        /// <summary>
        /// Retrieves a set of verification applications for a university, starting after a specified ID.
        /// </summary>
        /// <param name="user">The university to retrieve applications for.</param>
        /// <param name="startID">The ID after which to start retrieving applications.</param>
        /// <param name="n">The number of applications to retrieve.</param>
        /// <returns>An array of verification applications.</returns>
        public async Task<VerificationApplication[]> GetNextApplications(University user, int startID, int n)
            => await dbContext.Applications.Where(a => a.TargetUniversity == user && a.Id > startID).Take(n).ToArrayAsync();

        /// <summary>
        /// Adds a new verification application to the database.
        /// </summary>
        /// <param name="application">The verification application to add.</param>
        /// <returns>The added verification application, now including an ID assigned by the database.</returns>
        public async Task<VerificationApplication> AddApplication(VerificationApplication application)
        {
            dbContext.Applications.Add(application);
            await dbContext.SaveChangesAsync();
            return application;
        }

        /// <summary>
        /// Assigns a university ID to a user, effectively setting their verified university.
        /// </summary>
        /// <param name="user">The user to update.</param>
        /// <param name="universityId">The ID of the university to associate with the user.</param>
        public async Task SetUniversity(User user, int universityId)
        {
            user.UniversityId = universityId;
            await dbContext.SaveChangesAsync();
        }

        /// <summary>
        /// Removes all verification applications associated with a specific user.
        /// </summary>
        /// <param name="userId">The ID of the user whose applications should be removed.</param>
        /// <returns>A boolean indicating whether the operation was successful.</returns>
        public async Task<bool> RemoveApplication(int userId)
        {
            var applications = await dbContext.Applications.Where(a => a.UserId == userId).ToArrayAsync();
            dbContext.Applications.RemoveRange(applications);
            await dbContext.SaveChangesAsync();
            return true;
        }
    }
}
