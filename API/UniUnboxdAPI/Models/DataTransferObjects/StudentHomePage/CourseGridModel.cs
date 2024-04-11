namespace UniUnboxdAPI.Models.DataTransferObjects.StudentHomePage
{
    // Represents the data transfer object for the course grid on the student home page.

    public class CourseGridModel
    {
        // Gets or sets the ID of the course.
        public required int Id { get; set; }

        // Gets or sets the name of the course.
        public required string Name { get; set; }

        // Gets or sets the image URL of the course. Can be null.
        public string? Image { get; set; }
    }
}
