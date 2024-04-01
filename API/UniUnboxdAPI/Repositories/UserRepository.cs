using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;

namespace UniUnboxdAPI.Repositories;

/// <summary>
/// Handles all calls to database in regard to User model.
/// </summary>
public class UserRepository {
        private readonly UniUnboxdDbContext dbContext;

        public UserRepository(UniUnboxdDbContext dbContext)
        {
            this.dbContext = dbContext;
        }

        public async Task<User> GetUser(int id)
            => await dbContext.Users.Where(i => i.Id == id).FirstAsync();

        public async Task<User> GetUser(string email)
            => await dbContext.Users.Where(i => i.Email!.Equals(email)).FirstAsync();

        public async Task<bool> DoesStudentExist(int id)
            => await dbContext.Students.AnyAsync(c => c.Id == id);

        public async Task<Student> GetStudent(int id)
            => await dbContext.Students.Where(i => i.Id == id)
                    .Include(i => i.NotificationSettings)
                    .Include(i => i.Followers)!
                    .ThenInclude(i => i.FollowingStudent)
                    .ThenInclude(i => i.NotificationSettings)
                    .FirstAsync();

        public async Task<bool> DoesUniversityExist(int id)
            => await dbContext.Universities.AnyAsync(c => c.Id == id);

        public async Task<University> GetUniversity(int id)
            => await dbContext.Universities.Where(i => i.Id == id).FirstAsync();

        public async Task<bool> DoesProfessorExist(int id)
            => await dbContext.Professors.AnyAsync(c => c.Id == id);
        
        public async Task<Student> GetStudentAndConnectedData(int id)
            => await dbContext.Students.Where(i => i.Id == id)
                .Include(i => i.Following)
                .Include(i => i.Followers)
                .Include(i => i.Reviews)!
                .ThenInclude(i => i.Course)
                .FirstAsync(); 
        
        public async Task<Professor> GetProfessorAndConnectedData(int id)
            => await dbContext.Professors.Where(i => i.Id == id)
                .Include(i => i.AssignedCourses)
                .ThenInclude(i => i.Course)
                .ThenInclude(i => i.University)
                .FirstAsync(); 

        public async Task SetDeviceToken(int studentId, string deviceToken)
        {
            var student = await GetStudent(studentId);
            student.DeviceToken = deviceToken;
            await dbContext.SaveChangesAsync();
        }

        public async Task<bool> DoesStudentFollowStudent(int followingStudentId, int followedStudentId)
        {
            var followingStudent = await dbContext.Students.Where(i => i.Id == followingStudentId).Include(i => i.Following).FirstAsync();
            return followingStudent.Following!.Any(i => i.FollowedStudentId == followedStudentId);
        }

        public async Task FollowStudent(Follow followRelation)
        {
            await dbContext.Follows.AddAsync(followRelation);
            await dbContext.SaveChangesAsync();
        }

        public async Task UnfollowStudent(int unfollowingStudentId, int unfollowedStudentId)
        {
            dbContext.Follows.Remove(dbContext.Follows.Where(i => i.FollowingStudentId == unfollowingStudentId  && i.FollowedStudentId == unfollowedStudentId).First());
            await dbContext.SaveChangesAsync();
        }

        public async Task<Professor> GetProfessor(int id)
            => await dbContext.Professors.Where(i => i.Id == id).FirstAsync();

        public async Task PutProfessor(Professor professor)
        {
            dbContext.Professors.Update(professor);
            await dbContext.SaveChangesAsync();
        }

        public async Task PutStudent(Student student)
        {
            dbContext.Students.Update(student);
            await dbContext.SaveChangesAsync();
        }
        public async Task<ICollection<Student>> GetFollowers(int id)
            => await dbContext.Follows.Where(i => i.FollowedStudent.Id == id).Select(i => i.FollowingStudent).ToListAsync();

        public async Task<ICollection<Student>> GetFollowing(int id)
            => await dbContext.Follows.Where(i => i.FollowingStudent.Id == id).Select(i => i.FollowedStudent)
                .ToListAsync();

        public async Task<List<Professor>> GetAssignedProfessors(int id)
            => await dbContext.Professors.Where(i => i.AssignedCourses.Any(i => i.Course.Id == id)).ToListAsync();

        public async Task AssignProfessorToCourse(CourseProfessorAssignment courseProfessorAssignment)
        {
            dbContext.CourseProfessorAssignments.Add(courseProfessorAssignment);
            await dbContext.SaveChangesAsync();
        }

        public async Task<bool> CourseProfessorAssignmentExist(int courseId, int professorId)
            => await dbContext.CourseProfessorAssignments.AnyAsync(i =>
                i.Professor.Id == professorId && i.Course.Id == courseId);

        public async Task DismissProfessorFromCourse(CourseProfessorAssignment courseProfessorAssignment)
        {
            dbContext.CourseProfessorAssignments.Remove(courseProfessorAssignment);
            await dbContext.SaveChangesAsync();        
        }

        public async Task<Professor> GetProfessor(string email)
            => await dbContext.Professors.Where(i => i.NormalizedEmail == email.ToUpper()).FirstAsync();

        public async Task<bool> DoesProfessorExist(string email)
            => await dbContext.Professors.AnyAsync(i => i.NormalizedEmail == email.ToUpper());

        public async Task<CourseProfessorAssignment> GetProfessorAssignment(int professorId, int courseId)
            => await dbContext.CourseProfessorAssignments.FirstAsync(i =>
                        i.Professor.Id == professorId && i.Course.Id == courseId);
}
