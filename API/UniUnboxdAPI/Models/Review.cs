// This class represents a review in the UniUnboxd application.
namespace UniUnboxdAPI.Models;

public class Review : Base
{
    // The rating given by the student for the course.
    public required double Rating { get; set; }
    
    // The comment provided by the student for the course.
    public required string Comment { get; set; }
    
    // Indicates whether the review is anonymous or not.
    public required bool IsAnonymous { get; set; }
    
    // The course for which the review is given.
    public required Course Course { get; set; } 
    
    // The student who wrote the review.
    public required Student Student { get; set;  }
    
    // The collection of replies received for the review.
    public required ICollection<Reply> Replies { get; set; }
    
    // The collection of likes received for the review.
    public required ICollection<Like> Likes { get; set; }
}