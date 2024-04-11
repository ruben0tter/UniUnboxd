// This file contains the definition of the Follow class, which represents a relationship between two students where one student follows another.
namespace UniUnboxdAPI.Models
{
    public class Follow
    {
        public int FollowingStudentId { get; set; } // The ID of the student who is following another student
        public int FollowedStudentId { get; set; } // The ID of the student who is being followed
        public required Student FollowingStudent { get; set; } // The instance of the following student
        public required Student FollowedStudent { get; set; } // The instance of the followed student
    }
}
