namespace UniUnboxdAPI.Models;

public class Course : Base
{
    public required string Name { get; set; }
    public required string Code { get; set; }
    public required string Description { get; set; }
    public required string Professor { get; set; }
    public string? Image { get; set; }
    public string? Banner { get; set; } 
    public double AnonymousRating { get; set; }
    public double NonanonymousRating { get; set; }
    public required List<Review> Reviews { get; set; }
    public required University University { get; set; }
    public ICollection<CourseProfessorAssignment> AssignedProfessors { get; set; }
}
