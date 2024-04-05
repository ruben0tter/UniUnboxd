// This file contains the definition of the CourseRetrievalModel class.
// The CourseRetrievalModel class represents the data transfer object for retrieving course information.
// It includes properties such as Id, Name, Code, Description, Professor, Image, Banner, UniversityId, UniversityName, AnonymousRating, NonanonymousRating, Reviews, YourReview, AssignedProfessors, and FriendReviews.

using UniUnboxdAPI.Models.DataTransferObjects.CoursePage;

namespace UniUnboxdAPI.Models.DataTransferObjects
{
    // The CourseRetrievalModel class represents the data transfer object for retrieving course information.
    public class CourseRetrievalModel
    {
        // The Id property represents the unique identifier of the course.
        public required int Id { get; set; }

        // The Name property represents the name of the course.
        public required string Name { get; set; }

        // The Code property represents the code of the course.
        public required string Code { get; set; }

        // The Description property represents the description of the course.
        public required string Description { get; set; }

        // The Professor property represents the professor of the course.
        public required string Professor { get; set; }

        // The Image property represents the image associated with the course.
        public string? Image { get; set; }

        // The Banner property represents the banner associated with the course.
        public string? Banner { get; set; }

        // The UniversityId property represents the unique identifier of the university associated with the course.
        public required int UniversityId { get; set; }

        // The UniversityName property represents the name of the university associated with the course.
        public string? UniversityName { get; set; }

        // The AnonymousRating property represents the anonymous rating of the course.
        public double AnonymousRating { get; set; }

        // The NonanonymousRating property represents the non-anonymous rating of the course.
        public double NonanonymousRating { get; set; }

        // The Reviews property represents the collection of course reviews.
        public ICollection<CourseReviewModel>? Reviews { get; set; }

        // The YourReview property represents the course review submitted by the user.
        public CourseReviewModel? YourReview { get; set; }

        // The AssignedProfessors property represents the collection of assigned professors for the course.
        public ICollection<int>? AssignedProfessors { get; set; }

        // The FriendReviews property represents the collection of friend reviews for the course.
        public ICollection<FriendReview>? FriendReviews { get; set; }
    }
}
