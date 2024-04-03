namespace UniUnboxdAPI.Models.DataTransferObjects;

public class AssignedCourseModel
{
    public required int Id { get; set; }
    public required double AnonymousRating { get; set; }
    public required double NonanonymousRating { get; set; }
    public required string Name { get; set; }
    public required string Code { get; set; }
    public required string University { get; set; }
    public string? Image { get; set; }
}