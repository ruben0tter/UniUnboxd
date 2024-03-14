namespace UniUnboxdAPI.Models.DataTransferObjects
{
    public class AdvancedCourseRetrievelHomeModel
    {
        public required int Id { get; set; }
        public required string? CourseImage { get; set; }
        public required string? CourseName { get; set; }
        public ReviewPosterStudentModel? Poster { get; set; }
    }
}
