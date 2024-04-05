namespace UniUnboxdAPI.Models.DataTransferObjects.StudentHomePage
{
    public class ReviewGridModel
    {
        public required int Id { get; set; }
        public required string CourseName { get; set; }
        public required string CourseImage { get; set; }
        public required int StudentId { get; set; }
        public required string StudentName { get; set; }
        public required string StudentImage { get; set; }
        public required double Rating { get; set;}
    }
}
