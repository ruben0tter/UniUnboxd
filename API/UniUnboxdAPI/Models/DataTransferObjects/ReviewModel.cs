namespace UniUnboxdAPI.Models.DataTransferObjects
{
    public class ReviewModel
    {
        public required double Rating { get; set; }
        public string? Comment { get; set; }
        public required bool IsAnonymous { get; set; }
        public required int CourseId { get; set; }
        public required int StudentId { get; set; }
    }
}
