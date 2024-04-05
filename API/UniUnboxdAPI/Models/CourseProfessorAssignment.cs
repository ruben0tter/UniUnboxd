namespace UniUnboxdAPI.Models;

/// <summary>
/// Represents an assignment of a professor to a course.
/// </summary>
public class CourseProfessorAssignment
{
    // Gets or sets the ID of the professor.
    public int ProfessorId { get; set; }

    // Gets or sets the ID of the course.
    public int CourseId { get; set; }

    // Gets or sets the professor assigned to the course.
    public required Professor Professor { get; set; }

    // Gets or sets the course to which the professor is assigned.
    public required Course Course { get; set; }
}