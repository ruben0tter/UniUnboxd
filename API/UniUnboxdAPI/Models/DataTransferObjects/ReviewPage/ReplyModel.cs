namespace UniUnboxdAPI.Models.DataTransferObjects.ReviewPage
{
    // Definition of the ReplyModel class
    public class ReplyModel
    {
        // Property representing the text of the reply
        public required string Text { get; set; }

        // Property representing the ID of the review the reply belongs to
        public required int ReviewId { get; set; }
    }
}
