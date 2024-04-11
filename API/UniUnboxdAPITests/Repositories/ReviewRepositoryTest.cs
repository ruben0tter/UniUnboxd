namespace UniUnboxdAPITests.Repositories
{
    [TestClass]
    public class ReviewRepositoryTest
    {
        private readonly ReviewRepository reviewRepository;
        private readonly UniUnboxdDbContext dbContext;

        public ReviewRepositoryTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("ReviewRepository");
            reviewRepository= new ReviewRepository(dbContext);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("ReviewRepository");

            var student = new Student() { Id = 1, Following = new List<Follow>(), NotificationSettings = new() };
            var friend = new Student() { Id = 2, Followers = new List<Follow>(), NotificationSettings = new() };
            var university = new University() { Id = 3 };
            var course = new Course() { Id = 1, Name = "course", Code = "code", Description = "description", Professor = "professor", 
                Reviews = [], University = university };
            var course1 = new Course() { Id = 2, Name = "course", Code = "code", Description = "description", Professor = "professor", 
                Reviews = [], University = university };
            var review = new Review() { Id = 1, Rating = 5, Comment = "test", IsAnonymous = false, 
                Course = course, Student = friend, Likes = new List<Like>(), Replies = new List<Reply>() };
            var review1 = new Review() { Id = 2, Rating = 5, Comment = "test", IsAnonymous = false, 
                Course = course1, Student = friend, Likes = new List<Like>(), Replies = new List<Reply>() };
            var review2 = new Review() { Id = 3, Rating = 5, Comment = "test", IsAnonymous = false, 
                Course = course1, Student = student, Likes = new List<Like>(), Replies = new List<Reply>() };

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
            dbContext.Reviews.Add(review);
            dbContext.Reviews.Add(review1);
            dbContext.Reviews.Add(review2);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task DoesReviewExistTest_ExistingReview()
        {
            int reviewId = 1;
            bool exists = await reviewRepository.DoesReviewExist(reviewId);
            Assert.IsTrue(exists);
        }

        [TestMethod]
        public async Task DoesReviewExistTest_NonExistingReview()
        {
            int reviewId = 999;
            bool exists = await reviewRepository.DoesReviewExist(reviewId);
            Assert.IsFalse(exists);
        }

        [TestMethod]
        public async Task HasStudentAlreadyReviewedCourseTest_AlreadyReviewed()
        {
            int studentId = 2;
            int courseId = 1;
            bool hasReviewed = await reviewRepository.HasStudentAlreadyReviewedCourse(studentId, courseId);
            Assert.IsTrue(hasReviewed);
        }

        [TestMethod]
        public async Task HasStudentAlreadyReviewedCourseTest_NotAlreadyReviewed()
        {
            int studentId = 1;
            int courseId = 1;
            bool hasReviewed = await reviewRepository.HasStudentAlreadyReviewedCourse(studentId, courseId);
            Assert.IsFalse(hasReviewed);
        }

        [TestMethod]
        public async Task GetReviewTest()
        {
            int reviewId = 1;
            var review = await reviewRepository.GetReview(reviewId);
            Assert.IsNotNull(review);
            Assert.AreEqual(reviewId, review.Id);
        }

        [TestMethod]
        public async Task GetAllFriendsThatReviewedTest()
        {
            int studentId = 1;
            int courseId = 1;
            var reviews = await reviewRepository.GetAllFriendsThatReviewed(studentId, courseId);
            Assert.IsNotNull(reviews);
            Assert.IsTrue(reviews.Any());
        }

        [TestMethod]
        public async Task GetReviewAndConnectedDataTest()
        {
            int reviewId = 1;
            var review = await reviewRepository.GetReviewAndConnectedData(reviewId);
            Assert.IsNotNull(review);
            Assert.AreEqual(reviewId, review.Id);
            Assert.IsNotNull(review.Student);
            Assert.IsNotNull(review.Student.NotificationSettings);
            Assert.IsNotNull(review.Course);
            Assert.IsNotNull(review.Replies);
            Assert.IsTrue(review.Replies.All(i => i.User != null));
            Assert.IsNotNull(review.Likes);
            Assert.IsTrue(review.Likes.All(i => i.Student != null));
        }

        [TestMethod]
        public async Task GetCourseReviewByStudentTest()
        {
            int courseId = 1;
            int studentId = 2;
            var review = await reviewRepository.GetCourseReviewByStudent(courseId, studentId);
            Assert.IsNotNull(review);
        }

        [TestMethod]
        public async Task GetLatestReviewsByFriendsTest()
        {
            int studentId = 1;
            var reviews = await reviewRepository.GetLatestReviewsByFriends(studentId);
            Assert.IsNotNull(reviews);
            Assert.IsTrue(reviews.Any());
        }

        [TestMethod]
        public async Task PostReviewTest()
        {
            var student = dbContext.Students.First(i => i.Id == 2);
            var university = dbContext.Universities.First(i => i.Id == 3);
            var course = new Course() { Id = 3, Name = "course", Code = "code", Description = "description", 
                Professor = "professor", Reviews = [], University = university };
            var review = new Review()
            {
                Id = 4,
                Rating = 5,
                Comment = "test",
                IsAnonymous = false,
                Course = course,
                Student = student,
                Replies = new List<Reply>(),
                Likes = new List<Like>()
            };

            await reviewRepository.PostReview(review);

            Assert.IsTrue(await reviewRepository.DoesReviewExist(review.Id));
        }

        [TestMethod]
        public async Task GetNextReviewsForCourseTest()
        {
            var startingId = 0;
            var reviews = await reviewRepository.GetNextReviewsForCourse(startingId, 1, 5);

            Assert.IsNotNull(reviews);
            Assert.IsTrue(reviews.All(i => i.Id > startingId));
        }

        [TestMethod]
        public async Task PutReviewTest()
        {
            var review = dbContext.Reviews.First(i => i.Id == 1);
            review.Comment = "Updated Review Content";

            await reviewRepository.PutReview(review);
            var updatedReview = await reviewRepository.GetReview(review.Id);

            Assert.AreEqual("Updated Review Content", updatedReview.Comment);
        }

        [TestMethod]
        public async Task IsReviewWrittenByStudentTest()
        {
            int reviewId = 1;
            int studentId = 2;
            bool isWrittenByStudent = await reviewRepository.IsReviewWrittenByStudent(reviewId, studentId);
            Assert.IsTrue(isWrittenByStudent);
        }

        [TestMethod]
        public async Task DoesStudentLikeReviewTest()
        {
            int reviewId = 1;
            int studentId = 1;
            bool doesLike = await reviewRepository.DoesStudentLikeReview(reviewId, studentId);
            Assert.IsTrue(doesLike);
        }

        [TestMethod]
        public async Task LikeReviewTest()
        {
            int reviewId = 2;
            int studentId = 1;

            await reviewRepository.LikeReview(reviewId, studentId);
            Assert.IsTrue(await reviewRepository.DoesStudentLikeReview(reviewId, studentId));
        }

        [TestMethod]
        public async Task UnlikeReviewTest()
        {
            int reviewId = 1;
            int studentId = 2;

            await reviewRepository.UnlikeReview(reviewId, studentId);
            Assert.IsFalse(await reviewRepository.DoesStudentLikeReview(reviewId, studentId));
        }

        [TestMethod]
        public async Task DeleteReviewTest()
        {
            var review = await dbContext.Reviews.FirstAsync(i => i.Id == 3);
            await reviewRepository.DeleteReview(review);
            Assert.IsFalse(await reviewRepository.DoesReviewExist(review.Id));
        }
    }
}
