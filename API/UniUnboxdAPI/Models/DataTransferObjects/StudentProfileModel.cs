namespace UniUnboxdAPI.Models.DataTransferObjects;

public class StudentProfileModel
{
    public required int Id { get; set; }
    public string? ProfilePic { get; set; } 
    public required string Name { get; set; }
    public string? UniversityName { get; set; }
    public NotificationSettingsModel NotificationSettings { get; set; }
    public ICollection<StudentGridModel> Followers { get; set; }
    public ICollection<StudentGridModel> Following { get; set; }
    public ICollection<StudentProfileReview>? Reviews { get; set; }
}