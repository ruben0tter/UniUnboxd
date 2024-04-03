namespace UniUnboxdAPI.Models.DataTransferObjects
{
    public class ReviewModel
    {
        public required double Rating { get; set; }
        public required string Comment { get; set; }
        public required bool IsAnonymous { get; set; }
        public required int CourseId { get; set; }
    }
}
