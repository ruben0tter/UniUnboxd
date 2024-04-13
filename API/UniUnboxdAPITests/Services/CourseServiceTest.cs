using System.Security.Claims;

namespace UniUnboxdAPITests.Services
{
    [TestClass]
    public class CourseServiceTest
    {
        private readonly UniUnboxdDbContext dbContext;
        private readonly CourseService courseService;

        public CourseServiceTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("CourseService");
            var courseRepository = new CourseRepository(dbContext);
            var userRepository = new UserRepository(dbContext);
            var reviewRepository = new ReviewRepository(dbContext);
            courseService = new CourseService(courseRepository, userRepository, reviewRepository);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("CourseService");

            var university = new University() { Id = 1, Email = "university@gmail.com" };
            var student = new Student() { Id = 2, Email = "student@gmail.com", 
                NotificationSettings = new(), Followers = new List<Follow>() };
            var student1 = new Student() { Id = 3, Email = "student1@gmail.com", 
                NotificationSettings = new(), Following = new List<Follow>() };
            var professor = new Professor()
            {
                Id = 4,
                Email = "professor@gmail.com",
                NormalizedEmail = "PROFESSOR@GMAIL.COM",
                Image = "image",
                AssignedCourses = new List<CourseProfessorAssignment>()
            };
            var professor1 = new Professor()
            {
                Id = 5,
                Email = "professor1@gmail.com",
                AssignedCourses = new List<CourseProfessorAssignment>()
            };
            var professor2 = new Professor()
            {
                Id = 6,
                Email = "professor2@gmail.com",
                AssignedCourses = new List<CourseProfessorAssignment>()
            };
            var course = new Course() { Id = 1, Name = "course", Code = "code", Description = "description", Professor = "professor", 
                Reviews = new(), University = university, AssignedProfessors = new List<CourseProfessorAssignment>(),
                LastModificationTime = DateTime.Now };
            var course1 = new Course() { Id = 2, Name = "course1", Code = "code", Description = "description", Professor = "professor", 
                Reviews = new(), University = university, AssignedProfessors = new List<CourseProfessorAssignment>() };
            var review = new Review() { Rating = 5, Comment = "test", IsAnonymous = false, 
                Course = course, Student = student, Replies = new List<Reply>(), Likes = new List<Like>(),
                LastModificationTime = DateTime.Now };

            course.Reviews.Add(review);

            var follow = new Follow() { FollowingStudent = student1, FollowedStudent = student };
            student.Followers.Add(follow);
            student1.Following.Add(follow);

            var assignment = new CourseProfessorAssignment() { Professor = professor, Course = course };
            var assignment1 = new CourseProfessorAssignment() { Professor = professor1, Course = course };

            professor.AssignedCourses.Add(assignment);
            professor1.AssignedCourses.Add(assignment1);

