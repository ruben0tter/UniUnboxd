using Microsoft.AspNetCore.Identity;
using UniUnboxdAPITests.TestUtilities;

namespace UniUnboxdAPITests.Services
{
    [TestClass]
    public class AuthenticationServiceTest
    {
        private readonly AuthenticationService authenticationService;
        private readonly UserManager<User> userManager;

        public AuthenticationServiceTest()
        {
            var dbContext = DatabaseUtil.CreateDbContext("AuthenticationService");
            userManager = DatabaseUtil.CreateUserManager(dbContext);
            var userRepository = new UserRepository(dbContext);
            authenticationService = new AuthenticationService(userManager, userRepository);
            JWTConfiguration.Init(ConfigurationUtil.CreateConfiguration());
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("AuthenticationService");
            var userManager = DatabaseUtil.CreateUserManager(dbContext);
            userManager.CreateAsync(new User { UserName = "test", Email = "authentication@gmail.com" }, "test").Wait();
        }

        [TestMethod]
        public async Task DoesEmailExistTestWithNonExistingEmail()
        {
            var email = "nonexisting@gmail.com";
            var result = await authenticationService.DoesEmailExist(email);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task DoesEmailExistTestWithExistingEmail()
        {
            var email = "authentication@gmail.com";
            var result = await authenticationService.DoesEmailExist(email);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task AuthenticateTestSuccesful()
        {
            var model = new AuthenticationModel() { Email = "authentication@gmail.com", Password = "test" };
            var result = await authenticationService.Authenticate(model);

            Assert.IsNotNull(result);
        }

        [TestMethod]
        public async Task AuthenticateTestunsuccesful()
        {
            var model = new AuthenticationModel() { Email = "authentication@gmail.com", Password = "123" };
            var result = await authenticationService.Authenticate(model);

            Assert.IsNull(result);
        }

        [TestMethod]
        public async Task GetUserTest()
        {
            var user = await authenticationService.GetUser(1);

            Assert.IsNotNull(user);
            Assert.AreEqual(1, user.Id);
            Assert.AreEqual("authentication@gmail.com", user.Email);
        }

        [TestMethod]
        public async Task UpdateTokenTest()
        {
            var user = await userManager.FindByEmailAsync("authentication@gmail.com");
            var token = authenticationService.UpdateToken(user!);

            Assert.IsNotNull(token);
        }
    }
}
