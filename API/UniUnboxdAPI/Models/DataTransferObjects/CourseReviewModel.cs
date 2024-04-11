// This file contains the definition of the CourseReviewModel class.
// The CourseReviewModel class represents a data transfer object for a course review.
// It contains properties such as Id, Rating, Comment, IsAnonymous, CourseId, and Poster.

namespace UniUnboxdAPI.Models.DataTransferObjects;

public class CourseReviewModel
{
    // The unique identifier of the course review.
    public required int Id { get; set; }

    // The rating given to the course.
    public required double Rating { get; set; }

    // The comment provided for the course review.
    public string? Comment { get; set; }

    // Indicates whether the review is anonymous or not.
    public required bool IsAnonymous { get; set; }

    // The identifier of the course associated with the review.
    public required int CourseId { get; set; }

    // The student who posted the review.
    public ReviewPosterStudentModel? Poster { get; set; }
}