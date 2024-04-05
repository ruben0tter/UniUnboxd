using Microsoft.AspNetCore.Mvc;
using UniUnboxdAPI.Models.DataTransferObjects.ReviewPage;
using UniUnboxdAPI.Models.DataTransferObjects.StudentHomePage;

namespace UniUnboxdAPITests.Controllers
{
    [TestClass]
    public class ReviewControllerTest
    {
        private readonly UniUnboxdDbContext dbContext;
        private readonly ReviewController reviewController;

        public ReviewControllerTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("ReviewController");
            var reviewRepository = new ReviewRepository(dbContext);
            var courseRepository = new CourseRepository(dbContext);
            var userRepository = new UserRepository(dbContext);
            var mailService = new MailService(ConfigurationUtil.CreateConfiguration());
            var pushNotificationService = new PushNotificationService();
            var reviewService = new ReviewService(reviewRepository, courseRepository, userRepository, mailService, pushNotificationService);
            reviewController = new ReviewController(reviewService);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("ReviewController");

            var student = new Student() { Id = 1, Email = "student@gmail.com", Following = new List<Follow>(), NotificationSettings = new() };
            var friend = new Student() { Id = 2, Email = "student1@gmail.com", Followers = new List<Follow>(), NotificationSettings = new() };
            var university = new University() { Id = 3 };
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
            var course1 = new Course()
            {
                Id = 2,
                Name = "course",
                Code = "code",
                Description = "description",
                Professor = "professor",
                Reviews = [],
                University = university
            };
            var course2 = new Course()
            {
                Id = 3,
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
                Student = friend,
                Likes = new List<Like>(),
                Replies = new List<Reply>()
            };
            var review1 = new Review()
            {
                Id = 2,
                Rating = 5,
                Comment = "test",
                IsAnonymous = false,
                Course = course1,
                Student = friend,
                Likes = new List<Like>(),
                Replies = new List<Reply>()
            };
            var review2 = new Review()
            {
                Id = 3,
                Rating = 5,
                Comment = "test",
                IsAnonymous = false,
                Course = course1,
                Student = student,
                Likes = new List<Like>(),
                Replies = new List<Reply>()
            };

            var follow = new Follow() { FollowingStudent = student, FollowedStudent = friend };
            student.Following.Add(follow);
            friend.Followers.Add(follow);

            var like = new Like() { Review = review, Student = student };
            review.Likes.Add(like);
            var like1 = new Like() { Review = review, Student = friend };
            review.Likes.Add(like1);

            dbContext.Users.Add(student);
            dbContext.Users.Add(friend);
            dbContext.Users.Add(university);
            dbContext.Courses.Add(course);
            dbContext.Courses.Add(course1);
            dbContext.Courses.Add(course2);
            dbContext.Reviews.Add(review);
            dbContext.Reviews.Add(review1);
            dbContext.Reviews.Add(review2);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task GetReviewTestWithInvalidId()
        {
            int reviewId = 999;
            var result = (ObjectResult)await reviewController.GetReview(reviewId);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Given review does not exist.", result.Value);
        }

        [TestMethod]
        public async Task GetReviewTestSuccessful()
        {
            int reviewId = 1;
            var result = (ObjectResult)await reviewController.GetReview(reviewId);
            var review = result.Value as ReviewPageModel;

            Assert.AreEqual(200, result.StatusCode);
            Assert.AreEqual(reviewId, review.Id);
        }

        [TestMethod]
        public async Task GetNextReviewsTestUnsuccesful()
        {
            int courseId = 1;
            var result = (ObjectResult)await reviewController.GetNextReviewsForCourse(999, courseId, 10);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual($"No review with id bigger than 999 exists", result.Value);
        }

        [TestMethod]
        public async Task GetNextReviewsTestSuccessful()
        {
            int courseId = 1;
            var result = (ObjectResult)await reviewController.GetNextReviewsForCourse(0, courseId, 10);
            var reviews = result.Value as List<CourseReviewModel>;

            Assert.AreEqual(200, result.StatusCode);
            Assert.IsTrue(reviews!.Any());
            Assert.IsTrue(reviews.All(i => i.CourseId == courseId));
        }

        [TestMethod]
        public async Task GetLatestReviewsByFriendsTest()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 1, UserType.Student);

            var result = (ObjectResult)await reviewController.GetLatestReviewsByFriends();
            var reviews = result.Value as List<ReviewGridModel>;

