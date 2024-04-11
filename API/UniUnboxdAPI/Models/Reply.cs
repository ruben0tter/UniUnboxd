namespace UniUnboxdAPI.Models
{
    // This class represents a reply to a review.
    public class Reply : Base
    {
        // The text of the reply.
        public required string Text { get; set; }
        
        // The user who posted the reply.
        public required User User { get; set; } 
        
        // The review to which the reply is posted.
        public required Review Review { get; set; }
    }
}
