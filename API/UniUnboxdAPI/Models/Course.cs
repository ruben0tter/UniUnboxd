
/// <summary>
/// Represents a course in the UniUnboxd application.
/// </summary>
namespace UniUnboxdAPI.Models;

public class Course : Base
{
    // The name of the course.
    public required string Name { get; set; }

    // The code of the course.
    public required string Code { get; set; }

    // The description of the course.
    public required string Description { get; set; }

    // The professor of the course.
    public required string Professor { get; set; }

    // The image associated with the course.
    public string? Image { get; set; }

    // The banner image associated with the course.
    public string? Banner { get; set; } 

    // The anonymous rating of the course.
    public double AnonymousRating { get; set; }

    // The non-anonymous rating of the course.
    public double NonanonymousRating { get; set; }

    // The reviews of the course.
    public required List<Review> Reviews { get; set; }

    // The university associated with the course.
    public required University University { get; set; }

    // The professors assigned to the course.
    public ICollection<CourseProfessorAssignment> AssignedProfessors { get; set; }
}
