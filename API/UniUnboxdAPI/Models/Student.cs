namespace UniUnboxdAPI.Models
{
    public class Student : User
    {
        public bool Verified { get; set; }
        public string? Image { get; set; }
        public University? University { get; set; }
        public ICollection<Review>? Reviews { get; set; }
    }
}
