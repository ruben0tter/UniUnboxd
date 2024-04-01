using Microsoft.IdentityModel.Tokens;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;

namespace UniUnboxdAPI.Services
{
    public class UserService(UserRepository userRepository, CourseRepository courseRepository, MailService mailService, PushNotificationService pushNotificationService)
    {
        /// <summary>
        /// Check whether there exists a student with the provided id.
        /// </summary>
        /// <param name="id">Provided student id.</param>
        /// <returns>Whether there exists a student with the provided id.</returns>
        public async Task<bool> DoesStudentExist(int id)
            => await userRepository.DoesStudentExist(id);

        /// <summary>
        /// Gets the Student model attached to the provided id.
        /// </summary>
        /// <param name="id">Provided student id.</param>
        /// <returns>Student model with given id.</returns>
        public async Task<Student> GetStudent(int id)
            => await userRepository.GetStudent(id);

        public async Task SetDeviceToken(int studentId, string deviceToken)
            => await userRepository.SetDeviceToken(studentId, deviceToken);

        /// <summary>
        /// Check whether student 1 already follows student 2.
        /// </summary>
        /// <param name="followingStudentId">Provided student 1 id.</param>
        /// <param name="followedStudentId">Provided student 2 id.</param>
        /// <returns>Whether student 1 follows student 2.</returns>
        public async Task<bool> DoesStudentFollowStudent(int followingStudentId, int followedStudentId)
            => await userRepository.DoesStudentFollowStudent(followingStudentId, followedStudentId);

        /// <summary>
        /// Student 1 follows student 2.
        /// </summary>
        /// <param name="followingStudent">Provided student 1.</param>
        /// <param name="followedStudent">Provided student 2.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task FollowStudent(Student followingStudent, Student followedStudent)
        {
            var followModel = CreateFollow(followingStudent, followedStudent);
            await userRepository.FollowStudent(followModel);
        }

        /// <summary>
        /// Notify Student 2 that Student 1 has followed them.
        /// </summary>
        /// <param name="followingStudent">Provided student 1.</param>
        /// <param name="followedStudent">Provided student 2.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public void NotifyFollowedStudent(Student followingStudent, Student followedStudent)
        {
            if (followedStudent.NotificationSettings!.ReceivesNewFollowerMail)
                mailService.SendNewFollowerNotification(followingStudent, followedStudent);

            if (followedStudent.NotificationSettings!.ReceivesNewFollowerPush)
                pushNotificationService.SendNewFollowerNotification(followingStudent, followedStudent);
        }

        /// <summary>
        /// Student 1 unfollows student 2.
        /// </summary>
        /// <param name="unfollowingStudent">Provided student 1.</param>
        /// <param name="unfollowedStudent">Provided student 2.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task UnfollowStudent(Student unfollowingStudent, Student unfollowedStudent)
        {
            await userRepository.UnfollowStudent(unfollowingStudent.Id, unfollowedStudent.Id);
        }

        private static Follow CreateFollow(Student followingStudent, Student followedStudent)
            => new ()
            {
                FollowingStudent = followingStudent,
                FollowedStudent = followedStudent
            };

        public async Task<bool> DoesProfessorExist(int id)
            => await userRepository.DoesProfessorExist(id);

        public async Task<bool> DoesCourseExist(int courseId)
            => await courseRepository.DoesCourseExist(courseId);

        public async Task<Course> GetCourse(int courseId)
            => await courseRepository.GetCourse(courseId);


        public async Task<Professor> GetProfessor(int professorId)
            => await userRepository.GetProfessor(professorId);

        public async Task AssignProfessor(Professor professor, Course course)
        {
            var courseProfessorAssignment = CreateCourseProfessorAssignment(professor, course);
            await userRepository.AssignProfessorToCourse(courseProfessorAssignment);
        }

        private CourseProfessorAssignment CreateCourseProfessorAssignment(Professor professor, Course course)
            => new()
            {
                Professor = professor,
                Course = course
            };

        public async Task<bool> DoesCourseProfessorAssignmentExist(int courseId, int professorId)
            => await userRepository.CourseProfessorAssignmentExist(courseId, professorId);

        public async  Task<List<AssignedProfessorModel>> GetAssignedProfessors(int courseId)
        {
            List<Professor> professors = await userRepository.GetAssignedProfessors(courseId);
            List<AssignedProfessorModel> models = new List<AssignedProfessorModel>();
            foreach (var professor in professors)
            {
                models.Add(MakeAssignedProfessorModel(professor));
            }
            return models;
        }

        private AssignedProfessorModel MakeAssignedProfessorModel(Professor professor)
            => new()
            {
                Id = professor.Id,
                Name = professor.UserName,
                Email = professor.Email
            };
    
        public async Task<StudentProfileModel> GetStudentAndConnectedData(int id)
        {
            var student = await userRepository.GetStudentAndConnectedData(id);
                
            var studentProfileModel = CreateStudentProfileModel(student);
            University university = null;
            if(await userRepository.DoesUniversityExist(student.UniversityId))
                university = await userRepository.GetUniversity(student.UniversityId);
            if(university != null)
                studentProfileModel.UniversityName = university.UserName;
            
            // if (student.Reviews.IsNullOrEmpty()) return studentProfileModel;

            foreach (var review in student.Reviews.OrderByDescending(i => i.LastModificationTime))
                studentProfileModel.Reviews.Add(CreateStudentProfileReview(review));
            
            
            studentProfileModel.Followers = new List<StudentGridModel>();
            foreach(var x in await userRepository.GetFollowers(id))
                studentProfileModel.Followers.Add(new StudentGridModel
                {
                    Id = x.Id,
                    Name = x.UserName,
                    Image = x.Image
                });
            
            studentProfileModel.Following = new List<StudentGridModel>();
            foreach(var x in await userRepository.GetFollowing(id))
                studentProfileModel.Following.Add(new StudentGridModel
                {
                    Id = x.Id,
                    Name = x.UserName,
                    Image = x.Image
                });
            
            return studentProfileModel;
        }
        
