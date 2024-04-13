namespace UniUnboxdAPITests.Repositories
{
    [TestClass]
    public class ReplyRepositoryTest
    {
        private readonly ReplyRepository replyRepository;
        private readonly UniUnboxdDbContext dbContext;

        public ReplyRepositoryTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("ReplyRepository");
            replyRepository = new ReplyRepository(dbContext);
        }

        [TestMethod]
        public async Task PostReplyTest()
        {
            var student = new Student() { NotificationSettings = new() };
            var course = new Course() { Name = "course", Code = "code", Description = "test", Professor = "professor", Reviews = [], University = new() };
            var review = new Review() { Rating = 1, Comment = "test", IsAnonymous = false, Course = course, Student = student, Replies = [], Likes = [] };
            var reply = new Reply() { User = student, Review = review, Text = "test" };

            await replyRepository.PostReply(reply);

            Assert.IsTrue(dbContext.Replies.Contains(reply));
        }
    }
}
