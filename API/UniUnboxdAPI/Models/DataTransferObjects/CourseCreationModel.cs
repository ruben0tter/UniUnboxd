namespace UniUnboxdAPI.Models.DataTransferObjects;

public class CourseCreationModel
{
    public required string Name { get; set; }
    public required string Code { get; set; }
    public required string Description { get; set; }
    public required string Professor { get; set; }
    public string? Image { get; set; }
    public string? Banner { get; set; } 
    public required int UniversityId { get; set; }
}