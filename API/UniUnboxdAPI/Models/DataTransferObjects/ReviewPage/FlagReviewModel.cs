namespace UniUnboxdAPI.Models.DataTransferObjects.ReviewPage
{
    // Represents the model for flagging a review
    public class FlagReviewModel
    {
        // The ID of the review to be flagged
        public required int ReviewId { get; set; }

        // The message explaining the reason for flagging the review
        public required string Message { get; set; }
    }
}
