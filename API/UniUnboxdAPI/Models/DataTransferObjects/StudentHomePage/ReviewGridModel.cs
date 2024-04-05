namespace UniUnboxdAPI.Models.DataTransferObjects.StudentHomePage
{
    // Represents the model for the review grid on the student home page.
    public class ReviewGridModel
    {
        // The unique identifier of the review.
        public required int Id { get; set; }

        // The name of the course associated with the review.
        public required string CourseName { get; set; }

        // The image URL of the course associated with the review.
        public required string CourseImage { get; set; }

        // The unique identifier of the student who wrote the review.
        public required int StudentId { get; set; }

        // The name of the student who wrote the review.
        public required string StudentName { get; set; }

        // The image URL of the student who wrote the review.
        public required string StudentImage { get; set; }

        // The rating given by the student for the course.
        public required double Rating { get; set; }
    }
}
