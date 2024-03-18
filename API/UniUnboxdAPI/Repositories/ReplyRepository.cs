using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories
{
    public class ReplyRepository
    {
        private readonly UniUnboxdDbContext dbContext;

        public ReplyRepository(UniUnboxdDbContext dbContext)
        {
            this.dbContext = dbContext;
        }

        public async Task PostReply(Reply reply)
        {
            await dbContext.Replies.AddAsync(reply);
            await dbContext.SaveChangesAsync();
        }
    }
}
