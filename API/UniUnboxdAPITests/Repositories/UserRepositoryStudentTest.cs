namespace UniUnboxdAPITests.Repositories
{
    [TestClass]
    public class UserRepositoryStudentTest
    {
        private readonly UserRepository userRepository;

        public UserRepositoryStudentTest()
        {
            var dbContext = DatabaseUtil.CreateDbContext("UserRepositoryStudent");
            userRepository = new UserRepository(dbContext);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("UserRepositoryStudent");

            var student = new Student()
            {
                Id = 1,
                Email = "student@gmail.com",
                Image = "image",
                Following = new List<Follow>(),
                Followers = new List<Follow>(),
                NotificationSettings = new NotificationSettings(),
                Reviews = new List<Review>()
            };

            var student1 = new Student()
            {
                Id = 2,
                Email = "student1@gmail.com",
                Following = new List<Follow>(),
                Followers = new List<Follow>(),
                NotificationSettings = new NotificationSettings()
            };

            var student2 = new Student()
            {
                Id = 3,
                Email = "student2@gmail.com",
                Following = new List<Follow>(),
                Followers = new List<Follow>(),
                NotificationSettings = new NotificationSettings()
            };

            var student3 = new Student()
            {
                Id = 4,
                Email = "student3@gmail.com",
                Following = new List<Follow>(),
                Followers = new List<Follow>(),
                NotificationSettings = new NotificationSettings()
            };

            var student4 = new Student()
            {
                Id = 5,
                Email = "student4@gmail.com",
                Following = new List<Follow>(),
                Followers = new List<Follow>(),
                NotificationSettings = new NotificationSettings()
            };

            var follow = new Follow() { FollowingStudent = student1, FollowedStudent = student };
            var follow1 = new Follow() { FollowingStudent = student, FollowedStudent = student2 };
            var follow2 = new Follow() { FollowingStudent = student3, FollowedStudent = student4 };

            student.Followers.Add(follow);
            student.Following.Add(follow1);
            student1.Following.Add(follow);
            student2.Followers.Add(follow1);
            student3.Following.Add(follow2);
            student4.Followers.Add(follow2);

            dbContext.Users.Add(student);
            dbContext.Users.Add(student1);
            dbContext.Users.Add(student2);
            dbContext.Users.Add(student3);
            dbContext.Users.Add(student4);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task GetImageOfStudentTest()
        {
            int id = 1;
            var image = await userRepository.GetImageOf(id, UserType.Student);

            Assert.IsNotNull(image);
            Assert.AreEqual("image", image);
        }

        [TestMethod]
        public async Task DoesStudentExistTestWithExistingStudent()
        {
            var id = 1;
            var result = await userRepository.DoesStudentExist(id);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesStudentExistTestWithNonExistingStudent()
        {
            var id = 6;
            var result = await userRepository.DoesStudentExist(id);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task GetStudentTest()
        {
            var id = 1;
            var student = await userRepository.GetStudent(id);

            Assert.IsNotNull(student);
            Assert.IsInstanceOfType(student, typeof(Student));
            Assert.AreEqual(id, student.Id);
            Assert.AreEqual("student@gmail.com", student.Email);
            Assert.IsNotNull(student.NotificationSettings);
            Assert.IsNotNull(student.Followers);
        }

        [TestMethod]
        public async Task GetStudentAndConnectedDataTest()
        {
            var id = 1;
            var student = await userRepository.GetStudentAndConnectedData(id);

            Assert.IsNotNull(student);
            Assert.IsInstanceOfType(student, typeof(Student));
            Assert.AreEqual(id, student.Id);
            Assert.AreEqual("student@gmail.com", student.Email);
            Assert.IsNotNull(student.NotificationSettings);
            Assert.IsNotNull(student.Following);
            Assert.IsNotNull(student.Followers);
            Assert.IsTrue(student.Followers.Any());
            Assert.IsNotNull(student.Reviews);
        }

        [TestMethod]
        public async Task SetDeviceTokenTest()
        {
            var id = 1;
            var token = "token";

            await userRepository.SetDeviceToken(id, token);

            var student = await userRepository.GetStudent(id);

            Assert.AreEqual(token, student.DeviceToken);
        }

        [TestMethod]
        public async Task DoesStudentFollowStudentTestSuccesful()
        {
            var followingId = 1;
            var followedId = 3;

            var result = await userRepository.DoesStudentFollowStudent(followingId, followedId);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesStudentFollowStudentTestUnsuccesful()
        {
            var followingId = 1;
            var followedId = 2;

            var result = await userRepository.DoesStudentFollowStudent(followingId, followedId);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task FollowTest()
        {
            var student3 = await userRepository.GetStudent(4);
            var student4 = await userRepository.GetStudent(5);

            var follow = new Follow() { FollowingStudent = student4, FollowedStudent = student3 };

            await userRepository.FollowStudent(follow);

            Assert.IsTrue(student4.Following!.Any(i => i.FollowedStudent == student3));
            Assert.IsTrue(student3.Followers!.Any(i => i.FollowingStudent == student4));
        }

        [TestMethod]
        public async Task UnfollowTest()
        {
            var student3 = await userRepository.GetStudent(4);
            var student4 = await userRepository.GetStudent(5);

            await userRepository.UnfollowStudent(student3.Id, student4.Id);

            Assert.IsFalse(student3.Following!.Any(i => i.FollowedStudent == student4));
            Assert.IsFalse(student4.Followers!.Any(i => i.FollowingStudent == student3));
        }

        [TestMethod]
        public async Task PutStudentTest()
        {
            var id = 1;
            var student = await userRepository.GetStudent(id);
            student.UserName = "student";

            await userRepository.PutStudent(student);

            student = await userRepository.GetStudent(id);

            Assert.AreEqual("student", student.UserName);
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
            var student = await userRepository.GetStudentAndConnectedData(id);
            var following = await userRepository.GetFollowing(id);

            Assert.IsNotNull(following);
            Assert.IsTrue(following.All(x => student.Following!.Any(y => y.FollowedStudentId == x.Id)));
        }
    }
}
