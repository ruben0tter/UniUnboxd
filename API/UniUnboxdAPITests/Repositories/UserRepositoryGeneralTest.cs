namespace UniUnboxdAPITests.Repositories
{
    [TestClass]
    public class UserRepositoryGeneralTest
    {
        private readonly UserRepository userRepository;

        public UserRepositoryGeneralTest()
        {
            var dbContext = DatabaseUtil.CreateDbContext("UserRepositoryGeneral");
            userRepository = new UserRepository(dbContext);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("UserRepositoryGeneral");

            var user = new User() { Id = 1, Email = "student@gmail.com" };

            dbContext.Users.Add(user);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task GetUserByIdTest()
        {
            var id = 1;
            var user = await userRepository.GetUser(id);

            Assert.IsNotNull(user);
            Assert.IsInstanceOfType(user, typeof(User));
            Assert.AreEqual(id, user.Id);
            Assert.AreEqual("student@gmail.com", user.Email);
        }

        [TestMethod]
        public async Task GetUserByEmailTest()
        {
            var email = "student@gmail.com";
            var user = await userRepository.GetUser(email);

            Assert.IsNotNull(user);
            Assert.IsInstanceOfType(user, typeof(User));
            Assert.AreEqual(1, user.Id);
            Assert.AreEqual(email, user.Email);
        }

        [TestMethod]
        public async Task SetVerificationStatusTest()
        {
            var user = await userRepository.GetUser(1);
            var verificationStatus = VerificationStatus.Verified;

            await userRepository.SetVerificationStatus(user, verificationStatus);

            Assert.AreEqual(verificationStatus, user.VerificationStatus);
        }
    }
}
