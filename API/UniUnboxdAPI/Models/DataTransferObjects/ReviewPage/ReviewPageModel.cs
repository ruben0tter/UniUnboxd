namespace UniUnboxdAPI.Models.DataTransferObjects.ReviewPage
{
    // Represents a model for a review page
    public class ReviewPageModel
    {
        // The ID of the review (required)
        public required int Id { get; set; }

        // The date of the review (required)
        public required DateTime Date { get; set; }

        // The rating of the review (required)
        public required double Rating { get; set; }

        // The comment of the review (required)
        public required string Comment { get; set; }

        // Indicates if the review is anonymous (required)
        public required bool IsAnonymous { get; set; }

        // The header of the course associated with the review (required)
        public required CourseHeaderModel CourseHeader { get; set; }

        // The header of the student who wrote the review (nullable)
        public UserHeaderModel? StudentHeader { get; set; }

        // The collection of replies to the review (required)
        public required ICollection<ReviewReplyModel> Replies { get; set; }

        // The count of likes for the review (required)
        public required int LikeCount { get; set; }

        // The collection of student IDs who liked the review (required)
        public required ICollection<int> StudentLikes { get; set; }
    }
}
