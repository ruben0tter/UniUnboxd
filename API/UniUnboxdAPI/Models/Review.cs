namespace UniUnboxdAPI.Models;

public class Review : Base
{
    public required double Rating { get; set; }
    public string? Comment { get; set; }
    public required bool IsAnonymous { get; set; }
    public required Course Course { get; set; } 
    public required Student Student { get; set;  }
}