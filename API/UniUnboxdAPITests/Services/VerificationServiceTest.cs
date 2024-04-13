namespace UniUnboxdAPITests.Services
{
    [TestClass]
    public class VerificationServiceTest
    {
        private readonly UniUnboxdDbContext dbContext;
        private readonly VerificationService verificationService;

        public VerificationServiceTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("VerificationService");
            var verificationRepository = new VerificationRepository(dbContext);
            var userRepository = new UserRepository(dbContext);
            var mailService = new MailService(ConfigurationUtil.CreateConfiguration());
            var pushNotificationService = new PushNotificationService();
            verificationService = new VerificationService(verificationRepository, userRepository, mailService, pushNotificationService);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("VerificationService");

            var university = new University { Id = 1, Email = "university@gmail.com" };
            var student = new Student { Id = 2, Email = "student@gmail.com", NotificationSettings = new NotificationSettings() };
            var student1 = new Student { Id = 3, Email = "student1@gmail.com", NotificationSettings = new NotificationSettings() };
            var application = new VerificationApplication { UserId = 1, VerificationData = ["data"] };
            var application1 = new VerificationApplication { UserId = 2, VerificationData = ["data"], TargetUniversity = university };
            var application2 = new VerificationApplication { UserId = 2, VerificationData = ["data"], TargetUniversity = university };

            dbContext.Users.Add(university);
            dbContext.Users.Add(student);
            dbContext.Users.Add(student1);
            dbContext.Applications.Add(application);
            dbContext.Applications.Add(application1);
            dbContext.Applications.Add(application2);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task DoesUniversityExistTestWithExistingUniversity()
        {
            var id = 1;
            var result = await verificationService.DoesUniversityExist(id);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesUniversityExistTestWithNonExistingUniversity()
        {
            var id = 999;
            var result = await verificationService.DoesUniversityExist(id);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task RequestStudentVerificationTest()
        {
            var model = new VerificationModel()
            {
                VerificationData = ["test"],
                TargetUniversityId = 1
            };
            int userId = 2;

            await verificationService.RequestStudentVerification(model, userId);

            Assert.IsTrue(dbContext.Applications.Any(i => i.UserId == userId && i.VerificationData.Contains("test")));
        }

        [TestMethod]
        public async Task RequestUniversityVerificationTest()
        {
            var model = new VerificationModel()
            {
                VerificationData = ["test"]
            };
            int userId = 1;

            await verificationService.RequestUniversityVerification(model, userId);

            Assert.IsTrue(dbContext.Applications.Any(i => i.UserId == userId && i.VerificationData.Contains("test")));
        }

        [TestMethod]
        public async Task GetVerificationStatusTest()
        {
            var result = await verificationService.GetVerificationStatus(3);

            Assert.AreEqual(VerificationStatus.Unverified, result);
        }

        [TestMethod]
        public async Task GetPendingVerificationRequestsTest()
        {
            var applications = await verificationService.GetPendingVerificationRequests(1, 0);

            Assert.IsNotNull(applications);
        }

        [TestMethod]
        public async Task AcceptApplicationTest()
        {
            var model = new AcceptRejectModel() { UserId = 2, AcceptedOrRejected = true };

            var result = await verificationService.AcceptApplication(model, 1);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task RejectApplicationTest()
        {
            var model = new AcceptRejectModel() { UserId = 2, AcceptedOrRejected = true };

            var result = await verificationService.RejectApplication(model, 1);

            Assert.IsTrue(result);
        }
    }
}
