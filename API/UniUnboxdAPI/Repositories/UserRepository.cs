using Microsoft.EntityFrameworkCore;
using UniUnboxdAPI.Data;
using UniUnboxdAPI.Models;

namespace UniUnboxdAPI.Repositories;

/// <summary>
/// Handles all calls to database in regard to User model.
/// </summary>
public class UserRepository(UniUnboxdDbContext dbContext)
{
    /// <summary>
    /// Retrieves a user by their ID.
    /// </summary>
    /// <param name="id">The user's ID.</param>
    /// <returns>A user if found; otherwise null.</returns>
    public async Task<User?> GetUser(int id)
        => await dbContext.Users.Where(i => i.Id == id).FirstOrDefaultAsync();

    /// <summary>
    /// Retrieves a user by their email.
    /// </summary>
    /// <param name="email">The user's email.</param>
    /// <returns>The user associated with the given email.</returns>
    public async Task<User> GetUser(string email)
        => await dbContext.Users.Where(i => i.Email!.Equals(email)).FirstAsync();

    /// <summary>
    /// Sets the verification status of a user.
    /// </summary>
    /// <param name="user">The user to update.</param>
    /// <param name="status">The new verification status.</param>
    /// <returns>True if the operation was successful.</returns>
    public async Task<bool> SetVerificationStatus(User user, VerificationStatus status)
    {
        user.VerificationStatus = status;
        dbContext.Users.Update(user);
        await dbContext.SaveChangesAsync();
        return true;
    }

    /// <summary>
    /// Gets the profile image of a user based on their ID and type.
    /// </summary>
    /// <param name="id">The user's ID.</param>
    /// <param name="type">The type of the user.</param>
    /// <returns>The image URL if available; otherwise null.</returns>
    public async Task<string?> GetImageOf(int id, UserType type)
    {
        switch (type)
        {
            case UserType.Student:
                var student = await dbContext.Students.Where(i => i.Id == id).FirstAsync();
                return student.Image;
            case UserType.Professor:
                var professor = await dbContext.Professors.Where(i => i.Id == id).FirstAsync();
                return professor.Image;
            default:
                return null;
        }
    }

    /// <summary>
    /// Checks if a student exists in the database.
    /// </summary>
    /// <param name="id">The student's ID.</param>
    /// <returns>True if the student exists; otherwise false.</returns>
    public async Task<bool> DoesStudentExist(int id)
        => await dbContext.Students.AnyAsync(c => c.Id == id);

    /// <summary>
    /// Retrieves a student by their ID, including their notification settings and followers.
    /// </summary>
    /// <param name="id">The student's ID.</param>
    /// <returns>The student if found.</returns>
    public async Task<Student> GetStudent(int id)
        => await dbContext.Students.Where(i => i.Id == id)
                .Include(i => i.NotificationSettings)
                .Include(i => i.Followers)
                    .ThenInclude(i => i.FollowingStudent)
                        .ThenInclude(i => i.NotificationSettings)
                .FirstAsync();

    /// <summary>
    /// Retrieves a student and their connected data, including notifications, following, followers, and reviews.
    /// </summary>
    /// <param name="id">The student's ID.</param>
    /// <returns>The student along with their connected data.</returns>
    public async Task<Student> GetStudentAndConnectedData(int id)
        => await dbContext.Students.Where(i => i.Id == id)
            .Include(i => i.NotificationSettings)
            .Include(i => i.Following)
            .Include(i => i.Followers)
            .Include(i => i.Reviews)
                .ThenInclude(i => i.Course)
            .FirstAsync();

    /// <summary>
    /// Updates the device token for a student.
    /// </summary>
    /// <param name="studentId">The student's ID.</param>
    /// <param name="deviceToken">The new device token.</param>
    public async Task SetDeviceToken(int studentId, string deviceToken)
    {
        var student = await GetStudent(studentId);
        student.DeviceToken = deviceToken;
        await dbContext.SaveChangesAsync();
    }

