namespace UniUnboxdAPI.Models;

public class Review : Base
{
    public double Rating { get; set; }
    public string? Comment { get; set; }
    public bool IsAnonymous { get; set; }
    public required Course Course { get; set; } 
    public required Student Student { get; set;  }
}