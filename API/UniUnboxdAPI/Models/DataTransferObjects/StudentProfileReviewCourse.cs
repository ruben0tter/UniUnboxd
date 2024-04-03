namespace UniUnboxdAPI.Models.DataTransferObjects;

public class StudentProfileReviewCourse
{
    public required int Id { get; set; }
    public required string Name { get; set; }
    public required string Code { get; set; }
    public string? Image { get; set; }
}