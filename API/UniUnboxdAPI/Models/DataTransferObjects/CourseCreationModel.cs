namespace UniUnboxdAPI.Models.DataTransferObjects;

// CourseCreationModel represents the data transfer object for creating a new course.
public class CourseCreationModel
{
    // Name of the course. Required field.
    public required string Name { get; set; }

    // Code of the course. Required field.
    public required string Code { get; set; }

    // Description of the course. Required field.
    public required string Description { get; set; }

    // Professor of the course. Required field.
    public required string Professor { get; set; }

    // Image URL of the course. Nullable field.
    public string? Image { get; set; }

    // Banner URL of the course. Nullable field.
    public string? Banner { get; set; } 

    // University ID of the course. Required field.
    public required int UniversityId { get; set; }
    
    // List of assigned professors for the course. Required field.
    public required List<AssignedProfessorModel> AssignedProfessors { get; set; }
}