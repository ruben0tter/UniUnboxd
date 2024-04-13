namespace UniUnboxdAPI.Models.DataTransferObjects
{
    // AdvancedCourseRetrievelHomeModel represents a data transfer object for retrieving advanced course information for the home page.
    public class AdvancedCourseRetrievelHomeModel
    {
        // Id represents the unique identifier of the course.
        public required int Id { get; set; }

        // CourseImage represents the image URL of the course.
        public required string? CourseImage { get; set; }

        // CourseName represents the name of the course.
        public required string? CourseName { get; set; }

        // Poster represents the review poster student model associated with the course.
        public ReviewPosterStudentModel? Poster { get; set; }
    }
}
