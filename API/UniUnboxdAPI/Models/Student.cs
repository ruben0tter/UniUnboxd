namespace UniUnboxdAPI.Models
{
    public class Student : User
    {
        public string? Image { get; set; }
        public ICollection<Review>? Reviews { get; set; }
        public ICollection<Reply>? Replies { get; set; }
        public ICollection<Follow>? Following { get; set; }
        public ICollection<Follow>? Followers { get; set; }
        public ICollection<Like>? Likes { get; set; }
        public required NotificationSettings NotificationSettings { get; set; }
        public string? DeviceToken { get; set; }
    }
}
