namespace UniUnboxdAPITests.Repositories
{
    [TestClass]
    public class UserRepositoryUniversityTest
    {
        private readonly UserRepository userRepository;

        public UserRepositoryUniversityTest()
        {
            var dbContext = DatabaseUtil.CreateDbContext("UserRepositoryUniversity");
            userRepository = new UserRepository(dbContext);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("UserRepositoryUniversity");

            var university = new University() { Id = 1, Email = "university@gmail.com" };
            var university1 = new University() { Id = 2, Email = "university1@gmail.com" };

            dbContext.Users.Add(university);
            dbContext.Users.Add(university1);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task GetImageOfUniversityTest()
        {
            var image = await userRepository.GetImageOf(0, UserType.University);

            Assert.IsNull(image);
        }

        [TestMethod]
        public async Task DoesUniversityExistTestWithExistingUniversity()
        {
            var id = 1;
            var result = await userRepository.DoesUniversityExist(id);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesUniversityExistTestWithNonExistingUniversity()
        {
            var id = 3;
            var result = await userRepository.DoesUniversityExist(id);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task GetUniversityTest()
        {
            var id = 1;
            var university = await userRepository.GetUniversity(id);

            Assert.IsNotNull(university);
            Assert.IsInstanceOfType(university, typeof(University));
            Assert.AreEqual(id, university.Id);
            Assert.AreEqual("university@gmail.com", university.Email);
        }

        [TestMethod]
        public async Task GetUniversitiesTest()
        {
            var universities = await userRepository.GetUniversities();

            Assert.IsNotNull(universities);
            Assert.IsTrue(universities.Any());
            Assert.IsTrue(universities.Any(i => i.Id == 1));
            Assert.IsTrue(universities.Any(i => i.Id == 2));
        }
    }
}
