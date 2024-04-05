using UniUnboxdAPI.Models.DataTransferObjects.ReviewPage;

namespace UniUnboxdAPITests.Services
{
    [TestClass]
    public class ReplyServiceTest
    {
        private readonly UniUnboxdDbContext dbContext;
        private readonly ReplyService replyService;

        public ReplyServiceTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("ReplyService");
            var replyRepository = new ReplyRepository(dbContext);
            var reviewRepository = new ReviewRepository(dbContext);
            var userRepository = new UserRepository(dbContext);
            var mailService = new MailService(ConfigurationUtil.CreateConfiguration());
            var pushNotificationService = new PushNotificationService();
            replyService = new ReplyService(replyRepository, reviewRepository, userRepository, mailService, pushNotificationService);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("ReplyService");

            var student = new Student() { Id = 1, Following = new List<Follow>(), NotificationSettings = new() };
            var university = new University() { Id = 2 };
            var course = new Course()
            {
                Id = 1,
                Name = "course",
                Code = "code",
                Description = "description",
                Professor = "professor",
                Reviews = [],
                University = university
            };
            var review = new Review()
            {
                Id = 1,
                Rating = 5,
                Comment = "test",
                IsAnonymous = false,
                Course = course,
                Student = student,
                Likes = new List<Like>(),
                Replies = new List<Reply>()
            };
            var reply = new Reply() { Id = 1, Review = review, User = student, Text = "test" };

            dbContext.Users.Add(student);
            dbContext.Users.Add(university);
            dbContext.Courses.Add(course);
            dbContext.Reviews.Add(review);
            dbContext.Replies.Add(reply);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task DoesUserExistTestWithExistingUser()
        {
            var id = 1;
            var result = await replyService.DoesUserExist(id);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesUserExistTestWithNonExistingUser()
        {
            var id = 999;
            var result = await replyService.DoesUserExist(id);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task DoesReviewExistTest_ExistingReview()
        {
            int reviewId = 1;
            bool exists = await replyService.DoesReviewExist(reviewId);
            Assert.IsTrue(exists);
        }

        [TestMethod]
        public async Task DoesReviewExistTest_NonExistingReview()
        {
            int reviewId = 999;
            bool exists = await replyService.DoesReviewExist(reviewId);
            Assert.IsFalse(exists);
        }

        [TestMethod]
        public async Task CreateReplyTest()
        {
            var model = new ReplyModel() { Text = "reply", ReviewId = 1 };
            var userId = 1;

            var reply = await replyService.CreateReply(model, userId);

            Assert.IsNotNull(reply);
            Assert.IsNotNull(reply.User);
            Assert.IsNotNull(reply.Review);
            Assert.AreEqual(model.Text, reply.Text);
            Assert.AreEqual(userId, reply.User.Id);
            Assert.AreEqual(model.ReviewId, reply.Review.Id);
        }

        [TestMethod]
        public async Task PostReplyTest()
        {
            var student = new Student() { NotificationSettings = new() };
            var course = new Course() { Name = "course", Code = "code", Description = "test", Professor = "professor", Reviews = [], University = new() };
            var review = new Review() { Rating = 1, Comment = "test", IsAnonymous = false, Course = course, Student = student, Replies = [], Likes = [] };
            var reply = new Reply() { User = student, Review = review, Text = "test" };

            await replyService.PostReply(reply);

            Assert.IsTrue(dbContext.Replies.Contains(reply));
        }

        [TestMethod]
        public async Task CreateReviewReplyModelTest()
        {
            var reply = await dbContext.Replies.Include(i => i.User).FirstAsync(i => i.Id == 1);

            var model = replyService.CreateReviewReplyModel(reply);

            Assert.IsNotNull(model);
        }
    }
}
