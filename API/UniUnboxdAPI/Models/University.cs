namespace UniUnboxdAPI.Models
{
    public class University : User
    {
        public bool Verified { get; set; }
        public ICollection<Course>? Courses { get; set; }
        public ICollection<Student>? Students { get; set; }
    }
}
