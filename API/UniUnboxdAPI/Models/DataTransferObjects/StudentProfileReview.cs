// This file contains the definition of the StudentProfileReview class.
// It is located at /c:/Users/20221723/OneDrive - TU Eindhoven/Documents/TUe/Y2/Q3/DBL_App_Dev/UniUnboxd/API/UniUnboxdAPI/Models/DataTransferObjects/StudentProfileReview.cs

namespace UniUnboxdAPI.Models.DataTransferObjects;

public class StudentProfileReview
{
    // The unique identifier of the student profile review.
    public required int Id { get; set; }

    // The rating given to the student profile.
    public required double Rating { get; set; }

    // The comment provided for the student profile.
    public string? Comment { get; set; }

    // The course associated with the student profile review.
    public required StudentProfileReviewCourse StudentProfileReviewCourse { get; set; }
    
}