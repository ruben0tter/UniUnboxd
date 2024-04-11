namespace UniUnboxdAPI.Models.DataTransferObjects;

public class ReviewPosterStudentModel
{
    // Represents the model for a review poster student.
    public required int Id { get; set; }

    // Represents the username of the review poster student.
    public string? UserName { get; set; }

    // Represents the image of the review poster student.
    public string? Image { get; set; }
}