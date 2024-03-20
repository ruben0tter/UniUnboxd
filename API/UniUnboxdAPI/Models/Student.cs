namespace UniUnboxdAPI.Models
{
    public class Student : User
    {
        public string? Image { get; set; }
        public ICollection<Review>? Reviews { get; set; }
        public ICollection<Reply>? Replies { get; set; }
    }
}
