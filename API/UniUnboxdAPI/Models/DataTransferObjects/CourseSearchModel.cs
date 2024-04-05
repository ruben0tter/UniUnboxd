namespace UniUnboxdAPI.Models.DataTransferObjects;

public class CourseSearchModel
{
    // Represents the unique identifier of the course.
    public required int Id { get; set; }

    // Represents the name of the course.
    public required string Name { get; set; }

    // Represents the code of the course.
    public required string Code { get; set; }

    // Represents the university of the course.
    public required string University { get; set; }

    // Represents the unique identifier of the university.
    public required int UniversityId { get; set; }

    // Represents the professor of the course.
    public required string Professor { get; set; }

    // Represents the image of the course (optional).
    public string? Image { get; set; }

    // Represents the anonymous rating of the course.
    public double AnonymousRating { get; set; }

    // Represents the non-anonymous rating of the course.
    public double NonanonymousRating { get; set; }
}
