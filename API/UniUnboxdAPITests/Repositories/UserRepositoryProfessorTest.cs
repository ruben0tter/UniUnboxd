namespace UniUnboxdAPITests.Repositories
{
    [TestClass]
    public class UserRepositoryProfessorTest
    {
        private readonly UniUnboxdDbContext dbContext;
        private readonly UserRepository userRepository;

        public UserRepositoryProfessorTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("UserRepositoryProfessor");
            userRepository = new UserRepository(dbContext);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("UserRepositoryProfessor");

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

            var course = new Course()
            {
                Id = 1,
                Name = "course",
                Code = "code",
                Description = "description",
                Image = null,
                Professor = "professor",
                Reviews = new List<Review>(),
                University = null
            };

            var assignment = new CourseProfessorAssignment() { Professor = professor, Course = course };
            var assignment1 = new CourseProfessorAssignment() { Professor = professor1, Course = course };

            professor.AssignedCourses.Add(assignment);
            professor1.AssignedCourses.Add(assignment1);

            dbContext.Users.Add(professor);
            dbContext.Users.Add(professor1);
            dbContext.Users.Add(professor2);
            dbContext.Courses.Add(course);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task GetImageOfProfessorTest()
        {
            int id = 1;
            var image = await userRepository.GetImageOf(id, UserType.Professor);

            Assert.IsNotNull(image);
            Assert.AreEqual("image", image);
        }

        [TestMethod]
        public async Task DoesProfessorExistByIdTestWithExistingProfessor()
        {
            var id = 1;
            var result = await userRepository.DoesProfessorExist(id);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesProfessorExistByIdTestWithNonExistingProfessor()
        {
            var id = 4;
            var result = await userRepository.DoesProfessorExist(id);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task DoesProfessorExistByEmailTestWithExistingProfessor()
        {
            var email = "professor@gmail.com";
            var result = await userRepository.DoesProfessorExist(email);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesProfessorExistByEmailTestWithNonExistingProfessor()
        {
            var email = string.Empty;
            var result = await userRepository.DoesProfessorExist(email);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task GetProfessorByIdTest()
        {
            var id = 1;
            var professor = await userRepository.GetProfessor(id);

            Assert.IsNotNull(professor);
            Assert.IsInstanceOfType(professor, typeof(Professor));
            Assert.AreEqual(id, professor.Id);
            Assert.AreEqual("professor@gmail.com", professor.Email);
        }

        [TestMethod]
        public async Task GetProfessorByEmailTest()
        {
            var email = "professor@gmail.com";
            var professor = await userRepository.GetProfessor(email);

            Assert.IsNotNull(professor);
            Assert.IsInstanceOfType(professor, typeof(Professor));
            Assert.AreEqual(1, professor.Id);
            Assert.AreEqual(email, professor.Email);
        }

        [TestMethod]
        public async Task GetProfessorAndConnectedDataTest()
        {
            var id = 1;
            var professor = await userRepository.GetProfessorAndConnectedData(id);

            Assert.IsNotNull(professor);
            Assert.IsInstanceOfType(professor, typeof(Professor));
            Assert.AreEqual(id, professor.Id);
            Assert.AreEqual("professor@gmail.com", professor.Email);
            Assert.IsNotNull(professor.AssignedCourses);
        }

        [TestMethod]
        public async Task PutProfessorTest()
        {
            var id = 1;
            var professor = await userRepository.GetProfessor(id);
            professor.UserName= "professor";

            await userRepository.PutProfessor(professor);

            professor = await userRepository.GetProfessor(id);

            Assert.AreEqual("professor", professor.UserName);
        }

        [TestMethod]
        public async Task GetAssignedProfessorsTest()
        {
            var id = 1;
            var professors = await userRepository.GetAssignedProfessors(id);

            Assert.IsNotNull(professors);
            Assert.IsTrue(professors.Any());
            Assert.IsTrue(professors.Any(i => i.Id == 1));
        }

        [TestMethod]
        public async Task DoesCourseProfessorAssignmentExistTestSuccesful()
        {
            var professorId = 1;
            var courseId = 1;

            var result = await userRepository.DoesCourseProfessorAssignmentExist(courseId, professorId);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesCourseProfessorAssignmentExistTestUnsuccesful()
        {
            var professorId = 1;
            var courseId = 999;

            var result = await userRepository.DoesCourseProfessorAssignmentExist(courseId, professorId);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task GetProfessorAssignmentTest()
        {
            var professorId = 1;
            var courseId = 1;

            var assignment = await userRepository.GetProfessorAssignment(professorId, courseId);

            Assert.IsNotNull(assignment);
        }

        [TestMethod]
        public async Task AssignProfessorToCourseTest()
        {
            var professor = await dbContext.Professors.Where(i => i.Id == 3).FirstAsync();
            var course = await dbContext.Courses.Where(i => i.Id == 1).FirstAsync();

            var assignment = new CourseProfessorAssignment() { Professor = professor, Course = course };

            await userRepository.AssignProfessorToCourse(assignment);

            Assert.IsTrue(professor.AssignedCourses!.Any(i => i.CourseId == course.Id));
        }

        [TestMethod]
        public async Task DismissProfessorFromCourseTest()
        {
            var professor = await dbContext.Professors.Where(i => i.Id == 2).FirstAsync();
            var course = await dbContext.Courses.Where(i => i.Id == 1).FirstAsync();

            var assignment = new CourseProfessorAssignment() { Professor = professor, Course = course };

            await userRepository.DismissProfessorFromCourse(assignment);

            Assert.IsFalse(professor.AssignedCourses!.Any(i => i.CourseId == course.Id));
        }
    }
}
