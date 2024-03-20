namespace UniUnboxdAPI.Models
{
    public class Reply : Base
    {
        public required string Text { get; set; }
        public required User User { get; set; } 
        public required Review Review { get; set; }
    }
}
