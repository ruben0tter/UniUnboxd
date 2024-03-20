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
            UniversityName = student.University?.UserName,
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
}