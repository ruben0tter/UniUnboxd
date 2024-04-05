using Microsoft.AspNetCore.Mvc;

namespace UniUnboxdAPITests.Controllers
{
    [TestClass]
    public class SearchControllerTest
    {
        private readonly SearchController searchController;

        public SearchControllerTest()
        {
            var dbContext = DatabaseUtil.CreateDbContext("SearchController");
            var searchRepository = new SearchRepository(dbContext);
            var userRepository = new UserRepository(dbContext);
            var searchService = new SearchService(searchRepository, userRepository);
            searchController = new SearchController(searchService);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("SearchController");

            var university = new University() { Id = 1 };
            var student = new Student() { Id = 2, UserName = "student", NotificationSettings = new() };
            var course = new Course() { Id = 1, Name = "course", Code = "code", Description = "test", Professor = "professor", Reviews = new List<Review>(), University = university };

            dbContext.Users.Add(university);
            dbContext.Users.Add(student);
            dbContext.Courses.Add(course);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task SearchCoursesTestWithEmptySearch()
        {
            ConfigurationUtil.SetHttpContext(searchController, 1, UserType.University);

            var searchOptions = new SearchOptions();
            var result = (ObjectResult)await searchController.SearchCourses(searchOptions);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Search query cannot be empty", result.Value);
        }

        [TestMethod]
        public async Task SearchCoursesTest()
        {
            ConfigurationUtil.SetHttpContext(searchController, 2, UserType.Student);

            var searchOptions = new SearchOptions() { Search = "course" };
            var result = (ObjectResult)await searchController.SearchCourses(searchOptions);
            var courses = result.Value as List<CourseSearchModel>;

            Assert.AreEqual(200, result.StatusCode);
            Assert.IsNotNull(courses);
            Assert.IsTrue(courses!.Any());
        }

        [TestMethod]
        public async Task SearchCoursesFromUniTest()
        {
            ConfigurationUtil.SetHttpContext(searchController, 1, UserType.University);

            var searchOptions = new SearchOptions() { Search = "course" };
            var result = (ObjectResult)await searchController.SearchCourses(searchOptions);
            var courses = result.Value as List<CourseSearchModel>;

            Assert.AreEqual(200, result.StatusCode);
            Assert.IsNotNull(courses);
            Assert.IsTrue(courses!.Any());
            Assert.IsTrue(courses.Any(i => i.UniversityId == 1));
        }

        [TestMethod]
        public async Task SearchUsersTestWithEmptySearch()
        {
            ConfigurationUtil.SetHttpContext(searchController, 2, UserType.Student);

            var searchOptions = new SearchOptions();
            var result = (ObjectResult)await searchController.SearchUsers(searchOptions);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("Search query cannot be empty", result.Value);
        }

        [TestMethod]
        public async Task SearchUsersTest()
        {
            ConfigurationUtil.SetHttpContext(searchController, 2, UserType.Student);

            var searchOptions = new SearchOptions() { Search = "student" };
            var result = (ObjectResult)await searchController.SearchUsers(searchOptions);
            var users = result.Value as List<UserSearchModel>;

            Assert.AreEqual(200, result.StatusCode);
            Assert.IsNotNull(users);
            Assert.IsTrue(users!.Any());
        }
    }
}