    /// <summary>
    /// Checks if one student follows another.
    /// </summary>
    /// <param name="followingStudentId">The ID of the following student.</param>
    /// <param name="followedStudentId">The ID of the followed student.</param>
    /// <returns>True if the follow relationship exists; otherwise false.</returns>
    public async Task<bool> DoesStudentFollowStudent(int followingStudentId, int followedStudentId)
    {
        var followingStudent = await dbContext.Students.Where(i => i.Id == followingStudentId).Include(i => i.Following).FirstAsync();
        return followingStudent.Following.Any(i => i.FollowedStudentId == followedStudentId);
    }

    /// <summary>
    /// Creates a follow relationship between two students.
    /// </summary>
    /// <param name="followRelation">The follow relationship to add.</param>
    public async Task FollowStudent(Follow followRelation)
    {
        await dbContext.Follows.AddAsync(followRelation);
        await dbContext.SaveChangesAsync();
    }

    /// <summary>
    /// Removes a follow relationship between two students.
    /// </summary>
    /// <param name="unfollowingStudentId">The ID of the unfollowing student.</param>
    /// <param name="unfollowedStudentId">The ID of the unfollowed student.</param>
    public async Task UnfollowStudent(int unfollowingStudentId, int unfollowedStudentId)
    {
        dbContext.Follows.Remove(dbContext.Follows.First(i => i.FollowingStudentId == unfollowingStudentId && i.FollowedStudentId == unfollowedStudentId));
        await dbContext.SaveChangesAsync();
    }

    /// <summary>
    /// Updates a student's information in the database.
    /// </summary>
    /// <param name="student">The student to update.</param>
    public async Task PutStudent(Student student)
    {
        dbContext.Students.Update(student);
        await dbContext.SaveChangesAsync();
    }

    /// <summary>
    /// Retrieves all followers of a student.
    /// </summary>
    /// <param name="id">The student's ID.</param>
    /// <returns>A collection of students who follow the specified student.</returns>
    public async Task<ICollection<Student>> GetFollowers(int id)
        => await dbContext.Follows.Where(i => i.FollowedStudent.Id == id).Select(i => i.FollowingStudent).ToListAsync();

    /// <summary>
    /// Retrieves all students that a student is following.
    /// </summary>
    /// <param name="id">The following student's ID.</param>
    /// <returns>A collection of students that the specified student is following.</returns>
    public async Task<ICollection<Student>> GetFollowing(int id)
        => await dbContext.Follows.Where(i => i.FollowingStudent.Id == id).Select(i => i.FollowedStudent).ToListAsync();

    /// <summary>
    /// Checks if a university exists in the database.
    /// </summary>
    /// <param name="id">The university's ID.</param>
    /// <returns>True if the university exists; otherwise false.</returns>
    public async Task<bool> DoesUniversityExist(int id)
        => await dbContext.Universities.AnyAsync(c => c.Id == id);

    /// <summary>
    /// Retrieves a university by ID.
    /// </summary>
    /// <param name="id">The university's ID.</param>
    /// <returns>The university if found.</returns>
    public async Task<University> GetUniversity(int id)
        => await dbContext.Universities.Where(i => i.Id == id).FirstAsync();

    /// <summary>
    /// Retrieves all universities in the database.
    /// </summary>
    /// <returns>A list of all universities.</returns>
    public async Task<List<University>> GetUniversities()
        => await dbContext.Universities.ToListAsync();

    /// <summary>
    /// Checks if a professor exists in the database by ID.
    /// </summary>
    /// <param name="id">The professor's ID.</param>
    /// <returns>True if the professor exists; otherwise false.</returns>
    public async Task<bool> DoesProfessorExist(int id)
        => await dbContext.Professors.AnyAsync(c => c.Id == id);

