namespace UniUnboxdAPITests.Repositories
{
    [TestClass]
    public class VerificationRepositoryTest
    {
        private readonly VerificationRepository verificationRepository;
        private readonly UniUnboxdDbContext dbContext;

        public VerificationRepositoryTest() 
        {
            dbContext = DatabaseUtil.CreateDbContext("VerificationRepository");
            verificationRepository = new VerificationRepository(dbContext);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("VerificationRepository");

            var university = new University { Id = 1, Email = "university@gmail.com" };
            var student = new Student { Id = 2, Email = "student@gmail.com", NotificationSettings = new NotificationSettings() };
            var application = new VerificationApplication { UserId = 1, VerificationData = ["test"], TargetUniversity = university };
            var application1 = new VerificationApplication { UserId = 2, VerificationData = ["test"], TargetUniversity = university };

            dbContext.Users.Add(university);
            dbContext.Users.Add(student);
            dbContext.Applications.Add(application);
            dbContext.Applications.Add(application1);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task GetNextApplicationsTest()
        {
            var university = await dbContext.Universities.FirstAsync(i => i.Id == 1);
            var applications = await verificationRepository.GetNextApplications(university, 0, 10);

            Assert.IsNotNull(applications);
            Assert.IsTrue(applications.Length > 0);
        }

        [TestMethod]
        public async Task AddApplicationTest()
        {
            var application = new VerificationApplication
            {
                UserId = 3,
                VerificationData = ["test"]
            };

            await verificationRepository.AddApplication(application);

            Assert.IsTrue(dbContext.Applications.Any(i => i.Id == 3 && i.VerificationData.Contains("test")));
        }

        [TestMethod]
        public async Task SetUniversityTest()
        {
            var student = await dbContext.Students.FirstAsync(i => i.Id == 2);
            var univerisityId = 1;
            await verificationRepository.SetUniversity(student, univerisityId);

            Assert.AreEqual(univerisityId, student.UniversityId);
        }

        [TestMethod]
        public async Task RemoveApplicationTest()
        {
            await verificationRepository.RemoveApplication(2);

            Assert.IsFalse(dbContext.Applications.Any(a => a.UserId == 2));
        }
    }
}
