namespace UniUnboxdAPI.Models.DataTransferObjects;

public class AssignedCourseModel
{
    public required int Id { get; set; }
    public required double Rating { get; set; }
    public required string Name { get; set; }
    public required string Code { get; set; }
    public required string Professor { get; set; }
    public string? Image { get; set; }
}