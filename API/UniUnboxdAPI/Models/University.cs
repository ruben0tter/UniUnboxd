// This file contains the definition of the University class, which represents a university entity in the UniUnboxdAPI.
// It inherits from the User class and includes properties for courses and students associated with the university.

namespace UniUnboxdAPI.Models
{
    public class University : User
    {
        public ICollection<Course>? Courses { get; set; } // Represents the collection of courses offered by the university.
        public ICollection<Student>? Students { get; set; } // Represents the collection of students enrolled in the university.
    }
}
