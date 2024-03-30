using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
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
            var dbContext = DatabaseUtil.CreateDbContext();
            userManager = DatabaseUtil.CreateUserManager(dbContext);
            var userRepository = new UserRepository(dbContext);
            authenticationService = new AuthenticationService(userManager, userRepository);
            JWTConfiguration.Init(ConfigurationUtil.CreateConfiguration());

            CreateTestUser().Wait();
        }

        private async Task CreateTestUser()
        {
            var email = "authentication@gmail.com";

            if (!await userManager.Users.AnyAsync(i => i.Email == email))
                await userManager.CreateAsync(new User { UserName = "test", Email = email }, "test");

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
            var id = (await userManager.FindByEmailAsync("authentication@gmail.com"))!.Id;
            var user = await authenticationService.GetUser(id);

            Assert.IsNotNull(user);
            Assert.AreEqual(user.Id, id);
            Assert.AreEqual(user.Email, "authentication@gmail.com");
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
