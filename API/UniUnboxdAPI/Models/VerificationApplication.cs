namespace UniUnboxdAPI.Models;
public class VerificationApplication : Base {
    public required string[] VerificationData { get; set; }
    // public required UserType UserType { get; set; }
    public required User UserToBeVerified { get; set; }
    public University? TargetUniversity { get; set; }
}
