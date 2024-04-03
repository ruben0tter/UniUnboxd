namespace UniUnboxdAPI.Models
{
    public class Like
    {
        public int ReviewId { get; set; }
        public int StudentId { get; set; }
        public required Review Review { get; set; }
        public required Student Student { get; set; }
    }
}
