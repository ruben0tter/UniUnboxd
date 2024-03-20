namespace UniUnboxdAPI.Models.DataTransferObjects;

public class StudentProfileReview
{
    public required int Id { get; set; }
    public required double Rating { get; set; }
    public string? Comment { get; set; }
    public required StudentProfileReviewCourse StudentProfileReviewCourse { get; set; }
    
}