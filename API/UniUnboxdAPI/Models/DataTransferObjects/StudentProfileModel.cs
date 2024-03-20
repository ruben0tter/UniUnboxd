namespace UniUnboxdAPI.Models.DataTransferObjects;

public class StudentProfileModel
{
    public required int Id { get; set; }
    public string? ProfilePic { get; set; } 
    public required string Name { get; set; }
    public string? UniversityName { get; set; }
    //TODO: add following and followers
    public ICollection<StudentProfileReview>? Reviews { get; set; }
    
}