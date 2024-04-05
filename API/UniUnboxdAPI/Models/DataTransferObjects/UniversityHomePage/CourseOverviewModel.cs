namespace UniUnboxdAPI.Models.DataTransferObjects.UniversityHomePage
{
    public class CourseOverviewModel
    {
        // Represents the Course ID
        public required int Id { get; set; }

        // Represents the Course Name
        public required string Name { get; set; }

        // Represents the Course Code
        public required string Code { get; set; }

        // Represents the Course Professor
        public required string Professor { get; set; }

        // Represents the Course Image (optional)
        public string? Image { get; set; }
    }
}
