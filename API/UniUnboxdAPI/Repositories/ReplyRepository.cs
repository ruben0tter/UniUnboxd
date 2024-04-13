using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories
{
    public class ReplyRepository(UniUnboxdDbContext dbContext)
    {
        /// <summary>
        /// Adds a new reply to the database and saves changes.
        /// </summary>
        /// <param name="reply">The reply to add.</param>
        /// <returns>A task representing the asynchronous operation.</returns>
        public async Task PostReply(Reply reply)
        {
            await dbContext.Replies.AddAsync(reply);
            await dbContext.SaveChangesAsync();
        }
    }
}
