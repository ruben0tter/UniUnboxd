// This file contains the definition of the Professor class, which represents a professor in the UniUnboxdAPI.
namespace UniUnboxdAPI.Models
{
    // The Professor class inherits from the User class.
    public class Professor : User
    {
        // The Image property represents the image of the professor.
        public string? Image { get; set; }
        
        // The AssignedCourses property represents the collection of courses assigned to the professor.
        public ICollection<CourseProfessorAssignment>? AssignedCourses { get; set; }
        
        // The Replies property represents the collection of replies made by the professor.
        public ICollection<Reply>? Replies { get; set; }
    }
}
