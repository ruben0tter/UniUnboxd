namespace UniUnboxdAPI.Models.DataTransferObjects
{
    public class BasicCourseRetrievalHome
    {
        public required int Id { get; set; }
        public required string? CourseImage { get; set; }
        public required string? CourseName { get; set; }
    }
}
