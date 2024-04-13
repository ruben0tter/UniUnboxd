// StudentProfileModel represents the data transfer object for a student's profile.
namespace UniUnboxdAPI.Models.DataTransferObjects;

public class StudentProfileModel
{
    // Id represents the unique identifier of the student profile.
    public required int Id { get; set; }

    // ProfilePic represents the URL of the student's profile picture.
    public string? ProfilePic { get; set; } 

    // Name represents the name of the student.
    public required string Name { get; set; }

    // UniversityName represents the name of the university the student is associated with.
    public string? UniversityName { get; set; }

    // VerificationStatus represents the verification status of the student's profile.
    public required VerificationStatus VerificationStatus { get; set; }

    // NotificationSettings represents the notification settings for the student.
    public NotificationSettingsModel NotificationSettings { get; set; }

    // Followers represents the collection of students who are following this student.
    public ICollection<StudentGridModel> Followers { get; set; }

    // Following represents the collection of students whom this student is following.
    public ICollection<StudentGridModel> Following { get; set; }

    // Reviews represents the collection of reviews for the student's profile.
    public ICollection<StudentProfileReview>? Reviews { get; set; }
}