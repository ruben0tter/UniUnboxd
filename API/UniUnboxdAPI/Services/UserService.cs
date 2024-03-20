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
        
        if (student.Reviews.IsNullOrEmpty()) return studentProfileModel;
            
        foreach (var review in student.Reviews)
            studentProfileModel.Reviews.Add(CreateStudentProfileReview(review));

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
}