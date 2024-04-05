using UniUnboxdAPI.Repositories;

namespace UniUnboxdAPITests.Services
{
    [TestClass]
    public class UserServiceProfessorTest
    {
        private readonly UniUnboxdDbContext dbContext;
        private readonly UserService userService;

        public UserServiceProfessorTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("UserServiceProfessor");
            var userRepository = new UserRepository(dbContext);
            var courseRepository = new CourseRepository(dbContext);
            var mailService = new MailService(ConfigurationUtil.CreateConfiguration());
            var pushNotificationService = new PushNotificationService();
            userService = new UserService(userRepository, courseRepository, mailService, pushNotificationService);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("UserServiceProfessor");

            var professor = new Professor()
            {
                Id = 1,
                Email = "professor@gmail.com",
                NormalizedEmail = "PROFESSOR@GMAIL.COM",
                Image = "image",
                AssignedCourses = new List<CourseProfessorAssignment>()
            };

            var professor1 = new Professor()
            {
                Id = 2,
                Email = "professor1@gmail.com",
                AssignedCourses = new List<CourseProfessorAssignment>()
            };

            var professor2 = new Professor()
            {
                Id = 3,
                Email = "professor2@gmail.com",
                AssignedCourses = new List<CourseProfessorAssignment>()
            };

            var university = new University() { Id = 4 };

            var course = new Course()
            {
                Id = 1,
                Name = "course",
                Code = "code",
                Description = "description",
                Image = null,
                Professor = "professor",
                Reviews = new List<Review>(),
                University = university,
                AssignedProfessors = new List<CourseProfessorAssignment>()
            };

            var assignment = new CourseProfessorAssignment() { Professor = professor, Course = course };
            var assignment1 = new CourseProfessorAssignment() { Professor = professor1, Course = course };

            professor.AssignedCourses.Add(assignment);
            professor1.AssignedCourses.Add(assignment1);

            dbContext.Users.Add(university);
            dbContext.Courses.Add(course);
            dbContext.Users.Add(professor);
            dbContext.Users.Add(professor1);
            dbContext.Users.Add(professor2);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task DoesProfessorExistTestWithExistingProfessor()
        {
            var id = 1;
            var result = await userService.DoesProfessorExist(id);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesProfessorExistTestWithNonExistingProfessor()
        {
            var id = 999;
            var result = await userService.DoesProfessorExist(id);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task DoesCourseExistTestWithExistingCourse()
        {
            int existingCourseId = 1;
            var result = await userService.DoesCourseExist(existingCourseId);
            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesCourseExistTestWithNonExistingCourse()
        {
            int nonExistingCourseId = 999;
            var result = await userService.DoesCourseExist(nonExistingCourseId);
            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task GetCourseTest()
        {
            int courseId = 1;
            var course = await userService.GetCourse(courseId);

            Assert.IsNotNull(course);
            Assert.AreEqual(courseId, course.Id);
            Assert.IsNotNull(course.University);
            Assert.IsNotNull(course.AssignedProfessors);
        }

        [TestMethod]
        public async Task GetProfessorTest()
        {
            var id = 1;
            var professor = await userService.GetProfessor(id);

            Assert.IsNotNull(professor);
            Assert.IsInstanceOfType(professor, typeof(Professor));
            Assert.AreEqual(id, professor.Id);
            Assert.AreEqual("professor@gmail.com", professor.Email);
        }

        [TestMethod]
        public async Task AssignProfessorToCourseTest()
        {
            var professor = await dbContext.Professors.Where(i => i.Id == 3).FirstAsync();
            var course = await dbContext.Courses.Where(i => i.Id == 1).FirstAsync();

            await userService.AssignProfessor(professor, course);

            Assert.IsTrue(professor.AssignedCourses!.Any(i => i.CourseId == course.Id));
        }

        [TestMethod]
        public async Task DoesCourseProfessorAssignmentExistTestSuccesful()
        {
            var professorId = 1;
            var courseId = 1;

            var result = await userService.DoesCourseProfessorAssignmentExist(courseId, professorId);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesCourseProfessorAssignmentExistTestUnsuccesful()
        {
            var professorId = 1;
            var courseId = 999;

            var result = await userService.DoesCourseProfessorAssignmentExist(courseId, professorId);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task GetAssignedProfessorsTest()
        {
            var id = 1;
            var professors = await userService.GetAssignedProfessors(id);

            Assert.IsNotNull(professors);
            Assert.IsTrue(professors.Any());
            Assert.IsTrue(professors.Any(i => i.Id == 1));
        }

        [TestMethod]
        public async Task GetProfessorAndConnectedDataTest()
        {
            var id = 1;
            var professor = await userService.GetProfessorAndConnectedData(id);

            Assert.IsNotNull(professor);
            Assert.IsInstanceOfType(professor, typeof(ProfessorProfileModel));
            Assert.AreEqual(id, professor.Id);
        }

        [TestMethod]
        public async Task UpdateProfessorTest()
        {
            var professor = await userService.GetProfessor(1);
            var model = new ProfessorEditModel() { Id = 1, Name = "new name" };

            userService.UpdateProfessor(professor, model);

            Assert.AreEqual(model.Id, professor.Id);
            Assert.AreEqual(model.Name, professor.UserName);
        }

        [TestMethod]
        public async Task PutProfessorTest()
        {
            var id = 1;
            var professor = await userService.GetProfessor(id);
            professor.UserName = "professor";

            await userService.PutProfessor(professor);

            professor = await userService.GetProfessor(id);

            Assert.AreEqual("professor", professor.UserName);
        }

        [TestMethod]
        public async Task GetAssignedProfessorTest()
        {
            var email = "professor@gmail.com";
            var professor = await userService.GetAssignedProfessor(email);

            Assert.IsNotNull(professor);
            Assert.IsInstanceOfType(professor, typeof(AssignedProfessorModel));
            Assert.AreEqual(1, professor.Id);
            Assert.AreEqual(email, professor.Email);
        }

        [TestMethod]
        public async Task DoesProfessorExistByEmailTestWithExistingProfessor()
        {
            var email = "professor@gmail.com";
            var result = await userService.DoesProfessorExist(email);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesProfessorExistByEmailTestWithNonExistingProfessor()
        {
            var email = string.Empty;
            var result = await userService.DoesProfessorExist(email);

            Assert.IsFalse(result);
        }
    }
}
