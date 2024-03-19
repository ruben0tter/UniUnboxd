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

        public async Task<bool> DoesReviewExist(int id)
            => await dbContext.Reviews.AnyAsync(i => i.Id == id);

        public async Task<bool> HasStudentAlreadyReviewedCourse(int studentId, int courseId)
            => await dbContext.Reviews.AnyAsync(i => i.Student.Id == studentId && i.Course.Id == courseId);

        public async Task<Review> GetReview(int id)
               => await dbContext.Reviews.Where(i => i.Id == id).FirstAsync();

        public async Task<Review?> GetReviewAndConnectedData(int id)
            => await dbContext.Reviews.Where(i => i.Id == id)
                                        .Include(i => i.Student)
                                        .Include(i => i.Course)
                                        .Include(i => i.Replies)  
                                        .ThenInclude(i => i.User)        
                                        .FirstOrDefaultAsync();

        public async Task<ICollection<Review>> GetLatestReviewsByFriends(int id)
            => await dbContext.Reviews.Where(i => i.Student.Followers!.Any(i => i.FollowingStudentId == id) 
                                        && i == i.Student.Reviews!.Where(i => !i.IsAnonymous)
                                        .OrderBy(i => i.CreationTime).Last())
                                        .Include(i => i.Course).Include(i => i.Student)
                                        .OrderByDescending(i => i.CreationTime)
                                        .Take(10).ToListAsync();

        public async Task PostReview(Review review)
        {
            await dbContext.Reviews.AddAsync(review);
            await dbContext.SaveChangesAsync();
        }
        
        public async Task<List<Review>> GetNextReviewsForCourse(int id, int courseId, int n)
            => await dbContext.Reviews.Where(i => i.Id > id && i.Course.Id == courseId).Take(n).Include(i => i.Course).ToListAsync();
        
        public async Task PutReview(Review review)
        {
            dbContext.Reviews.Update(review);
            await dbContext.SaveChangesAsync();
        }

        public async Task DeleteReview(Review review)
        {
            dbContext.Reviews.Remove(review);
            await dbContext.SaveChangesAsync();
        }
    }
}
