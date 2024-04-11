using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace UniUnboxdAPITests.Controllers
{
    [TestClass]
    public class RegistrationControllerTest
    {
        private readonly RegistrationController registrationController;
        private readonly RegistrationService registrationService;
        private readonly UserManager<User> userManager;

        public RegistrationControllerTest()
        {
            var dbContext = DatabaseUtil.CreateDbContext("RegistrationController");
            userManager = DatabaseUtil.CreateUserManager(dbContext);
            var mailService = new MailService(ConfigurationUtil.CreateConfiguration());
            registrationService = new RegistrationService(userManager, mailService);
            registrationController = new RegistrationController(registrationService);
        }

        [TestMethod]
        public async Task RegistrateTestWithnInvalidEmail()
        {
            var invalidEmailModel = new RegisterModel() { Email = "studentgmail.com", Password = "test", Type = UserType.Student };
            ObjectResult result = (ObjectResult)await registrationController.Registrate(invalidEmailModel);
            
            Assert.AreEqual("A non-valid email was submitted.", result.Value);
            Assert.AreEqual(400, result.StatusCode);
        }
        
        [TestMethod]
        public async Task RegistrateTestWithAlreadyExistingEmail()
        {
            var model = new RegisterModel() { Email = "student@gmail.com", Password = "test", Type = UserType.Student };
            var user = registrationService.CreateUser(model);
            await userManager.CreateAsync(user);
            var existentEmailModel = new RegisterModel() { Email = "student@gmail.com", Password = "test", Type = UserType.Student };
            ObjectResult result = (ObjectResult)await registrationController.Registrate(existentEmailModel);
            
            Assert.AreEqual("An account with the same email already exists.", result.Value);
            Assert.AreEqual(400, result.StatusCode);
        }
        
        [TestMethod]
        public async Task RegistrateTestSuccessful()
        {
            
            var creatingModel = new RegisterModel() { Email = "student2@gmail.com", Password = "test", Type = UserType.Student };
            ObjectResult result = (ObjectResult)await registrationController.Registrate(creatingModel);
            
            Assert.AreEqual("Account created successfully.", result.Value);
            Assert.AreEqual(200, result.StatusCode);
        }
    }
}
