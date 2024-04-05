// This file contains the definition of the CourseEditModel class.
// The CourseEditModel class represents the data transfer object for editing a course.

namespace UniUnboxdAPI.Models.DataTransferObjects
{
    // The CourseEditModel class represents the data transfer object for editing a course.
    public class CourseEditModel
    {
        // The Id property represents the unique identifier of the course.
        public required int Id { get; set; }

        // The Name property represents the name of the course.
        public required string Name { get; set; }

        // The Code property represents the code of the course.
        public required string Code { get; set; }

        // The Description property represents the description of the course.
        public required string Description { get; set; }

        // The Professor property represents the professor of the course.
        public required string Professor { get; set; }

        // The Image property represents the image of the course.
        public string? Image { get; set; }

        // The Banner property represents the banner of the course.
        public string? Banner { get; set; } 

        // The AssignedProfessors property represents the list of assigned professors for the course.
        public required List<AssignedProfessorModel> AssignedProfessors { get; set; }
    }
}