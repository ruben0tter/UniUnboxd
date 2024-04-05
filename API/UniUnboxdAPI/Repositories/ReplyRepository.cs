using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories
{
    public class ReplyRepository(UniUnboxdDbContext dbContext)
    {
        public async Task PostReply(Reply reply)
        {
            await dbContext.Replies.AddAsync(reply);
            await dbContext.SaveChangesAsync();
        }
    }
}
