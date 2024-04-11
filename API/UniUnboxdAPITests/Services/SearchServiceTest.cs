namespace UniUnboxdAPITests.Services
{
    [TestClass]
    public class SearchServiceTest
    {
        private readonly SearchService searchService;

        public SearchServiceTest()
        {
            var dbContext = DatabaseUtil.CreateDbContext("SearchService");
            var searchRepository = new SearchRepository(dbContext);
            var userRepository = new UserRepository(dbContext);
            searchService = new SearchService(searchRepository, userRepository);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("SearchService");

            var university = new University() { Id = 1 };
            var student = new Student() { Id = 2, UserName = "student", NotificationSettings = new() };
            var course = new Course() { Id = 1, Name = "course", Code = "code", Description = "test", Professor = "professor", Reviews = new List<Review>(), University = university };

            dbContext.Users.Add(university);
            dbContext.Users.Add(student);
            dbContext.Courses.Add(course);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task GetCoursesTestSuccesful()
        {
            var courses = await searchService.GetCourses(new SearchOptions() { Search = "course" });

            Assert.IsNotNull(courses);
            Assert.IsTrue(courses.Any());
        }

        [TestMethod]
        public async Task GetCoursesTestUnsuccesful()
        {
            var courses = await searchService.GetCourses(new SearchOptions() { Search = "test" });

            Assert.IsNotNull(courses);
            Assert.IsFalse(courses.Any());
        }

        [TestMethod]
        public async Task GetCoursesFromUniTestSuccesful()
        {
            var courses = await searchService.GetCourses(new SearchOptions() { UniversityId = 1 });

            Assert.IsNotNull(courses);
            Assert.IsTrue(courses.Any());
        }

        [TestMethod]
        public async Task GetCoursesFromUniTestUnsuccesful()
        {
            var courses = await searchService.GetCourses(new SearchOptions() { UniversityId = 2 });

            Assert.IsNotNull(courses);
            Assert.IsFalse(courses.Any());
        }

        [TestMethod]
        public async Task GetUsersTestSuccesful()
        {
            var users = await searchService.GetUsers(new SearchOptions() { Search = "student" });

            Assert.IsNotNull(users);
            Assert.IsTrue(users.Any());
        }

        [TestMethod]
        public async Task GetUsersTestUnsuccesful()
        {
            var users = await searchService.GetUsers(new SearchOptions() { Search = "test" });

            Assert.IsNotNull(users);
            Assert.IsFalse(users.Any());
        }
    }
}
