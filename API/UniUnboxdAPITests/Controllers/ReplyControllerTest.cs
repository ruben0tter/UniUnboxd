using Microsoft.AspNetCore.Mvc;
using UniUnboxdAPI.Models.DataTransferObjects.ReviewPage;

namespace UniUnboxdAPITests.Controllers
{
    [TestClass]
    public class ReplyControllerTest
    {
        private readonly ReplyController replyController;
        private readonly ReplyService replyService;
        private readonly UniUnboxdDbContext dbContext;

        public ReplyControllerTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("ReplyController");
            var replyRepository = new ReplyRepository(dbContext);
            var reviewRepository = new ReviewRepository(dbContext);
            var userRepository = new UserRepository(dbContext);
            var mailService = new MailService(ConfigurationUtil.CreateConfiguration());
            var pushNotificationService = new PushNotificationService();
            replyService = new ReplyService(replyRepository, reviewRepository, userRepository, mailService, pushNotificationService);
            replyController = new ReplyController(replyService);
        }
        
        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("ReplyController");

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
        public async Task PostReplyTestWithInvalidReviewId()
        {
            ConfigurationUtil.SetHttpContext(replyController, 1, UserType.Student);
            var model = new ReplyModel() { Text = "", ReviewId = 0};
            ObjectResult result = (ObjectResult)await replyController.PostReply(model);
            Assert.AreEqual("Given review does not exist.", result.Value);
            Assert.AreEqual(400, result.StatusCode);
            
        }
        
        [TestMethod]
        public async Task PostReplyTestWithInvalidUserId()
        {
            ConfigurationUtil.SetHttpContext(replyController, 999, UserType.Student);
            var model = new ReplyModel() { Text = "", ReviewId = 1};
            ObjectResult result = (ObjectResult)await replyController.PostReply(model);
            Assert.AreEqual("Given user does not exist.", result.Value);
            Assert.AreEqual(400, result.StatusCode);
            
        }
        
        [TestMethod]
        public async Task PostReplyTestSuccessful()
        {
            ConfigurationUtil.SetHttpContext(replyController, 1, UserType.Student);
            var model = new ReplyModel() { Text = "new reply", ReviewId = 1 };
            ObjectResult result = (ObjectResult)await replyController.PostReply(model);
            
            Assert.IsTrue(dbContext.Replies.Any(i => i.Text == model.Text));
            Assert.AreEqual(200, result.StatusCode);
        }
    }
}
