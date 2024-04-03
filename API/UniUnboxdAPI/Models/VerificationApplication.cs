namespace UniUnboxdAPI.Models;
public class VerificationApplication : Base {
    public required string[] VerificationData { get; set; }
    public required int UserId { get; set; }
    public University? TargetUniversity { get; set; }
}
