using Microsoft.IdentityModel.Tokens;
using UniUnboxdAPI.Models;
using UniUnboxdAPI.Models.DataTransferObjects;
using UniUnboxdAPI.Repositories;

namespace UniUnboxdAPI.Services;

public class UserService
{
    private readonly UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    
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
    
    private static StudentProfileModel CreateStudentProfileModel(Student student)
        => new ()
        {
            Id = student.Id,
            ProfilePic = student.Image,
            Name = student.UserName,
            UniversityName = "",
            Reviews = new List<StudentProfileReview>()
            
        };
    
    private static StudentProfileReview CreateStudentProfileReview(Review review)
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
            
        };
        
    public async Task<ProfessorProfileModel> GetProfessorAndConnectedData(int id)
        {
            var professor = await userRepository.GetProfessorAndConnectedData(id);

            var professorProfileModel = CreateProfessorProfileModel(professor);

            return professorProfileModel;
        }
        
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

    public async  Task<Professor> GetProfessor(int id)
        => await userRepository.GetProfessor(id);

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
    }

    public async Task PutStudent(Student student)
        => await userRepository.PutStudent(student);
}
