using Castle.Core.Configuration;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Org.BouncyCastle.Crypto;
using System;
using System.Collections.Generic;
using System.Drawing.Printing;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using UniUnboxdAPI.Controllers;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects.Notifications;
using UniUnboxdAPI.Services;

namespace UniUnboxdAPITests.Controllers
{
    [TestClass]
    public class UserControllerTest
    {
        private readonly UserController userController;
        private readonly UserService userService;
        private readonly UniUnboxdDbContext dbContext;


        public UserControllerTest()
        {
            dbContext = DatabaseUtil.CreateDbContext("UserController");
            var courseRepository = new CourseRepository(dbContext);
            var userRepository = new UserRepository(dbContext);
            var mailService = new MailService(ConfigurationUtil.CreateConfiguration());
            var pushNotificationService = new PushNotificationService();
            userService = new UserService(userRepository, courseRepository, mailService, pushNotificationService);
            userController = new UserController(userService);
        }

        [ClassInitialize]
        public static void Init(TestContext context)
        {
            var dbContext = DatabaseUtil.CreateDbContext("UserController");

            var university = new University() { Id = 1, Email = "university@gmail.com" };
            var university2 = new University() { Id = 2, Email = "university2@gmail.com" };
            var university3 = new University() { Id = 3, Email = "university3@gmail.com" };
            var student = new Student()
            {
                Id = 4,
                Email = "student@gmail.com",
                NotificationSettings = new(),
                Followers = new List<Follow>(),
                Following = new List<Follow>()
            };
            var student1 = new Student()
            {
                Id = 5,
                Email = "student1@gmail.com",
                NotificationSettings = new(),
                Following = new List<Follow>()
            };
            var student2 = new Student()
            {
                Id = 6,
                Email = "student2@gmail.com",
                NotificationSettings = new(),
                Followers = new List<Follow>(),
                Following = new List<Follow>()
            };
            var student3 = new Student()
            {
                Id = 7,
                Email = "student3@gmail.com",
                NotificationSettings = new(),
                Following = new List<Follow>()
            };
            var professor = new Professor()
            {
                Id = 8,
                Email = "professor@gmail.com",
                NormalizedEmail = "PROFESSOR@GMAIL.COM",
                Image = "image",
                AssignedCourses = new List<CourseProfessorAssignment>()
            };
            var professor1 = new Professor()
            {
                Id = 9,
                Email = "professor1@gmail.com",
                AssignedCourses = new List<CourseProfessorAssignment>()
            };
            var course = new Course()
            {
                Id = 1,
                Name = "course",
                Code = "code",
                Description = "description",
                Professor = "professor",
                Reviews = new(),
                University = university,
                AssignedProfessors = new List<CourseProfessorAssignment>(),
                LastModificationTime = DateTime.Now
            };

            var follow = new Follow() { FollowingStudent = student1, FollowedStudent = student };
            student.Followers.Add(follow);
            student1.Following.Add(follow);
            var follow2 = new Follow() { FollowingStudent = student, FollowedStudent = student2 };
            student.Following.Add(follow2);
            student2.Followers.Add(follow2);
            var follow3 = new Follow() { FollowingStudent = student3, FollowedStudent = student2 };
            student3.Following.Add(follow3);
            student2.Followers.Add(follow3);

            var assignment = new CourseProfessorAssignment() { Professor = professor, Course = course };
            var assignment1 = new CourseProfessorAssignment() { Professor = professor1, Course = course };

            professor.AssignedCourses.Add(assignment);
            professor1.AssignedCourses.Add(assignment1);

            dbContext.Users.Add(university);
            dbContext.Users.Add(university2);
            dbContext.Users.Add(university3);
            dbContext.Users.Add(student);
            dbContext.Users.Add(student1);
            dbContext.Users.Add(student2);
            dbContext.Users.Add(student3);
            dbContext.Users.Add(professor);
            dbContext.Users.Add(professor1);
            dbContext.Courses.Add(course);
            dbContext.SaveChanges();
        }

        [TestMethod]
        public async Task GetStudent()
        {
            ConfigurationUtil.SetHttpContext(userController, 4, UserType.Student);
            var userId = 4;
            ObjectResult result = (ObjectResult) await userController.GetStudent(userId);
            var student = result.Value as StudentProfileModel;

            Assert.IsNotNull(student); 
            Assert.AreEqual(200, result.StatusCode);
            Assert.AreEqual(student.Id, userId);
        }

        [TestMethod]
        public async Task GetProfessor()
        {
            ConfigurationUtil.SetHttpContext(userController, 8, UserType.Professor);
            var userId = 8;
            ObjectResult result = (ObjectResult) await userController.GetProfessor(userId);
            var professor = result.Value as ProfessorProfileModel;

            Assert.IsNotNull(professor);
            Assert.AreEqual(200, result.StatusCode);
            Assert.AreEqual(professor.Id, userId);
        }

        [TestMethod]
        public async Task GetUniversities()
        {
            ObjectResult result = (ObjectResult) await userController.GetUniversities();
            var universities = result.Value as List<UniversityNameModel>;
            Assert.IsNotNull(universities);
            foreach (UniversityNameModel university in universities)
            {
                Assert.IsNotNull(university);
                Assert.IsTrue(dbContext.Universities.Any(i => i.Id == university.Id));
            }
            Assert.AreEqual(200, result.StatusCode);
        }

