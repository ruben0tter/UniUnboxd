// This file contains the definition of the Student class, which represents a student in the UniUnboxdAPI.
namespace UniUnboxdAPI.Models
{
    public class Student : User
    {
        public string? Image { get; set; } // The image of the student.
        public ICollection<Review>? Reviews { get; set; } // The collection of reviews written by the student.
        public ICollection<Reply>? Replies { get; set; } // The collection of replies written by the student.
        public ICollection<Follow>? Following { get; set; } // The collection of users that the student is following.
        public ICollection<Follow>? Followers { get; set; } // The collection of users that are following the student.
        public ICollection<Like>? Likes { get; set; } // The collection of likes received by the student.
        public required NotificationSettings NotificationSettings { get; set; } // The notification settings of the student.
        public string? DeviceToken { get; set; } // The device token of the student.
    }
}
