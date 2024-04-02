namespace UniUnboxdAPI.Models.DataTransferObjects;

public class ProfessorProfileModel
{
    public required int Id { get; set; }
    public string? ProfilePic { get; set; } 
    public required string Name { get; set; }
    
    public string? UniversityName { get; set; }
    
    public required List<AssignedCourseModel> Courses { get; set; }
    
}