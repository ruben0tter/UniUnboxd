namespace UniUnboxdAPITests.Repositories
{
    [TestClass]
    public class SearchRepositoryTest
    {
        private readonly SearchRepository searchRepository;

        public SearchRepositoryTest()
        {
            var dbContext = DatabaseUtil.CreateDbContext("SearchRepository");
            searchRepository = new SearchRepository(dbContext);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("SearchRepository");

            var university = new University() { Id = 1 };
            var student = new University() { Id = 2, UserName = "student" };
            var course = new Course() { Id = 1, Name = "course", Code = "code", Description = "test", Professor = "professor", Reviews = new List<Review>(), University = university };

            dbContext.Users.Add(university); 
            dbContext.Users.Add(student);
            dbContext.Courses.Add(course);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task GetCoursesTestSuccesful() 
        {
            var courses = await searchRepository.GetCourses(new SearchOptions() { Search = "course" });

            Assert.IsNotNull(courses);
            Assert.IsTrue(courses.Count != 0);
        }

        [TestMethod]
        public async Task GetCoursesTestUnsuccesful()
        {
            var courses = await searchRepository.GetCourses(new SearchOptions() { Search = "test" });

            Assert.IsNotNull(courses);
            Assert.IsFalse(courses.Count != 0);
        }

        [TestMethod]
        public async Task GetCoursesFromUniTestSuccesful()
        {
            var courses = await searchRepository.GetCoursesFromUni(new SearchOptions() { UniversityId = 1 });

            Assert.IsNotNull(courses);
            Assert.IsTrue(courses.Count > 0);
        }

        [TestMethod]
        public async Task GetCoursesFromUniTestUnsuccesful()
        {
            var courses = await searchRepository.GetCoursesFromUni(new SearchOptions() { UniversityId = 2 });

            Assert.IsNotNull(courses);
            Assert.IsTrue(courses.Count == 0);
        }

        [TestMethod]
        public async Task GetUsersTestSuccesful()
        {
            var users = await searchRepository.GetUsers(new SearchOptions() { Search = "student" });

            Assert.IsNotNull(users);
            Assert.IsTrue(users.Count > 0);
        }

        [TestMethod]
        public async Task GetUsersTestUnsuccesful()
        {
            var users = await searchRepository.GetUsers(new SearchOptions() { Search = "test" });

            Assert.IsNotNull(users);
            Assert.IsTrue(users.Count == 0);
        }
    }
}
