namespace UniUnboxdAPI.Models.DataTransferObjects;

public class CourseReviewModel
{
    public required int Id { get; set; }
    public required double Rating { get; set; }
    public string? Comment { get; set; }
    public required bool IsAnonymous { get; set; }
    public required int CourseId { get; set; }
    public ReviewPosterStudentModel? Poster { get; set; }
}