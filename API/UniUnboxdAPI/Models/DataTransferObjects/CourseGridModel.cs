namespace UniUnboxdAPI.Models.DataTransferObjects;

public class CourseGridModel
{
    public required int Id { get; set; }
    public double Rating { get; set; }
    public required string Name { get; set; }
    public string? Image { get; set; }
}