            Assert.AreEqual(200, result.StatusCode);
            Assert.IsTrue(reviews!.Any());
        }

        [TestMethod]
        public async Task PostReviewTestWithInvalidStudent()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 999, UserType.Student);

            var model = new ReviewModel() { Rating = 5, Comment = "test", IsAnonymous = false, CourseId = 1 };
            var result = (ObjectResult)await reviewController.PostReview(model);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Given student does not exist.", result.Value);
        }

        [TestMethod]
        public async Task PostReviewTestWithInvalidCourse()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 1, UserType.Student);

            var model = new ReviewModel() { Rating = 5, Comment = "test", IsAnonymous = false, CourseId = 999 };
            var result = (ObjectResult)await reviewController.PostReview(model);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Given course does not exist.", result.Value);
        }

        [TestMethod]
        public async Task PostReviewTestWithStudentHavingAlreadyReviewedCourse()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 2, UserType.Student);

            var model = new ReviewModel() { Rating = 5, Comment = "test", IsAnonymous = false, CourseId = 1 };
            var result = (ObjectResult)await reviewController.PostReview(model);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Student has already reviewed provided course.", result.Value);
        }

        [TestMethod]
        public async Task PostReviewTestSuccessful()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 2, UserType.Student);

            var model = new ReviewModel() { Rating = 5, Comment = "test", IsAnonymous = false, CourseId = 3 };
            var result = (ObjectResult)await reviewController.PostReview(model);

            Assert.AreEqual(200, result.StatusCode);
            Assert.IsTrue(dbContext.Reviews.Any(i => i.Student.Id == 2 && i.Course.Id == 3));
        }

        [TestMethod]
        public async Task FlagReviewTestWithInvalidReview()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 1, UserType.Student);

            var model = new FlagReviewModel() { ReviewId = 999, Message = string.Empty };
            var result = (ObjectResult)await reviewController.FlagReview(model);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Given review does not exist.", result.Value);
        }

        [TestMethod]
        public async Task FlagReviewTestSuccessful()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 1, UserType.Student);

            var model = new FlagReviewModel() { ReviewId = 1, Message = string.Empty };
            var result = (ObjectResult)await reviewController.FlagReview(model);

            Assert.AreEqual(200, result.StatusCode);
        }

        [TestMethod]
        public async Task PutReviewTestWithInvalidStudent()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 999, UserType.Student);

            var model = new ReviewModel() { Rating = 5, Comment = "test", IsAnonymous = false, CourseId = 1 };
            var result = (ObjectResult)await reviewController.PutReview(1, model);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Given student does not exist.", result.Value);
        }

        [TestMethod]
        public async Task PutReviewTestWithInvalidCourse()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 1, UserType.Student);

            var model = new ReviewModel() { Rating = 5, Comment = "test", IsAnonymous = false, CourseId = 999 };
            var result = (ObjectResult)await reviewController.PutReview(1, model);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Given course does not exist.", result.Value);
        }

        [TestMethod]
        public async Task PutReviewTestSuccessful()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 2, UserType.Student);

            var reviewId = 1;
            var model = new ReviewModel() { Rating = 5, Comment = "new comment", IsAnonymous = false, CourseId = 1 };
            var result = (ObjectResult)await reviewController.PutReview(reviewId, model);

            Assert.AreEqual(200, result.StatusCode);
            Assert.IsTrue(dbContext.Reviews.Any(i => i.Student.Id == 2 && i.Course.Id == 1 && i.Comment == model.Comment ));
        }

        [TestMethod]
        public async Task LikeReviewTestWithInvalidReview()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 1, UserType.Student);

            var result = (ObjectResult)await reviewController.LikeReview(999);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Given review does not exist.", result.Value);
        }

        [TestMethod]
        public async Task LikeReviewTestWithInvalidStudent()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 999, UserType.Student);

            var result = (ObjectResult)await reviewController.LikeReview(1);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Given student does not exist.", result.Value);
        }

        [TestMethod]
        public async Task LikeReviewTestWithOwnReview()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 2, UserType.Student);

            var result = (ObjectResult)await reviewController.LikeReview(1);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("You can not like your own review.", result.Value);
        }

        [TestMethod]
        public async Task LikeReviewTestWithAlreadyLiked()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 1, UserType.Student);

            var result = (ObjectResult)await reviewController.LikeReview(1);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Given review is already liked.", result.Value);
        }

        [TestMethod]
        public async Task LikeReviewTestSuccessful()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 1, UserType.Student);

            var result = (OkResult)await reviewController.LikeReview(2);

            Assert.AreEqual(200, result.StatusCode);
        }

        [TestMethod]
        public async Task UnlikeReviewTestWithInvalidReview()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 1, UserType.Student);

            var result = (ObjectResult)await reviewController.UnlikeReview(999);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Given review does not exist.", result.Value);
        }

        [TestMethod]
        public async Task UnlikeReviewTestWithInvalidStudent()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 999, UserType.Student);

            var result = (ObjectResult)await reviewController.UnlikeReview(1);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Given student does not exist.", result.Value);
        }

        [TestMethod]
        public async Task UnlikeReviewTestWithOwnReview()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 2, UserType.Student);

            var result = (ObjectResult)await reviewController.UnlikeReview(1);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("You can not unlike your own review.", result.Value);
        }

        [TestMethod]
        public async Task UnlikeReviewTestWithAlreadyLiked()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 1, UserType.Student);

            var result = (ObjectResult)await reviewController.UnlikeReview(2);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Given review is not liked.", result.Value);
        }

        [TestMethod]
        public async Task UnlikeReviewTestSuccessful()
        {
            ConfigurationUtil.SetHttpContext(reviewController, 1, UserType.Student);

            var result = (OkResult)await reviewController.UnlikeReview(1);

            Assert.AreEqual(200, result.StatusCode);
        }
    }
}
