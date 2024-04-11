// This class represents a friend's review for a course page.
namespace UniUnboxdAPI.Models.DataTransferObjects.CoursePage
{
    // FriendReview class
    public class FriendReview
    {
        // The unique identifier of the friend review.
        public required int Id { get; set; }

        // The rating given by the friend for the course.
        public required double Rating { get; set; }

        // The name of the friend who wrote the review.
        public required string Name { get; set; }

        // The image associated with the friend's profile.
        public string? Image { get; set; }

        // Indicates whether the friend has provided a comment for the review.
        public required bool HasComment { get; set; }
    }
}
