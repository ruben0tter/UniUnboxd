using System.Collections;

namespace UniUnboxdAPI.Models;

public class CourseProfessorAssignment
{
    public int ProfessorId { get; set; }
    public int CourseId { get; set; }
    public required Professor Professor { get; set; }
    public required Course Course { get; set; }
}