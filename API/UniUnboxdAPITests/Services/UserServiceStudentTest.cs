namespace UniUnboxdAPITests.Services
{
    [TestClass]
    public class UserServiceStudentTest
    {
        private readonly UniUnboxdDbContext dbContext;
        private readonly UserService userService;

        public UserServiceStudentTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("UserServiceStudent");
            var userRepository = new UserRepository(dbContext);
            var courseRepository = new CourseRepository(dbContext);
            var mailService = new MailService(ConfigurationUtil.CreateConfiguration());
            var pushNotificationService = new PushNotificationService();
            userService = new UserService(userRepository, courseRepository, mailService, pushNotificationService);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("UserServiceStudent");

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
        public async Task DoesStudentExistTestWithExistingStudent()
        {
            var id = 1;
            var result = await userService.DoesStudentExist(id);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesStudentExistTestWithNonExistingStudent()
        {
            var id = 999;
            var result = await userService.DoesStudentExist(id);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task GetStudentTest()
        {
            var id = 1;
            var student = await userService.GetStudent(id);

            Assert.IsNotNull(student);
            Assert.IsInstanceOfType(student, typeof(Student));
            Assert.AreEqual(id, student.Id);
            Assert.AreEqual("student@gmail.com", student.Email);
            Assert.IsNotNull(student.NotificationSettings);
            Assert.IsNotNull(student.Followers);
        }

        [TestMethod]
        public async Task SetDeviceTokenTest()
        {
            var id = 1;
            var token = "token";

            await userService.SetDeviceToken(id, token);

            var student = await userService.GetStudent(id);

            Assert.AreEqual(token, student.DeviceToken);
        }

        [TestMethod]
        public async Task DoesStudentFollowStudentTestSuccesful()
        {
            var followingId = 1;
            var followedId = 3;

            var result = await userService.DoesStudentFollowStudent(followingId, followedId);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesStudentFollowStudentTestUnsuccesful()
        {
            var followingId = 1;
            var followedId = 2;

            var result = await userService.DoesStudentFollowStudent(followingId, followedId);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task FollowTest()
        {
            var student3 = await userService.GetStudent(4);
            var student4 = await userService.GetStudent(5);

            await userService.FollowStudent(student4, student3);

            Assert.IsTrue(student4.Following!.Any(i => i.FollowedStudent == student3));
            Assert.IsTrue(student3.Followers!.Any(i => i.FollowingStudent == student4));
        }

        [TestMethod]
        public async Task UnfollowTest()
        {
            var student3 = await userService.GetStudent(4);
            var student4 = await userService.GetStudent(5);

            await userService.UnfollowStudent(student3, student4);

            Assert.IsFalse(student3.Following!.Any(i => i.FollowedStudent == student4));
            Assert.IsFalse(student4.Followers!.Any(i => i.FollowingStudent == student3));
        }

        [TestMethod]
        public async Task GetStudentAndConnectedDataTest()
        {
            var id = 1;
            var student = await userService.GetStudentAndConnectedData(id);

            Assert.IsNotNull(student);
            Assert.AreEqual(id, student.Id);
            Assert.IsNotNull(student.NotificationSettings);
            Assert.IsNotNull(student.Following);
            Assert.IsNotNull(student.Followers);
            Assert.IsTrue(student.Followers.Any());
            Assert.IsNotNull(student.Reviews);
        }

        [TestMethod]
        public async Task UpdateStudentTest()
        {
            var student = await userService.GetStudent(1);
            var model = new StudentEditModel() { Id = 1, Name = "new name", VerificationStatus = VerificationStatus.Unverified, NotificationSettings = new() { StudentId = 1 } };

            userService.UpdateStudent (student, model);

            Assert.AreEqual(model.Id, student.Id);
            Assert.AreEqual(model.Name, student.UserName);
            Assert.AreEqual(model.VerificationStatus, student.VerificationStatus);
        }

        [TestMethod]
        public async Task PutStudentTest()
        {
            var id = 1;
            var student = await userService.GetStudent(id);
            student.UserName = "student";

            await userService.PutStudent(student);

            student = await userService.GetStudent(id);

            Assert.AreEqual("student", student.UserName);
        }

        [TestMethod]
        public async Task GetFollowersTest()
        {
            var id = 1;
            var student = await userService.GetStudent(id);
            var followers = await userService.GetFollowers(id);

            Assert.IsNotNull(followers);
            Assert.IsTrue(followers.All(x => student.Followers!.Any(y => y.FollowingStudentId == x.Id)));
        }

        [TestMethod]
        public async Task GetFollowedStudentsTest()
        {
            var id = 1;
            var student = await userService.GetStudentAndConnectedData(id);
            var following = await userService.GetFollowedStudents(id);

            Assert.IsNotNull(following);
            Assert.IsTrue(following.All(x => student.Following!.Any(y => y.Id == x.Id)));
        }

        [TestMethod]
        public async Task GetStudentListItemTest()
        {
            var id = 1;
            var student = await userService.GetStudentListItem(id);

            Assert.IsNotNull(student);
            Assert.IsInstanceOfType(student, typeof(StudentGridModel));
            Assert.AreEqual(id, student.Id);
        }
    }
}
