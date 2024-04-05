namespace UniUnboxdAPI.Models.DataTransferObjects.ReviewPage
{
    // Represents a model for a review reply
    public class ReviewReplyModel
    {
        // The text of the review reply
        public required string Text { get; set; }

        // The header information of the user who made the review reply
        public required UserHeaderModel UserHeader { get; set; }
    }
}
