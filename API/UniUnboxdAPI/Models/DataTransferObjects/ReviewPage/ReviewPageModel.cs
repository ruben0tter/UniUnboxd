namespace UniUnboxdAPI.Models.DataTransferObjects.ReviewPage
{
    public class ReviewPageModel
    {
        public required int Id { get; set; }
        public required DateTime Date { get; set; }
        public required double Rating { get; set; }
        public required string Comment { get; set; }
        public required bool IsAnonymous { get; set; }
        public required CourseHeaderModel CourseHeader { get; set; }
        public UserHeaderModel? StudentHeader { get; set; }
        public required ICollection<ReviewReplyModel> Replies { get; set; }
    }
}
