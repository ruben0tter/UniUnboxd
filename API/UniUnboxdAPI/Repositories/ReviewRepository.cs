using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;

namespace UniUnboxdAPI.Repositories
{
    /// <summary>
    /// Handles all calls to database in regard to Review model.
    /// </summary>
    public class ReviewRepository
    {
        private readonly UniUnboxdDbContext dbContext;

        public ReviewRepository(UniUnboxdDbContext dbContext)
        {
            this.dbContext = dbContext;
        }

        public async Task<bool> HasStudentAlreadyReviewedCourse(int studentId, int courseId)
            => await dbContext.Reviews.AnyAsync(i => i.Student.Id == studentId && i.Course.Id == courseId);

        public async Task PostReview(Review review)
        {
            await dbContext.Reviews.AddAsync(review);
            await dbContext.SaveChangesAsync();
        }

        public async Task<bool> DoesReviewExist(int id)
            => await dbContext.Reviews.AnyAsync(i => i.Id == id);

        public async Task<List<Review>> GetNextReviewsForCourse(int id, int n)
            => await dbContext.Reviews.Where(i => i.Id > id).Take(n).Include(i => i.Course).ToListAsync();
    }
}
