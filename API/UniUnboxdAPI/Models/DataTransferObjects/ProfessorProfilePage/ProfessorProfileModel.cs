namespace UniUnboxdAPI.Models.DataTransferObjects;

// Represents the data transfer object for a professor's profile page
public class ProfessorProfileModel
{
    // The unique identifier for the professor
    public required int Id { get; set; }

    // The URL of the professor's profile picture
    public string? ProfilePic { get; set; } 

    // The name of the professor
    public required string Name { get; set; }
    
    // The name of the university the professor belongs to
    public string? UniversityName { get; set; }
    
    // The list of courses assigned to the professor
    public required List<AssignedCourseModel> Courses { get; set; }
    
}