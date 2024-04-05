namespace UniUnboxdAPITests.Services
{
    [TestClass]
    public class UserServiceUniversityTest
    {
        private readonly UniUnboxdDbContext dbContext;
        private readonly UserService userService;

        public UserServiceUniversityTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("UserServiceUniversity");
            var userRepository = new UserRepository(dbContext);
            var courseRepository = new CourseRepository(dbContext);
            var mailService = new MailService(ConfigurationUtil.CreateConfiguration());
            var pushNotificationService = new PushNotificationService();
            userService = new UserService(userRepository, courseRepository, mailService, pushNotificationService);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("UserServiceUniversity");

            var university = new University() { Id = 1, Email = "university@gmail.com" };
            var university1 = new University() { Id = 2, Email = "university1@gmail.com" };

            dbContext.Users.Add(university);
            dbContext.Users.Add(university1);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task GetUniversitiesTest()
        {
            var universities = await userService.GetUniversities();

            Assert.IsNotNull(universities);
            Assert.IsTrue(universities.Any());
            Assert.IsTrue(universities.Any(i => i.Id == 1));
            Assert.IsTrue(universities.Any(i => i.Id == 2));
        }
    }
}
