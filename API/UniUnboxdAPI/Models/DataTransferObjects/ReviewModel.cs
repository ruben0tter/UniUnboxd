namespace UniUnboxdAPI.Models.DataTransferObjects
{
    // ReviewModel class represents a data transfer object for a review.
    public class ReviewModel
    {
        // Rating property represents the rating given for a course.
        public required double Rating { get; set; }

        // Comment property represents the comment given for a course.
        public required string Comment { get; set; }

        // IsAnonymous property indicates whether the review is anonymous or not.
        public required bool IsAnonymous { get; set; }

        // CourseId property represents the ID of the course being reviewed.
        public required int CourseId { get; set; }
    }
}
