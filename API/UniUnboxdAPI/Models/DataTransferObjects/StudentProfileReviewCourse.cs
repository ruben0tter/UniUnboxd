namespace UniUnboxdAPI.Models.DataTransferObjects;

public class StudentProfileReviewCourse
{
    // Represents the unique identifier of the course.
    public required int Id { get; set; }

    // Represents the name of the course.
    public required string Name { get; set; }

    // Represents the code of the course.
    public required string Code { get; set; }

    // Represents the image associated with the course, if available.
    public string? Image { get; set; }
}