            dbContext.Users.Add(university);
            dbContext.Users.Add(student);
            dbContext.Users.Add(student1);
            dbContext.Users.Add(professor);
            dbContext.Users.Add(professor1);
            dbContext.Users.Add(professor2);
            dbContext.Courses.Add(course);
            dbContext.Courses.Add(course1);
            dbContext.Reviews.Add(review);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task IsUserValidatedTestSuccesful()
        {
            var userId = 1;
            var claims = new List<Claim>() { new(ClaimTypes.NameIdentifier, userId.ToString()) };
            var identity = new ClaimsIdentity(claims);

            var result = courseService.IsUserValidated(identity, userId);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task IsUserValidatedTestUnsuccesful()
        {
            var userId = 1;
            var claims = new List<Claim>() { new(ClaimTypes.NameIdentifier, "999") };
            var identity = new ClaimsIdentity(claims);

            var result = courseService.IsUserValidated(identity, userId);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task DoesUniversityExistTestSuccesful()
        {
            var result = await courseService.DoesUniversityExist(1);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesUniversityExistTestUnsuccesful()
        {
            var result = await courseService.DoesUniversityExist(999);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task PostCourseTest()
        {
            var course = new Course()
            {
                Id = 3,
                Name = "course",
                Code = "code",
                Description = "description",
                Professor = "professor",
                Reviews = new(),
                University = new(),
                AssignedProfessors = new List<CourseProfessorAssignment>()
            };

            await courseService.PostCourse(course);

            Assert.IsTrue(dbContext.Courses.Any(i => i.Id == 3));
        }

        [TestMethod]
        public async Task CreateCourseCourseTest()
        {
            var model = new CourseCreationModel()
            {
                Name = "course",
                Code = "code",
                Description = "description",
                Professor = "professor",
                UniversityId = 1,
                AssignedProfessors = new()
            };

            var course = await courseService.CreateCourse(model);

            Assert.AreEqual(model.Name, course.Name);
            Assert.AreEqual(model.Code, course.Code);
            Assert.AreEqual(model.Description, course.Description);
            Assert.AreEqual(model.Professor, course.Professor);
        }

        [TestMethod]
        public async Task GetCourseRetrievalModelByIdTest()
        {
            int courseId = 1;
            var course = await courseService.GetCourseRetrievalModelById(courseId);

            Assert.IsNotNull(course);
            Assert.AreEqual(courseId, course.Id);
            Assert.IsNotNull(course.Reviews);
            Assert.IsNotNull(course.AssignedProfessors);
        }

        [TestMethod]
        public async Task GetCourseReviewByStudentTest()
        {
            int courseId = 1;
            int studentId = 2;

            var review = await courseService.GetCourseReviewByStudent(courseId, studentId);

            Assert.IsNotNull(review);
        }

        [TestMethod]
        public async Task GetPopularCoursesOfLastWeekTest()
        {
            var courses = await courseService.GetPopularCoursesOfLastWeek();

            Assert.IsNotNull(courses);
            Assert.IsTrue(courses.Any());
        }

        [TestMethod]
        public async Task GetPopularCoursesOfLastWeekByUniversityTest()
        {
            var courses = await courseService.GetPopularCoursesOfLastWeekByUniversity(1);

            Assert.IsNotNull(courses);
            Assert.IsTrue(courses.Any());
        }

        [TestMethod]
        public async Task GetPopularCoursesOfLastWeekByFriendsTest()
        {
            var courses = await courseService.GetPopularCoursesOfLastWeekByFriends(3);

            Assert.IsNotNull(courses);
            Assert.IsTrue(courses.Any());
        }

        [TestMethod]
        public async Task GetLastEditedCoursesByUniversityTest()
        {
            var courses = await courseService.GetLastEditedCoursesByUniversity(1);

            Assert.IsNotNull(courses);
            Assert.IsTrue(courses.Any());
        }

        [TestMethod]
        public async Task DoesCourseExistTestWithExistingCourse()
        {
            int existingCourseId = 1;
            var result = await courseService.DoesCourseExist(existingCourseId);
            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesCourseExistTestWithNonExistingCourse()
        {
            int nonExistingCourseId = 999;
            var result = await courseService.DoesCourseExist(nonExistingCourseId);
            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task DeleteCourseTest()
        {
            var course = await dbContext.Courses.FirstAsync(i => i.Id == 2);

            await courseService.DeleteCourse(course);

            Assert.IsNull(dbContext.Courses.FirstOrDefault(i => i.Id == 2));
        }

        [TestMethod]
        public async Task GetCourseTest()
        {
            int courseId = 1;
            var course = await courseService.GetCourse(courseId);

            Assert.IsNotNull(course);
            Assert.AreEqual(courseId, course.Id);
            Assert.IsNotNull(course.University);
            Assert.IsNotNull(course.AssignedProfessors);
        }

        [TestMethod]
        public async Task GetAllFriendsThatReviewedTest()
        {
            var friends = await courseService.GetAllFriendsThatReviewed(3, 1);

            Assert.IsNotNull(friends);
            Assert.IsTrue(friends.Any());
        }

        [TestMethod]
        public async Task UpdateCourseTest()
        {
            var course = await courseService.GetCourse(1);
            var model = new CourseEditModel()
            {
                Id = 1,
                Name = "new name",
                Code = "new code",
                Description = "new description",
                Professor = "new professor",
                AssignedProfessors = new()
            };

            courseService.UpdateCourse(course, model);

            Assert.AreEqual(model.Name, course.Name);
            Assert.AreEqual(model.Code, course.Code);
            Assert.AreEqual(model.Description, course.Description);
            Assert.AreEqual(model.Professor, course.Professor);
        }

        [TestMethod]
        public async Task PutCourseTest()
        {
            var course = await dbContext.Courses.FirstAsync(i => i.Id == 1);
            course.Name = "New Name";

            await courseService.PutCourse(course);

            Assert.AreEqual("New Name", dbContext.Courses.First(i => i.Id == 1).Name);
        }

        [TestMethod]
        public async Task DoesProfessorExistTestWithExistingProfessor()
        {
            var id = 4;
            var result = await courseService.DoesProfessorExist(id);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesProfessorExistTestWithNonExistingProfessor()
        {
            var id = 999;
            var result = await courseService.DoesProfessorExist(id);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task GetAssignedCoursesTest()
        {
            int professorId = 4;
            var assignedCourses = await courseService.GetAssignedCourses(professorId);

            Assert.IsNotNull(assignedCourses);
        }

        [TestMethod]
        public async Task DoesCourseProfessorAssignmentExistTestSuccesful()
        {
            var professorId = 4;
            var courseId = 1;

            var result = await courseService.DoesProfessorAssignmentExist(courseId, professorId);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesCourseProfessorAssignmentExistTestUnsuccesful()
        {
            var professorId = 1;
            var courseId = 999;

            var result = await courseService.DoesProfessorAssignmentExist(courseId, professorId);

            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task AssignProfessorTest()
        {
            var professor = await dbContext.Professors.Where(i => i.Id == 6).FirstAsync();
            var course = await dbContext.Courses.Where(i => i.Id == 1).FirstAsync();

            await courseService.AssignProfessor(professor, course);

            Assert.IsTrue(professor.AssignedCourses!.Any(i => i.CourseId == course.Id));
        }

        [TestMethod]
        public async Task DismissProfessorTest()
        {
            var professor = await dbContext.Professors.Where(i => i.Id == 5).FirstAsync();
            var course = await dbContext.Courses.Where(i => i.Id == 1).FirstAsync();

            await courseService.DismissProfessor(professor, course);

            Assert.IsFalse(professor.AssignedCourses!.Any(i => i.CourseId == course.Id));
        }

        [TestMethod]
        public async Task GetProfessorTest()
        {
            var id = 4;
            var professor = await courseService.GetProfessor(id);

            Assert.IsNotNull(professor);
            Assert.IsInstanceOfType(professor, typeof(Professor));
            Assert.AreEqual(id, professor.Id);
            Assert.AreEqual("professor@gmail.com", professor.Email);
        }
    }
}
