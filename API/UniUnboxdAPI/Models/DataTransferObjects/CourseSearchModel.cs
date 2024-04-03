namespace UniUnboxdAPI.Models.DataTransferObjects;

public class CourseSearchModel
{
    public required int Id { get; set; }
    public required string Name { get; set; }
    public required string Code { get; set; }
    public required string University { get; set; }
    public required int UniversityId { get; set; }
    public required string Professor { get; set; }
    public string? Image { get; set; }
    public double AnonymousRating { get; set; }
    public double NonanonymousRating { get; set; }
}
