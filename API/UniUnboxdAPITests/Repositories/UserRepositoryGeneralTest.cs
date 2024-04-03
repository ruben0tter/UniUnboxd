using UniUnboxdAPITests.TestUtilities;

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

            var student = new Student() { Id = 1, Email = "student@gmail.com", 
                Following = new List<Follow>(), Followers = new List<Follow>(), 
                NotificationSettings = new NotificationSettings(),
                Reviews = new List<Review>() };

            dbContext.Users.Add(student);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task GetUserByIdTest()
        {
            var id = 1;
            var user = await userRepository.GetUser(id);

            Assert.IsNotNull(user);
            Assert.IsInstanceOfType(user, typeof(Student));
            Assert.AreEqual(id, user.Id);
            Assert.AreEqual("student@gmail.com", user.Email);
        }

        [TestMethod]
        public async Task GetUserByEmailTest()
        {
            var email = "student@gmail.com";
            var user = await userRepository.GetUser(email);

            Assert.IsNotNull(user);
            Assert.IsInstanceOfType(user, typeof(Student));
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

        [TestMethod]
        public async Task GetFollowersTest()
        {
            var id = 1;
            var student = await userRepository.GetStudent(id);
            var followers = await userRepository.GetFollowers(id);

            Assert.IsNotNull(followers);
            Assert.IsTrue(followers.All(x => student.Followers!.Any(y => y.FollowingStudentId == x.Id)));
        }

        [TestMethod]
        public async Task GetFollowingTest()
        {
            var id = 1;
            var student = await userRepository.GetStudent(id);
            var following = await userRepository.GetFollowing(id);

            Assert.IsNotNull(following);
            Assert.IsTrue(following.All(x => student.Following!.Any(y => y.FollowedStudentId == x.Id)));
        }
    }
}