        [TestMethod]
        public async Task GetAssignedProfessorByEmail()
        {
            ConfigurationUtil.SetHttpContext(userController, 8, UserType.Professor);
            var email = "professor@gmail.com";
            ObjectResult result = (ObjectResult) await userController.GetAssignedProfessorByEmail(email);
            var professor = result.Value as AssignedProfessorModel;

            Assert.IsNotNull(professor);
            Assert.AreEqual(professor.Email, email);
            Assert.AreEqual(200, result.StatusCode);
        }

        [TestMethod]
        public async Task GetAssignedProfessors()
        {
            ConfigurationUtil.SetHttpContext(userController, 1, UserType.University);
            var courseId = 1;
            ObjectResult result = (ObjectResult) await userController.GetAssignedProfessors(courseId);
            var professors = result.Value as List<AssignedProfessorModel>;
            Assert.IsNotNull(professors);
            foreach (AssignedProfessorModel professor in professors)
            {
                Assert.IsNotNull(professor);
                Assert.IsTrue(dbContext.CourseProfessorAssignments.Any(i => i.CourseId == courseId && i.ProfessorId == professor.Id));
            }
            Assert.AreEqual(200, result.StatusCode);
        }

        [TestMethod]
        public async Task GetFollowedStudents()
        {
            ConfigurationUtil.SetHttpContext(userController, 7, UserType.Student);
            ObjectResult result = (ObjectResult) await userController.GetFollowedStudents();
            var students = result.Value as List<StudentGridModel>;
            Assert.IsNotNull(students);
            foreach (StudentGridModel student in students)
            {
                Assert.IsNotNull(student);
                Assert.AreEqual(6, student.Id);

            }
            Assert.AreEqual(200, result.StatusCode);
        }

        [TestMethod]
        public async Task GetFollowers()
        {
            ConfigurationUtil.SetHttpContext(userController, 4, UserType.Student);
            ObjectResult result = (ObjectResult) await userController.GetFollowers();
            var students = result.Value as List<StudentGridModel>;
            Assert.IsNotNull(students);
            foreach (StudentGridModel student in students)
            {
                Assert.IsNotNull(student);
                Assert.AreEqual(student.Id, 5);
            }
            Assert.AreEqual(200, result.StatusCode);
        }

        [TestMethod]
        public async Task GetStudentListItem()
        {
            ConfigurationUtil.SetHttpContext(userController, 4, UserType.Student);
            var iD = 4;
            ObjectResult result = (ObjectResult) await userController.GetStudentListItem(iD);
            var student = result.Value as StudentGridModel;
            Assert.IsNotNull(student);
            Assert.AreEqual(student.Id, iD);
            Assert.AreEqual(200, result.StatusCode);
        }

        [TestMethod]
        public async Task PutStudent()
        {
            ConfigurationUtil.SetHttpContext(userController, 4, UserType.Student);
            StudentEditModel model = new StudentEditModel() { Id = 4, Name = "Lebron", NotificationSettings = new NotificationSettingsModel() { StudentId=4},
                VerificationStatus = VerificationStatus.Verified};
            ObjectResult result = (ObjectResult) await userController.PutStudent(model);

            Assert.AreEqual("Student successfully updated", result.Value);
            Assert.AreEqual(200, result.StatusCode);

        }

        [TestMethod]
        public async Task SetDeviceToken()
        {
            ConfigurationUtil.SetHttpContext(userController, 4, UserType.Student);
            DeviceTokenModel model = new DeviceTokenModel() {DeviceToken =  "deviceToken"};
            OkResult result = (OkResult) await userController.SetDeviceToken(model);

            Assert.AreEqual(200, result.StatusCode);
        }

        [TestMethod]
        public async Task PutProfessor()
        {
            ConfigurationUtil.SetHttpContext(userController, 8, UserType.Professor);
            ProfessorEditModel model = new ProfessorEditModel() { Id = 8, Name = "Messi" };
            ObjectResult result = (ObjectResult) await userController.PutProfessor(model);

            Assert.AreEqual("Professor was updated successfully.", result.Value);
            Assert.AreEqual(200, result.StatusCode);
        }

        [TestMethod]
        public async Task Follow()
        {
            ConfigurationUtil.SetHttpContext(userController, 4, UserType.Student);
            OkResult result = (OkResult) await userController.Follow(5);

            Assert.IsTrue(dbContext.Follows.Any(i => i.FollowedStudentId == 5 && i.FollowingStudentId == 4));
            Assert.AreEqual(200, result.StatusCode);
        }

        [TestMethod]
        public async Task UnFollow()
        {
            ConfigurationUtil.SetHttpContext(userController, 5, UserType.Student);
            OkResult result = (OkResult) await userController.Unfollow(4);

            Assert.IsFalse(dbContext.Follows.Any(i => i.FollowedStudentId == 4 && i.FollowingStudentId == 5));
            Assert.AreEqual(200, result.StatusCode);
        }
    }
}
