using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;

namespace UniUnboxdAPI.Services
{
    public class UserService(UserRepository userRepository, CourseRepository courseRepository)
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
        /// Student 1 unfollows student 2.
        /// </summary>
        /// <param name="unfollowingStudent">Provided student 1.</param>
        /// <param name="unfollowedStudent">Provided student 2.</param>
        /// <returns>No object or value is returned by this method when it completes.</returns>
        public async Task UnfollowStudent(Student unfollowingStudent, Student unfollowedStudent)
        {
            var followModel = CreateFollow(unfollowingStudent, unfollowedStudent);
            await userRepository.UnfollowStudent(followModel);
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
                models.Add(new AssignedProfessorModel
                {
                    Id = professor.Id,
                    Name = professor.UserName,
                    Email = professor.Email,
                    Image = professor.Image
                });
            }
            return models;
        }

        public async Task DismissProfessor(Professor professor, Course course)
        {
            var courseProfessorAssignment = CreateCourseProfessorAssignment(professor, course);
            await userRepository.DismissProfessorFromCourse(courseProfessorAssignment);        }
    }
}
