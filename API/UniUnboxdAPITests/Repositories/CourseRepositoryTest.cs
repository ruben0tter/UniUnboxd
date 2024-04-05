namespace UniUnboxdAPITests.Repositories
{
    [TestClass]
    public class CourseRepositoryTest
    {
        private readonly CourseRepository courseRepository;
        private readonly UniUnboxdDbContext dbContext;

        public CourseRepositoryTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("CourseRepository");
            courseRepository = new CourseRepository(dbContext);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("CourseRepository");

            var course = new Course() { Id = 1, Name = "course", Code = "code", Description = "description", Professor = "professor", 
                Reviews = new(), University = new(), AssignedProfessors = new List<CourseProfessorAssignment>() };
            var course1 = new Course() { Id = 2, Name = "course1", Code = "code", Description = "description", Professor = "professor", 
                Reviews = new(), University = new(), AssignedProfessors = new List<CourseProfessorAssignment>() };

            dbContext.Courses.Add(course);
            dbContext.Courses.Add(course1);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task DoesCourseExistTestWithExistingCourse()
        {
            int existingCourseId = 1;
            var result = await courseRepository.DoesCourseExist(existingCourseId);
            Assert.IsTrue(result);
        }

        [TestMethod]
        public async Task DoesCourseExistTestWithNonExistingCourse()
        {
            int nonExistingCourseId = 999;
            var result = await courseRepository.DoesCourseExist(nonExistingCourseId);
            Assert.IsFalse(result);
        }

        [TestMethod]
        public async Task GetCourseTest()
        {
            int courseId = 1;
            var course = await courseRepository.GetCourse(courseId);

            Assert.IsNotNull(course);
            Assert.AreEqual(courseId, course.Id);
            Assert.IsNotNull(course.University);
            Assert.IsNotNull(course.AssignedProfessors);
        }

        [TestMethod]
        public async Task GetCourseAndConnectedDataTest()
        {
            int courseId = 1;
            var course = await courseRepository.GetCourseAndConnectedData(courseId);

            Assert.IsNotNull(course);
            Assert.AreEqual(courseId, course.Id);
            Assert.IsNotNull(course.University);
            Assert.IsNotNull(course.Reviews);
            Assert.IsNotNull(course.AssignedProfessors);
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

            await courseRepository.PostCourse(course);
            
            Assert.IsTrue(dbContext.Courses.Any(i => i.Id == 3));
        }

        [TestMethod]
        public async Task GetPopularCourseOfLastWeekTest()
        {
            var popularCourses = await courseRepository.GetPopularCourseOfLastWeek();

            Assert.IsNotNull(popularCourses);
            Assert.IsTrue(popularCourses.All(i => i.Reviews.Any(i => i.LastModificationTime > DateTime.Now.AddDays(-7))));
            Assert.IsTrue(popularCourses.SequenceEqual(popularCourses.OrderByDescending(i => i.Reviews.Where(i => i.LastModificationTime > DateTime.Now.AddDays(-7)).Count())));
        }

        [TestMethod]
        public async Task GetPopularCourseOfLastWeekByUniversityTest()
        {
            int universityId = 1;
            var popularCourses = await courseRepository.GetPopularCourseOfLastWeekByUniversity(universityId);

            Assert.IsNotNull(popularCourses);
            Assert.IsTrue(popularCourses.All(i => i.University.Id == universityId));
            Assert.IsTrue(popularCourses.All(i => i.Reviews.Any(i => i.LastModificationTime > DateTime.Now.AddDays(-7))));
            Assert.IsTrue(popularCourses.SequenceEqual(popularCourses.OrderByDescending(i => i.Reviews.Where(i => i.LastModificationTime > DateTime.Now.AddDays(-7)).Count())));
        }

        [TestMethod]
        public async Task GetPopularCourseOfLastWeekByFriendsTest()
        {
            int studentId = 1;
            var popularCourses = await courseRepository.GetPopularCourseOfLastWeekByFriends(studentId);

            Assert.IsNotNull(popularCourses);
            Assert.IsTrue(popularCourses.All(i => i.Reviews.Any(i => i.LastModificationTime > DateTime.Now.AddDays(-7))));
            Assert.IsTrue(popularCourses.SequenceEqual(popularCourses.OrderByDescending(i => i.Reviews.Where(i => i.LastModificationTime > DateTime.Now.AddDays(-7)).Count())));
        }

        [TestMethod]
        public async Task GetLastEditedCoursesByUniversityTest()
        {
            int universityId = 1;
            var lastEditedCourses = await courseRepository.GetLastEditedCoursesByUniversity(universityId);

            Assert.IsNotNull(lastEditedCourses);
            Assert.IsTrue(lastEditedCourses.SequenceEqual(lastEditedCourses.OrderByDescending(i => i.LastModificationTime)));
        }

        [TestMethod]
        public async Task PutCourseTest()
        {
            var course = await dbContext.Courses.FirstAsync(i => i.Id == 1);
            course.Name = "New Name";

            await courseRepository.PutCourse(course);

            Assert.AreEqual("New Name", dbContext.Courses.First(i => i.Id == 1).Name);
        }

        [TestMethod]
        public async Task DeleteCourseTest()
        {
            var course = await dbContext.Courses.FirstAsync(i => i.Id == 2);

            await courseRepository.DeleteCourse(course);

            Assert.IsNull(dbContext.Courses.FirstOrDefault(i => i.Id == 2));
        }

        [TestMethod]
        public async Task GetAssignedCoursesTest()
        {
            int professorId = 1;
            var assignedCourses = await courseRepository.GetAssignedCourses(professorId);

            Assert.IsNotNull(assignedCourses);
        }
    }
}
