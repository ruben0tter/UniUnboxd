// This class represents the data transfer object for an assigned course on a professor's profile page.
namespace UniUnboxdAPI.Models.DataTransferObjects;

public class AssignedCourseModel
{
    // The unique identifier of the assigned course.
    public required int Id { get; set; }

    // The anonymous rating of the assigned course.
    public required double AnonymousRating { get; set; }

    // The non-anonymous rating of the assigned course.
    public required double NonanonymousRating { get; set; }

    // The name of the assigned course.
    public required string Name { get; set; }

    // The code of the assigned course.
    public required string Code { get; set; }

    // The university offering the assigned course.
    public required string University { get; set; }

    // The image associated with the assigned course (optional).
    public string? Image { get; set; }
}