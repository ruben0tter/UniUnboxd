namespace UniUnboxdAPI.Models.DataTransferObjects.ReviewPage
{
    public class ReviewReplyModel
    {
        public required string Text { get; set; }
        public required UserHeaderModel UserHeader { get; set; }
    }
}
