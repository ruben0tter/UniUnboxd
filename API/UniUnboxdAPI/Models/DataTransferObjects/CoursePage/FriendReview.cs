namespace UniUnboxdAPI.Models.DataTransferObjects.CoursePage
{
    public class FriendReview
    {
        public required int Id { get; set; }
        public required double Rating { get; set; }
        public required string Name { get; set; }
        public string? Image { get; set; }
        public required bool HasComment { get; set; }
    }
}