    /// <summary>
    /// Checks if a professor exists in the database by email.
    /// </summary>
    /// <param name="email">The professor's email.</param>
    /// <returns>True if the professor exists; otherwise false.</returns>
    public async Task<bool> DoesProfessorExist(string email)
        => await dbContext.Professors.AnyAsync(i => i.NormalizedEmail == email.ToUpper());

    /// <summary>
    /// Retrieves a professor by ID.
    /// </summary>
    /// <param name="id">The professor's ID.</param>
    /// <returns>The professor if found.</returns>
    public async Task<Professor> GetProfessor(int id)
        => await dbContext.Professors.Where(i => i.Id == id).FirstAsync();

    /// <summary>
    /// Retrieves a professor by email.
    /// </summary>
    /// <param name="email">The professor's email.</param>
    /// <returns>The professor if found.</returns>
    public async Task<Professor> GetProfessor(string email)
        => await dbContext.Professors.Where(i => i.NormalizedEmail == email.ToUpper()).FirstAsync();

    /// <summary>
    /// Retrieves a professor and their connected data, including assigned courses.
    /// </summary>
    /// <param name="id">The professor's ID.</param>
    /// <returns>The professor along with their connected data.</returns>
    public async Task<Professor> GetProfessorAndConnectedData(int id)
        => await dbContext.Professors.Where(i => i.Id == id)
            .Include(i => i.AssignedCourses)
                .ThenInclude(i => i.Course)
                    .ThenInclude(i => i.University)
            .FirstAsync();

    /// <summary>
    /// Updates a professor's information in the database.
    /// </summary>
    /// <param name="professor">The professor to update.</param>
    public async Task PutProfessor(Professor professor)
    {
        dbContext.Professors.Update(professor);
        await dbContext.SaveChangesAsync();
    }

    /// <summary>
    /// Retrieves all professors assigned to a specific course.
    /// </summary>
    /// <param name="id">The course ID.</param>
    /// <returns>A list of professors assigned to the specified course.</returns>
    public async Task<List<Professor>> GetAssignedProfessors(int id)
        => await dbContext.Professors.Where(i => i.AssignedCourses.Any(i => i.Course.Id == id)).ToListAsync();

    /// <summary>
    /// Checks if an assignment between a course and a professor exists.
    /// </summary>
    /// <param name="courseId">The course ID.</param>
    /// <param name="professorId">The professor ID.</param>
    /// <returns>True if the assignment exists; otherwise false.</returns>
    public async Task<bool> DoesCourseProfessorAssignmentExist(int courseId, int professorId)
        => await dbContext.CourseProfessorAssignments.AnyAsync(i => i.Professor.Id == professorId && i.Course.Id == courseId);

    /// <summary>
    /// Retrieves a specific course-professor assignment.
    /// </summary>
    /// <param name="professorId">The professor ID.</param>
    /// <param name="courseId">The course ID.</param>
    /// <returns>The course-professor assignment if found.</returns>
    public async Task<CourseProfessorAssignment> GetProfessorAssignment(int professorId, int courseId)
        => await dbContext.CourseProfessorAssignments.FirstAsync(i => i.Professor.Id == professorId && i.Course.Id == courseId);

    /// <summary>
    /// Assigns a professor to a course.
    /// </summary>
    /// <param name="courseProfessorAssignment">The course-professor assignment to add.</param>
    public async Task AssignProfessorToCourse(CourseProfessorAssignment courseProfessorAssignment)
    {
        dbContext.CourseProfessorAssignments.Add(courseProfessorAssignment);
        await dbContext.SaveChangesAsync();
    }

    /// <summary>
    /// Dismisses a professor from a course.
    /// </summary>
    /// <param name="courseProfessorAssignment">The course-professor assignment to remove.</param>
    public async Task DismissProfessorFromCourse(CourseProfessorAssignment courseProfessorAssignment)
    {
        dbContext.CourseProfessorAssignments.Remove(courseProfessorAssignment);
        await dbContext.SaveChangesAsync();
    }
}