// This file contains the definition of the Like class, which represents a like given by a student to a review.

namespace UniUnboxdAPI.Models
{
    public class Like
    {
        public int ReviewId { get; set; } // The ID of the review that was liked
        public int StudentId { get; set; } // The ID of the student who gave the like
        public required Review Review { get; set; } // The review that was liked (required)
        public required Student Student { get; set; } // The student who gave the like (required)
    }
}
