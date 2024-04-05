using Microsoft.AspNetCore.Identity;

namespace UniUnboxdAPITests.Services
{
    [TestClass]
    public class RegistrationServiceTest
    {
        private readonly RegistrationService registrationService;
        private readonly UserManager<User> userManager;

        public RegistrationServiceTest()
        {
            var dbContext = DatabaseUtil.CreateDbContext("RegistrationService");
            userManager = DatabaseUtil.CreateUserManager(dbContext);
            var mailService = new MailService(ConfigurationUtil.CreateConfiguration());
            registrationService = new RegistrationService(userManager, mailService);
        }

        [TestMethod]
        public void CreateStudentUserTest()
        {
            var model = new RegisterModel() { Email = "student@gmail.com", Password = "test", Type = UserType.Student };
            var user = registrationService.CreateUser(model);

            Assert.IsInstanceOfType(user, typeof(Student));
            Assert.AreEqual("student", user.UserName);
            Assert.AreEqual("student@gmail.com", user.Email);
            Assert.AreEqual(UserType.Student, user.UserType);
            Assert.IsNotNull(user.SecurityStamp);
            Assert.IsNotNull(user.CreationTime);
            Assert.IsNotNull(user.LastModificationTime);
            Assert.IsNotNull((user as Student)!.NotificationSettings);
        }

        [TestMethod]
        public void CreateUniversityUserTest()
        {
            var model = new RegisterModel() { Email = "university@gmail.com", Password = "test", Type = UserType.University };
            var user = registrationService.CreateUser(model);

            Assert.IsInstanceOfType(user, typeof(University));
            Assert.AreEqual("university", user.UserName);
            Assert.AreEqual("university@gmail.com", user.Email);
            Assert.AreEqual(UserType.University, user.UserType);
            Assert.IsNotNull(user.SecurityStamp);
            Assert.IsNotNull(user.CreationTime);
            Assert.IsNotNull(user.LastModificationTime);
        }

        // TODO: Implement Professor Creation.
        /**
        [TestMethod]
        public void CreateProfessorUserTest()
        {
            var model = new RegisterModel() { Email = "professor@gmail.com", Password = "test", Type = UserType.Professor };
            var user = registrationService.CreateUser(model);

            Assert.IsInstanceOfType(user, typeof(Professor));
            Assert.AreEqual("professor", user.UserName);
            Assert.AreEqual("professor@gmail.com", user.Email);
            Assert.AreEqual(UserType.Professor, user.UserType);
            Assert.IsNotNull(user.SecurityStamp);
            Assert.IsNotNull(user.CreationTime);
            Assert.IsNotNull(user.LastModificationTime);
        }
        */

        [TestMethod]
        public void IsEmailValidTestValid()
        {
            var email = "valid@gmail.com";
            var result = registrationService.IsEmailValid(email);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public void IsEmailValidTestUnvalid()
        {
            var email = "invalid.gmail.com";
            var result = registrationService.IsEmailValid(email);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task DoesEmailExistTestWithNonExistingEmail()
        {
            var email = "nonexisting@gmail.com";
            var result = await registrationService.DoesEmailExist(email);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task DoesEmailExistTestWithExistingEmail()
        {
            // Create account.
            var model = new RegisterModel() { Email = "existing@gmail.com", Password = "test", Type = UserType.Student };
            var user = registrationService.CreateUser(model);
            await userManager.CreateAsync(user);

            var result = await registrationService.DoesEmailExist(model.Email);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task CreateAccountTest()
        {
            var model = new RegisterModel() { Email = "test@gmail.com", Password = "test", Type = UserType.Student };
            var user = registrationService.CreateUser(model);
            var result = await registrationService.CreateAccount(user, model.Password);

            var isCreated = await registrationService.DoesEmailExist(model.Email);

            Assert.IsTrue(result);
            Assert.IsTrue(isCreated);
        }
    }
}