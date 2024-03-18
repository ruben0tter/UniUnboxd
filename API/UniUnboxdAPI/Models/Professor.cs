namespace UniUnboxdAPI.Models
{
    public class Professor : User
    {
        public string? Image { get; set; }
        public ICollection<Course>? AssignedCourses { get; set; }
        public ICollection<Reply>? Replies { get; set; }
    }
}
