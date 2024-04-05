using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;

namespace UniUnboxdAPITests.Controllers
{
    [TestClass]
    public class AuthenticationControllerTest
    {
        private readonly AuthenticationController authenticationController;
        private readonly UserManager<User> userManager;

        public AuthenticationControllerTest()
        {
            var dbContext = DatabaseUtil.CreateDbContext("AuthenticationController");
            userManager = DatabaseUtil.CreateUserManager(dbContext);
            var userRepository = new UserRepository(dbContext);
            var authenticationService = new AuthenticationService(userManager, userRepository);
            authenticationController = new AuthenticationController(authenticationService);
            JWTConfiguration.Init(ConfigurationUtil.CreateConfiguration());
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("AuthenticationController");
            var userManager = DatabaseUtil.CreateUserManager(dbContext);
            userManager.CreateAsync(new User { Id = 1, UserName = "test", Email = "authentication@gmail.com" }, "test").Wait();
        }

        [TestMethod]
        public async Task AuthenticateTestWithInvalidUser()
        {
            var model = new AuthenticationModel() { Email = "test@gmail.com", Password = "123" };

            ObjectResult result = (ObjectResult)await authenticationController.Authenticate(model);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("User not found.", result.Value);
        }

        [TestMethod]
        public async Task AuthenticateTestWithIncorrectPassword()
        {
            var model = new AuthenticationModel() { Email = "authentication@gmail.com", Password = "123" };

            ObjectResult result = (ObjectResult)await authenticationController.Authenticate(model);

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("The email and password do not align.", result.Value);
        }

        [TestMethod]
        public async Task AuthenticateTestSuccesfully()
        {
            var model = new AuthenticationModel() { Email = "authentication@gmail.com", Password = "test" };

            var result = (ObjectResult)await authenticationController.Authenticate(model);

            Assert.AreEqual(200, result.StatusCode);
            Assert.IsFalse(result.Value!.ToString().IsNullOrEmpty());
        }

        [TestMethod]
        public async Task UpdateTokenTestWithInvalidUser()
        {
            ConfigurationUtil.SetHttpContext(authenticationController, 999, UserType.Student);

            var result = (ObjectResult)await authenticationController.UpdateToken();

            Assert.AreEqual(400, result.StatusCode);
            Assert.AreEqual("User does not exist.", result.Value);
        }

        [TestMethod]
        public async Task UpdateTokenTestWithValidUser()
        {
            ConfigurationUtil.SetHttpContext(authenticationController, 1, UserType.Student);

            var result = (ObjectResult)await authenticationController.UpdateToken();

            Assert.AreEqual(200, result.StatusCode);
            Assert.IsFalse(result.Value!.ToString().IsNullOrEmpty());
        }
    }
}