        private StudentProfileModel CreateStudentProfileModel(Student student)
            => new ()
            {
                Id = student.Id,
                ProfilePic = student.Image,
                Name = student.UserName,
                UniversityName = "",
                Reviews = new List<StudentProfileReview>(),
                NotificationSettings = MakeNotificationSettingsModel(student.NotificationSettings, student.Id),
                VerificationStatus = student.VerificationStatus
            };

        private NotificationSettingsModel MakeNotificationSettingsModel(NotificationSettings ns, int id)
            => new()
            {
                StudentId = id,
                ReceivesFollowersReviewMail = ns.ReceivesFollowersReviewMail,
                ReceivesFollowersReviewPush = ns.ReceivesFollowersReviewPush,
                ReceivesNewReplyMail = ns.ReceivesNewReplyMail,
                ReceivesNewReplyPush = ns.ReceivesNewReplyPush,
                ReceivesNewFollowerMail = ns.ReceivesNewFollowerMail,
                ReceivesNewFollowerPush = ns.ReceivesNewFollowerPush
            };
        private StudentProfileReview CreateStudentProfileReview(Review review)
            => new ()
            {
                Id = review.Id,
                Rating = review.Rating,
                Comment = review.Comment,
                StudentProfileReviewCourse = CreateStudentProfileReviewCourse(review.Course)
                
            };
        
        private static StudentProfileReviewCourse CreateStudentProfileReviewCourse(Course course)
            => new ()
            {
                Id = course.Id,
                Name = course.Name,
                Image = course.Image,
                Code = course.Code
            };
        
        private static ProfessorProfileModel CreateProfessorProfileModel(Professor professor)
            => new ()
            {
                Id = professor.Id,
                ProfilePic = professor.Image,
                Name = professor.UserName,
                Courses = new List<AssignedCourseModel>(),
            };
            
        public async Task<ProfessorProfileModel> GetProfessorAndConnectedData(int id)
            {
                var professor = await userRepository.GetProfessorAndConnectedData(id);

                var professorProfileModel = CreateProfessorProfileModel(professor);
                
                foreach (var course in await courseRepository.GetAssignedCourses(id))
                {
                    professorProfileModel.UniversityName = course.University.UserName;
                    professorProfileModel.Courses.Add(MakeAssignedCourseModel(course));
                }

                return professorProfileModel;
            }

            private AssignedCourseModel MakeAssignedCourseModel(Course course)
                => new()
                {
                    Id = course.Id,
                    AnonymousRating = course.AnonymousRating,
                    NonanonymousRating = course.NonanonymousRating,
                    Name = course.Name,
                    Code = course.Code,
                    Professor = course.Professor,
                    Image = course.Image
                };
                

            public void UpdateProfessor(Professor professor, ProfessorEditModel model)
        {
            professor.UserName = model.Name;
            professor.Image = model.Image;
        }
        public async Task PutProfessor(Professor professor)
            => await userRepository.PutProfessor(professor);

        public void UpdateStudent(Student student, StudentEditModel model)
        {
            student.UserName = model.Name;
            student.Image = model.Image;
            student.NotificationSettings = new()
            {
                ReceivesFollowersReviewMail = model.NotificationSettings.ReceivesFollowersReviewMail,
                ReceivesFollowersReviewPush = model.NotificationSettings.ReceivesFollowersReviewPush,
                ReceivesNewReplyMail = model.NotificationSettings.ReceivesNewReplyMail,
                ReceivesNewReplyPush = model.NotificationSettings.ReceivesNewReplyPush,
                ReceivesNewFollowerMail = model.NotificationSettings.ReceivesNewFollowerMail,
                ReceivesNewFollowerPush = model.NotificationSettings.ReceivesNewFollowerPush
            };
        }

        public async Task PutStudent(Student student)
            => await userRepository.PutStudent(student);

        public async Task<AssignedProfessorModel> getAssignedProfessor(string email)
        {
            var professor = await userRepository.GetProfessor(email);
            return MakeAssignedProfessorModel(professor);
        }

        public async Task<bool> DoesProfessorExist(string email)
            => await userRepository.DoesProfessorExist(email);

        public async Task<List<StudentGridModel>> GetFollowedStudents(int userId)
        {
            List<StudentGridModel> models = new List<StudentGridModel>();
            var followedStudents = await userRepository.GetFollowing(userId);
            foreach (var x in followedStudents)
            {
                models.Add(CreateStudentGridModel(x));
            }

            return models;
        }
        
        public async Task<List<StudentGridModel>> GetFollowers(int userId)
        {
            List<StudentGridModel> models = new List<StudentGridModel>();
            var followedStudents = await userRepository.GetFollowers(userId);
            foreach (var x in followedStudents)
            {
                models.Add(CreateStudentGridModel(x));
            }

            return models;
        }

        private StudentGridModel CreateStudentGridModel(Student student)
            => new()
            {
                Id = student.Id,
                Name = student.UserName!,
                Image = student.Image
            };

        public async Task<StudentGridModel> GetStudentListItem(int id)
        {
            Student student = await userRepository.GetStudent(id);
            return CreateStudentGridModel(student);
        }

        public async Task<List<UniversityNameModel>> GetUniversities()
        {
            var models = new List<UniversityNameModel>();
            var universities = await userRepository.GetUniversities();
            foreach(var x in universities)
            {
                models.Add(new UniversityNameModel() { Id = x.Id, Name = x.UserName });
            }

            return models;
        }
    }
}